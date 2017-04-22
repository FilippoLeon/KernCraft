package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.capabilities.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.IElementContainer;
import com.R3DKn16h7.kerncraft.crafting.ChemicalFurnaceRecipe;
import com.R3DKn16h7.kerncraft.crafting.ISmeltingRecipe;
import com.R3DKn16h7.kerncraft.crafting.KernCraftRecipes;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class ChemicalFurnaceTileEntity extends SmeltingTileEntity {

    // Slot IDs
    static final public int inputSlotStart = 0;
    static final public int inputSlotSize = 2;
    static final public int outputSlotStart = 0;
    static final public int outputSlotSize = 2;
    static final public int totalSlots = 4;
    public int[][] inputCoords = {{4,1},{5,1}};
    public int[][] outputCoords = {{4,3},{5,3}};

    public ChemicalFurnaceTileEntity() {
        super(2, 2);
    }

    static private List<Integer> itemStackHandlerMatchesElementStack(ItemStackHandler stackHandler,
                                                                     ElementStack elem) {
        List<Integer> match = new ArrayList<>();
        for (int i = 0; i < stackHandler.getSlots(); ++i) {
            ItemStack stack = stackHandler.getStackInSlot(i);

            if (stack != ItemStack.EMPTY
                    && ElementCapabilities.hasCapability(stack)
                    && ElementCapabilities.getCapability(stack).getAmountOf(elem.id) >= elem.quantity
                    ) {
                match.add(i);
            }
        }
        return match;
    }

    @Override
    public int[][] getInputCoords() {
        return inputCoords;
    }

    @Override
    public int[][] getOutputCoords() {
        return outputCoords;
    }

    @Override
    public List<ISmeltingRecipe> getRecipes() {
        return KernCraftRecipes.CHEMICAL_FURNACE_RECIPES;
    }

    @Override
    public int[] getFuelIconCoordinate() {
        return new int[]{-4,1};
    }

    @Override
    public int[] getProgressIconCoordinate() {
        return null;
    }

    @Override
    public int[] getProgressTextCoordinate() {
        return new int[]{6,1};
    }

    @Override
    public int getTotalSlots() {
        return totalSlots;
    }

    @Override
    public boolean canSmelt(ISmeltingRecipe rec) {
        ChemicalFurnaceRecipe chemrec = ((ChemicalFurnaceRecipe) rec);
        if(chemrec == null) return false;

        if(chemrec.fluid != null && chemrec.fluid.amount < 0) {
            if(getFluid().equals(chemrec.fluid)) {
                if(getFluidAmount() < -chemrec.fluid.amount) {
                    return false;
                }
            } else {
                return false;
            }
        }

        boolean[] matchInput = new boolean[2];
        for(ElementStack elem: chemrec.inputs) {
            List<Integer> slot = itemStackHandlerMatchesElementStack(input, elem);
            if(slot != null) {
                for(int slotId: slot) {
                    if(!matchInput[slotId]) {
                        matchInput[slotId] = true;
                        break;
                    }
                }
            }
        }
        for(boolean match: matchInput) {
            if(!match) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean tryProgress() {

        return true;
    }

    @Override
    public void doneSmelting() {
        ChemicalFurnaceRecipe chemrec = ((ChemicalFurnaceRecipe) currentlySmelting);

        if(chemrec.fluid != null) {
            if (getFluid() == null || getFluid().isFluidEqual(chemrec.fluid)) {
                if (chemrec.fluid.amount < 0) {
                    if(-chemrec.fluid.amount > getFluid().amount) return;
                    FluidStack positiveFluid = new FluidStack(chemrec.fluid, -chemrec.fluid.amount);
                    tank.drain(positiveFluid, true);
                    // TODO: IF FAIL ?
//                    return;
                } else {
                    tank.fill(chemrec.fluid, true);
                }
            }
        }

        // TODO: IF FAIL ?
        for(ElementStack elem: chemrec.inputs) {
            int to_remove = elem.quantity;
            for(int i = 0; i < input.getSlots(); ++i) {
                ItemStack stack = input.getStackInSlot(i);
                if (ElementCapabilities.hasCapability(stack)) {
                    IElementContainer cap = ElementCapabilities.getCapability(stack);
                    to_remove -= cap.removeAmountOf(elem.id, to_remove, false);
                    if(to_remove <= 0) {
                        break;
                    }
                }
            }
        }

        int producedEnergy = chemrec.energy;
        if(producedEnergy > 0) {
            storage.receiveEnergy(producedEnergy, false);
        }

        markDirty();
        world.scheduleBlockUpdate(pos, getBlockType(), 0,0);
    }
}
