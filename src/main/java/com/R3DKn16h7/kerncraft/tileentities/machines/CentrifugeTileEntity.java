package com.R3DKn16h7.kerncraft.tileentities.machines;

import com.R3DKn16h7.kerncraft.capabilities.element.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.element.IElementContainer;
import com.R3DKn16h7.kerncraft.client.gui.MachineGuiContainer;
import com.R3DKn16h7.kerncraft.crafting.CentrifugeRecipe;
import com.R3DKn16h7.kerncraft.crafting.KernCraftRecipes;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import com.R3DKn16h7.kerncraft.tileentities.SmeltingTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

import java.util.List;
import java.util.Random;

public class CentrifugeTileEntity extends SmeltingTileEntity<CentrifugeRecipe> {

    // Slot IDs
    private static final int[][] inputCoords = {{3, 0}, {3, 2}};
    private static final int[][] outputCoords = {
            {5, 0}, {5, 1}, {5, 2},
            {6, 0}, {6, 1}, {6, 2}
    };


    public CentrifugeTileEntity() {
        super(1);
    }

    @Override
    public int getInputSize() {
        return 2;
    }

    @Override
    public int getOutputSize() {
        return 6;
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
    public List<CentrifugeRecipe> getRecipes() {

        return KernCraftRecipes.CENTRIFUGE_RECIPES;
    }

    @Override
    public int[] getFuelIconCoordinate() {
        return new int[]{-4, 1};
    }

    @Override
    public Tuple<Integer[], MachineGuiContainer.ProgressIcon> getProgressIconCoordinate() {

        return null;
    }

    @Override
    public int[] getProgressTextCoordinate() {
        return new int[]{6, 1};
    }

    @Override
    public boolean canSmelt(CentrifugeRecipe recipe) {
        for (ItemStack stack : recipe.inputs) {
            int left = stack.getCount();
            for (int i = 0; i < getInputSize(); ++i) {
                if (getInput().getStackInSlot(i).isItemEqual(stack)) {
                    left -= getInput().getStackInSlot(i).getCount();
                    if (left <= 0) {
                        break;
                    }
                }
            }
            if (left > 0) return false;
        }

        if (recipe.fluid != null && getFluid(0) != null
                && getFluid(0).isFluidEqual(recipe.fluid)) {
            if (recipe.fluid.amount > tank.get(0).drain(recipe.fluid, false).amount) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean tryProgress() {
        if (storage.extractEnergy(currentlySmelting.energy, true)
                != currentlySmelting.energy) {
            return false;
        }

        storage.extractEnergy(currentlySmelting.energy, false);
        return true;
    }

    @Override
    public void doneSmelting() {
        // Consume items
        for (ItemStack stack : currentlySmelting.inputs) {
            int left = stack.getCount();
            for (int i = 0; i < getInputSize(); ++i) {
                if (getInput().getStackInSlot(i).isItemEqual(stack)) {
                    ItemStack res = getInput().getStackInSlot(i).splitStack(left);
                    left -= res.getCount();
                    if (left <= 0) {
                        break;
                    }
                }
            }
        }

        // Consume fluid
        if (currentlySmelting.fluid != null) {
            tank.get(0).drain(currentlySmelting.fluid, true);
        }

        Random rand = new Random();

        // Create elements
        for (ElementStack created : currentlySmelting.outputs) {

            if (rand.nextFloat() > created.probability) continue;

            int leftover = created.quantity;
            for (int i = 0; i < getOutputCoords().length; ++i) {
                ItemStack out = getOutput().getStackInSlot(i);
                if (!ElementCapabilities.hasCapability(out)) continue;
                IElementContainer cap = ElementCapabilities.getCapability(out);
                leftover -= cap.addAmountOf(created.id, leftover, false, this);
                if (leftover <= 0) break;
            }
        }

        markDirty();
        world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
    }
}
