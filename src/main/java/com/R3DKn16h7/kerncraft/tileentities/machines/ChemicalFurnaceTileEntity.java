package com.R3DKn16h7.kerncraft.tileentities.machines;

import com.R3DKn16h7.kerncraft.capabilities.element.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.element.IElementContainer;
import com.R3DKn16h7.kerncraft.client.gui.MachineGuiContainer;
import com.R3DKn16h7.kerncraft.crafting.ChemicalFurnaceRecipe;
import com.R3DKn16h7.kerncraft.crafting.KernCraftRecipes;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import com.R3DKn16h7.kerncraft.tileentities.SmeltingTileEntity;
import com.R3DKn16h7.kerncraft.tileentities.utils.Upgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ChemicalFurnaceTileEntity extends SmeltingTileEntity<ChemicalFurnaceRecipe> {

    // Slot IDs
    private static final int[][] inputCoords = {{4, 0}, {5, 0}, {3, 0}, {6, 0}};
    private static final int[][] outputCoords = {{4, 2}, {5, 2}, {3, 2}, {6, 2}};

    public ChemicalFurnaceTileEntity() {
        super(2);
    }


    @Override
    public int getInputSize() {
        int upg = getUpgrade().getCount(Upgrade.EXTRA_SLOT);
        int[] slots = {2, 3, 4};
        return slots[Math.min(upg, slots.length - 1)];
    }

    @Override
    public int getOutputSize() {
        int upg = getUpgrade().getCount(Upgrade.EXTRA_SLOT);
        int[] slots = {2, 3, 4};
        return slots[Math.min(upg, slots.length - 1)];
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
    public List<ChemicalFurnaceRecipe> getRecipes() {

        return KernCraftRecipes.CHEMICAL_FURNACE_RECIPES;
    }

    @Override
    public int[] getFuelIconCoordinate() {
        return new int[]{-4, 1};
    }

    @Override
    public Tuple<Integer[], MachineGuiContainer.ProgressIcon> getProgressIconCoordinate() {
        return new Tuple<>(new Integer[]{-4, 1}, MachineGuiContainer.ProgressIcon.ARROW_DOWN);
    }

    @Override
    public int[] getProgressTextCoordinate() {
        return new int[]{6, 1};
    }

    @Override
    public boolean canSmelt(ChemicalFurnaceRecipe chemrec) {

        // TODO: return false if tank full?
        if (chemrec.fluid != null) {
            if (tank.size() < 1) return false;
            if (getFluid(0) == null || getFluid(0).isFluidEqual(chemrec.fluid)) {
                if (chemrec.fluid.amount < 0) {
                    if (-chemrec.fluid.amount > getFluid(0).amount) return false;
                    FluidStack positiveFluid = new FluidStack(chemrec.fluid, -chemrec.fluid.amount);
                    // If we cannot drain sufficient amount of fluid, just go home
                    if (-chemrec.fluid.amount > tank.get(0).drain(positiveFluid, false).amount) {
                        return false;
                    }
                } else {
                    tank.get(0).fill(chemrec.fluid, false);
                }
            }
        }

        // Try use inputs, if fail return
        for (ElementStack elem : chemrec.inputs) {
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

        int consumedEnergy = -currentlySmelting.energy / currentlySmelting.cost;
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
        if (!canSmelt(currentlySmelting)) return;

        // Actually remove inputs
        for (ElementStack elem : currentlySmelting.inputs) {
            if (elem == null) continue;
            int to_remove = elem.quantity;
            for (int i = 0; i < input.getSlots(); ++i) {
                ItemStack stack = input.getStackInSlot(i);
                if (ElementCapabilities.hasCapability(stack)) {
                    IElementContainer cap = ElementCapabilities.getCapability(stack);
                    to_remove -= cap.removeAmountOf(elem.id, to_remove, false);
                    if (to_remove <= 0) {
                        break;
                    }
                }
            }
            // TODO: WARN IF FAIL
        }

        // Actually create output once we verified we can
        if (currentlySmelting.fluid != null) {
            FluidStack positiveFluid = new FluidStack(currentlySmelting.fluid, -currentlySmelting.fluid.amount);
            if (currentlySmelting.fluid.amount <= 0) {
                tank.get(0).drain(positiveFluid, true);
            } else {
                tank.get(0).fill(currentlySmelting.fluid, true);
            }
        }
        // TODO: WARN IF FAIL

        // Create elements once we know we can
        if (currentlySmelting.outputs != null) {
            for (ItemStack out : currentlySmelting.outputs) {
                int j = 0;
                ItemStack remaining = out.copy();
                while (j < output.getSlots()) {
                    if (remaining.isEmpty()) break;
                    remaining = output.insertItem(j, remaining, false);
                }
                // TODO: WARN IF FAILS
            }
        }

        int producedEnergy = currentlySmelting.energy;
        if (producedEnergy > 0) {
            storage.receiveEnergy(producedEnergy, false);
        }

        markDirty();
        world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
    }
}
