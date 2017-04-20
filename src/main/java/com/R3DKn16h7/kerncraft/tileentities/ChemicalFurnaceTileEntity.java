package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.crafting.ChemicalFurnaceRecipe;
import com.R3DKn16h7.kerncraft.crafting.ISmeltingRecipe;
import com.R3DKn16h7.kerncraft.crafting.KernCraftRecipes;
import com.R3DKn16h7.kerncraft.elements.ElementBase;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import com.R3DKn16h7.kerncraft.items.Canister;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ChemicalFurnaceTileEntity extends SmeltingTileEntity {

    // Slot IDs
    static final public int inputSlotStart = 0;
    static final public int inputSlotSize = 2;
    static final public int outputSlotStart = 0;
    static final public int outputSlotSize = 2;
    static final public int totalSlots = 4;

    public ChemicalFurnaceTileEntity() {
        super(2, 2);
    }

    public int[][] inputCoords = {{4,1},{5,1}};
    public int[][] outputCoords = {{4,3},{5,3}};

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

    static private List<Integer> itemStackHandlerMatchesElementStack(ItemStackHandler stackHandler, ElementStack elem) {
        List<Integer> match = new ArrayList<>();
        for(int i = 0; i < stackHandler.getSlots(); ++i) {
            ItemStack stack = stackHandler.getStackInSlot(i);
            if(stack != ItemStack.EMPTY && elem.isContainedInStack(stack)) {
                match.add(i);
            }
        }
        return match;
    }

    @Override
    public boolean tryProgress() {

        return true;
    }

    @Override
    public void doneSmelting() {
        ChemicalFurnaceRecipe chemrec = ((ChemicalFurnaceRecipe) currentlySmelting);

        if(chemrec.fluid != null) {
            if (getFluid().isFluidEqual(chemrec.fluid) || getFluid() == null) {
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
                if(elem.isContainedInStackAnyQuantity(input.getStackInSlot(i))) {
                    int canBeRemoved = Math.min(to_remove, ElementStack.getQuantity(input.getStackInSlot(i)));
                    ElementStack.removeFromStack(input.getStackInSlot(i), canBeRemoved);

                    to_remove -= canBeRemoved;
                    if(to_remove <= 0) {
                        continue;
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
