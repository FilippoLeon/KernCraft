package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.capabilities.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.IElementContainer;
import com.R3DKn16h7.kerncraft.crafting.ChemicalFurnaceRecipe;
import com.R3DKn16h7.kerncraft.crafting.ISmeltingRecipe;
import com.R3DKn16h7.kerncraft.crafting.KernCraftRecipes;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ChemicalFurnaceTileEntity extends SmeltingTileEntity {

    // Slot IDs
    public static final int[][] inputCoords = {{4, 0}, {5, 0}};
    public static final int[][] outputCoords = {{4, 2}, {5, 2}};

    public ChemicalFurnaceTileEntity() {

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
    public boolean canSmelt(ISmeltingRecipe rec) {
        ChemicalFurnaceRecipe chemrec = ((ChemicalFurnaceRecipe) rec);
        if(chemrec == null) return false;

        // TODO: return false if tank full?
        if (chemrec.fluid != null) {
            if (getFluid() == null || getFluid().isFluidEqual(chemrec.fluid)) {
                if (chemrec.fluid.amount < 0) {
                    if (-chemrec.fluid.amount > getFluid().amount) return false;
                    FluidStack positiveFluid = new FluidStack(chemrec.fluid, -chemrec.fluid.amount);
                    // If we cannot drain sufficient amount of fluid, just go home
                    if (-chemrec.fluid.amount > tank.drain(positiveFluid, false).amount) {
                        return false;
                    }
                } else {
                    tank.fill(chemrec.fluid, false);
                }
            }
        }

        // Try use inputs, if fail return
        for(ElementStack elem: chemrec.inputs) {
            if (elem == null) continue;
            int to_remove = elem.quantity;
            for (int i = 0; i < input.getSlots(); ++i) {
                ItemStack stack = input.getStackInSlot(i);
                if (ElementCapabilities.hasCapability(stack)) {
                    IElementContainer cap = ElementCapabilities.getCapability(stack);
                    to_remove -= cap.removeAmountOf(elem.id, to_remove, true);
                    if (to_remove <= 0) {
                        break;
                    }
                }
            }
            if (to_remove > 0) {
                return false;
            }
        }

        // Try create outs, if fails walk away
        if (chemrec.outputs != null) {
            for (ItemStack out : chemrec.outputs) {
                int j = 0;
                ItemStack remaining = out.copy();
                while (j < output.getSlots()) {
                    if (remaining.isEmpty()) break;
                    remaining = output.insertItem(j, remaining, true);
                }
                if (!remaining.isEmpty()) return false;
            }
        }

        return true;
    }

    @Override
    public boolean tryProgress() {
        ChemicalFurnaceRecipe chemrec = ((ChemicalFurnaceRecipe) currentlySmelting);
        if (chemrec == null) return true;

        int consumedEnergy = -chemrec.energy / chemrec.cost;
        if (consumedEnergy > 0) {
            int extracted = storage.extractEnergy(consumedEnergy, true);
            if (extracted == consumedEnergy) {
                storage.extractEnergy(consumedEnergy, false);
                return true;
            } else {
                return false;
            }
        }

        return true;
    }

    @Override
    public void doneSmelting() {
        ChemicalFurnaceRecipe chemrec = ((ChemicalFurnaceRecipe) currentlySmelting);
        if (chemrec == null) return;

        if (!canSmelt(chemrec)) return;

        // Actually remove inputs
        for(ElementStack elem: chemrec.inputs) {
            if (elem == null) continue;
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
            // TODO: WARN IF FAIL
        }

        // Actually create output once we verified we can
        if (chemrec.fluid != null) {
            FluidStack positiveFluid = new FluidStack(chemrec.fluid, -chemrec.fluid.amount);
            if (chemrec.fluid.amount <= 0) {
                tank.drain(positiveFluid, true);
            } else {
                tank.fill(chemrec.fluid, true);
            }
        }
        // TODO: WARN IF FAIL

        // Create elements once we know we can
        if (chemrec.outputs != null) {
            for (ItemStack out : chemrec.outputs) {
                int j = 0;
                ItemStack remaining = out.copy();
                while (j < output.getSlots()) {
                    if (remaining.isEmpty()) break;
                    remaining = output.insertItem(j, remaining, false);
                }
                // TODO: WARN IF FAIL
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
