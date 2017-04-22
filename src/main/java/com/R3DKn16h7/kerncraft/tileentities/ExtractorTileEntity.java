package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.capabilities.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.IElementContainer;
import com.R3DKn16h7.kerncraft.crafting.ExtractorRecipe;
import com.R3DKn16h7.kerncraft.crafting.ISmeltingRecipe;
import com.R3DKn16h7.kerncraft.crafting.KernCraftRecipes;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;

public class ExtractorTileEntity extends SmeltingTileEntity
        implements ICapabilityProvider {

    // Slot IDs
    static final public int inputSlot = 0;
    static final public int catalystSlot = 1;
    static final public int canisterSlot = 2;
    static final public int fuelSlot = 3;
    static final public int outputSlotSize = 4;
    static final public int totalSlots = 8;
    //// Static constants
    static private final int consumedEnergyPerFuelRefill = 100;
    static private final int consumedFuelPerTic = 20;
    static private int generatedFuelPerEnergyDrain = 100;
    public int[][] inputCoords = {{1,1},{3,1},{8,1},{1,3}};
    public int[][] outputCoords = {{5,3},{6,3},{7,3},{8,3}};

    public ExtractorTileEntity() {
        super(4, 4);

        generatedFuelPerEnergyDrain = TileEntityFurnace.getItemBurnTime(new ItemStack(Items.COAL));
    }

    @Override
    public int[][] getInputCoords() {return inputCoords; }

    @Override
    public int[][] getOutputCoords() {return outputCoords; }

    @Override
    public int[] getFuelIconCoordinate() {
        return new int[]{1,1};
    }

    @Override
    public int[] getProgressIconCoordinate() {
        return new int[]{3,1};
    }

    @Override
    public int[] getProgressTextCoordinate() {
        return new int[]{5,0};
    }

    @Override
    public List<ISmeltingRecipe> getRecipes() {
        return KernCraftRecipes.EXTRACTOR_RECIPES;
    }

    @Override
    public boolean canSmelt(ISmeltingRecipe rec) {
        ExtractorRecipe ex_rec = ((ExtractorRecipe) rec);

        return !(input == null ||
                input.getStackInSlot(inputSlot) == ItemStack.EMPTY ||
                // Check input
                input.getStackInSlot(inputSlot).getItem() != ex_rec.item ||
                input.getStackInSlot(inputSlot).getCount() < 1 ||
                // Check catalyst
                ex_rec.catalyst != null &&
                        (input.getStackInSlot(catalystSlot) == ItemStack.EMPTY ||
                                input.getStackInSlot(catalystSlot).getItem() != ex_rec.catalyst ||
                                input.getStackInSlot(catalystSlot).getCount() < 1
                        ));
    }

    @Override
    public int getTotalSlots() {
        return totalSlots;
    }

    /**
     * Try and consume one unit of fuel (depending on recipe)
     * if it fails, return false.
     * @return
     */
    @Override
    public boolean tryProgress() {
        ExtractorRecipe recipe = ((ExtractorRecipe) currentlySmelting);
        if (storedFuel < consumedFuelPerTic) {
            ItemStack fuelItem = input.getStackInSlot(fuelSlot);
            int fuel = TileEntityFurnace.getItemBurnTime(fuelItem);
            //GameRegistry.getFuelValue(fuelItem);
            if (storage.getEnergyStored() > consumedEnergyPerFuelRefill) {
                storage.extractEnergy(consumedEnergyPerFuelRefill, false);
                storedFuel += generatedFuelPerEnergyDrain;
            } else if (fuelItem != ItemStack.EMPTY &&
                    fuelItem.getCount() >= 1 &&
                    fuel > 0) {
                fuelItem.setCount(fuelItem.getCount() - 1);
                if (fuelItem.getCount() == 0) {
//                    fuelItem = null;
                    input.setStackInSlot(fuelSlot, ItemStack.EMPTY);
                }
                storedFuel += fuel;
            } else {
                return false;
            }
        }
        storedFuel -= consumedFuelPerTic;
        return true;
    }

    @Override
    public void doneSmelting() {
        ExtractorRecipe recipe = ((ExtractorRecipe) currentlySmelting);

        input.getStackInSlot(inputSlot).splitStack(1);
        if (recipe.catalyst != null) {
            input.getStackInSlot(catalystSlot).splitStack(1);
        }

        // For each Element output, flag telling if this has already been produced
        int[] remaining = new int[recipe.outs.length];
        for (int i = 0; i < recipe.outs.length; ++i) {
            remaining[i] = recipe.outs[i].quantity * (Math.random() < recipe.outs[i].prob ? 1 : 0);
        }

        // Place outputs in canisters, only if output can be placed
        // (i.e. there isn't another element inside.
        // Try each output slot
        for (int rec_slot = 0; rec_slot < outputSlotSize; ++rec_slot) {
            ItemStack out = output.getStackInSlot(rec_slot);
            if (out == ItemStack.EMPTY || !ElementCapabilities.hasCapability(out)) {
                continue;
            }

            IElementContainer cap = ElementCapabilities.getCapability(out);

            // Try each generated element
            int recipeElementNumber = 0;
            for (ElementStack rec_out : recipe.outs) {
                // If all output has been produced break
                if (remaining[recipeElementNumber] <= 0) {
                    ++recipeElementNumber;
                    continue;
                }

                remaining[recipeElementNumber] -= cap.addAmountOf(rec_out.id, remaining[recipeElementNumber], false);
                ++recipeElementNumber;
            }
        }

        boolean finished = true;
        for (int aRemaining : remaining) {
            if (aRemaining > 0) finished = false;
        }
        if (finished) return;

        // If there is a CANISTER in CANISTER slot, pull if to the output slot and
        // fill it with element
        // Try each output slot
        for (int rec_slot = 0; rec_slot < outputSlotSize; ++rec_slot) {


            ItemStack out = output.getStackInSlot(rec_slot);
            if (out == ItemStack.EMPTY) {
                // Try each element
                int i = 0;
                for (ElementStack rec_out : recipe.outs) {

                    if (out == ItemStack.EMPTY) {
                        // If Element has not been produced and there is a CANISTER in
                        // the slot
                        ItemStack containerStack = input.getStackInSlot(canisterSlot);
                        if (containerStack == ItemStack.EMPTY || !ElementCapabilities.hasCapability(containerStack)) {
                            ++i;
                            continue;
                        }

                        // Try to add element to item in canister slot.
                        // If you can, pull the stack down
                        IElementContainer cap = ElementCapabilities.getCapability(containerStack);
                        int tryAdd = cap.addAmountOf(rec_out.id, remaining[i], true);
                        if (tryAdd > 0) {
                            ItemStack newStack = containerStack.splitStack(1);
                            IElementContainer newCap = ElementCapabilities.getCapability(newStack);
                            remaining[i] -= newCap.addAmountOf(rec_out.id, remaining[i], false);
                            output.setStackInSlot(rec_slot, newStack);
                            out = newStack;
                        }
                    } else {
                        IElementContainer cap = ElementCapabilities.getCapability(out);
                        remaining[i] -= cap.addAmountOf(rec_out.id, remaining[i], false);
                    }

                    ++i;
                }
            }
        }
        this.markDirty();
    }


}
