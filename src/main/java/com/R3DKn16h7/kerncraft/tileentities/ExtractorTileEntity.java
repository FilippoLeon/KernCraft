package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.crafting.KernCraftRecipes;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import com.R3DKn16h7.kerncraft.items.Canister;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import com.R3DKn16h7.kerncraft.crafting.ExtractorRecipe;
import com.R3DKn16h7.kerncraft.crafting.ISmeltingRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
    static private int generatedFuelPerEnergyDrain = 100;
    static private final int consumedFuelPerTic = 20;

    public int[][] inputCoords = {{1,1},{3,1},{8,1},{1,3}};
    public int[][] outputCoords = {{5,3},{6,3},{7,3},{8,3}};

    @Override
    public int[][] getInputCoords() {return inputCoords; }
    @Override
    public int[][] getOutputCoords() {return outputCoords; }

    @Override
    public List<ISmeltingRecipe> getRecipes() {
        return KernCraftRecipes.EXTRACTOR_RECIPES;
    }

    public ExtractorTileEntity() {
        super(4, 4);

        generatedFuelPerEnergyDrain = TileEntityFurnace.getItemBurnTime(new ItemStack(Items.COAL));
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
            int fuel = TileEntityFurnace.getItemBurnTime(fuelItem); //GameRegistry.getFuelValue(fuelItem);
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

        input.setStackInSlot(inputSlot, new ItemStack(input.getStackInSlot(inputSlot).getItem(),
                input.getStackInSlot(inputSlot).getCount() - 1));
        if (input.getStackInSlot(inputSlot).getCount() == 0) input.setStackInSlot(inputSlot, ItemStack.EMPTY);
        if (recipe.catalyst != null) {
            input.setStackInSlot(catalystSlot, new ItemStack(input.getStackInSlot(catalystSlot).getItem(),
                    input.getStackInSlot(catalystSlot).getCount() - 1));
            if (input.getStackInSlot(catalystSlot).getCount() == 0) input.setStackInSlot(catalystSlot, ItemStack.EMPTY);
        }

        // For each Element output, flag telling if this has already been produced
        int[] remaining = new int[recipe.outs.length];
        for (int i = 0; i < recipe.outs.length; ++i) {
            remaining[i] = recipe.outs[i].quantity;
        }

        // Place outputs in canisters, only if output can be placed (i.e. there isn't another element inside.
        // Try each output slot
        for (int rec_slot = 0; rec_slot < outputSlotSize; ++rec_slot) {
            ItemStack out = output.getStackInSlot(rec_slot);
            if (out == ItemStack.EMPTY) {
                continue;
            }

            NBTTagCompound nbt;
            if (out.hasTagCompound()) {
                nbt = out.getTagCompound();
            } else {
                nbt = new NBTTagCompound();
            }

            // Try each generated element
            int i = 0;
            for (ElementStack rec_out : recipe.outs) {
                // If all output has been produced break
                if (remaining[i] <= 0) {
                    ++i;
                    continue;
                }
                if (nbt.hasKey("Element")) {
                    int elemId = nbt.getInteger("Element");
                    int qty = nbt.getInteger("Quantity");
                    if (elemId == rec_out.id) {
                        if (remaining[i] + qty <= Canister.CAPACITY) {
                            nbt.setInteger("Quantity", remaining[i] + qty);
                            remaining[i] = 0;
                            out.setTagCompound(nbt);
                            break;
                        } else {
                            remaining[i] -= Canister.CAPACITY - qty;
                            nbt.setInteger("Quantity", Canister.CAPACITY);
                            out.setTagCompound(nbt);
                        }
                    }
                } else {
                    nbt.setInteger("Element", rec_out.id);
                    nbt.setInteger("Quantity", rec_out.quantity);
                    out.setTagCompound(nbt);
                    remaining[i] = 0;
                    break;
                }
                ++i;
            }

        }

        // If there is a CANISTER in CANISTER slot, pull if to the output slot and
        // fill it with element
        // Try each output slot
        for (int rec_slot = 0; rec_slot < outputSlotSize; ++rec_slot) {

            ItemStack out = output.getStackInSlot(rec_slot);
            if (out == ItemStack.EMPTY) {
                // Try each element
                int i = 0;
                for (ElementStack rec_out : recipe.outs) {

                    // If Element has not been produced and there is a CANISTER in
                    // the slot
                    if (remaining[i] > 0 && input.getStackInSlot(canisterSlot) != ItemStack.EMPTY &&
                            input.getStackInSlot(canisterSlot).getCount() > 0 &&
                            input.getStackInSlot(canisterSlot).getItem() == KernCraftItems.CANISTER) {
                        remaining[i] = 0;
                        output.setStackInSlot(rec_slot, new ItemStack(input.getStackInSlot(canisterSlot).getItem(), 1));
                        input.setStackInSlot(canisterSlot, new ItemStack(input.getStackInSlot(canisterSlot).getItem(),
                                input.getStackInSlot(canisterSlot).getCount() - 1));
                        if (input.getStackInSlot(canisterSlot).getCount() == 0) input.setStackInSlot(canisterSlot,  ItemStack.EMPTY);
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setInteger("Element", rec_out.id);
                        nbt.setInteger("Quantity", rec_out.quantity);
                        output.getStackInSlot(rec_slot).setTagCompound(nbt);
                        break;
                    }

                    ++i;
                }
            }
        }
        this.markDirty();
    }


}
