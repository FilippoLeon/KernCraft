package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.client.gui.MachineGuiContainer;
import com.R3DKn16h7.kerncraft.crafting.ISmeltingRecipe;
import com.R3DKn16h7.kerncraft.crafting.KernCraftRecipes;
import net.minecraft.util.Tuple;

import java.util.List;

public class CentrifugeTileEntity extends SmeltingTileEntity {

    // Slot IDs
    public static final int[][] inputCoords = {{3, 0}, {3, 2}};
    public static final int[][] outputCoords = {{5, 0}, {5, 2}};

    public CentrifugeTileEntity() {

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
    public boolean canSmelt(ISmeltingRecipe rec) {
        // TODO
        return false;
    }

    @Override
    public boolean tryProgress() {
        // TODO
        return false;
    }

    @Override
    public void doneSmelting() {
        // TODO

        markDirty();
        world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
    }
}
