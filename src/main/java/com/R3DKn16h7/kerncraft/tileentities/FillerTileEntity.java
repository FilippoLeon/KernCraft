package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.crafting.ISmeltingRecipe;

import java.util.List;

public class FillerTileEntity extends SmeltingTileEntity {

    // Slot IDs
    public static final int[][] inputCoords = {{3, 0}, {4, 0}, {5, 0}, {6, 0}};
    public static final int[][] outputCoords = {{3, 2}, {4, 2}, {5, 2}, {6, 2}};

    public FillerTileEntity() {
        super();


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
        // TODO
        return null;
    }

    @Override
    public int[] getFuelIconCoordinate() {
        return new int[]{-4, 1};
    }

    @Override
    public int[] getProgressIconCoordinate() {
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
