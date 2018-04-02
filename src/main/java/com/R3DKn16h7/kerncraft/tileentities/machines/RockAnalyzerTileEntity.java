package com.R3DKn16h7.kerncraft.tileentities.machines;

import com.R3DKn16h7.kerncraft.client.gui.MachineGuiContainer;
import com.R3DKn16h7.kerncraft.crafting.KernCraftRecipes;
import com.R3DKn16h7.kerncraft.crafting.RockAnalyzerRecipe;
import com.R3DKn16h7.kerncraft.tileentities.SmeltingTileEntity;
import net.minecraft.util.Tuple;

import java.util.List;

public class RockAnalyzerTileEntity extends SmeltingTileEntity<RockAnalyzerRecipe> {

    // Slot IDs
    private static final int[][] inputCoords = {{3, 0}, {3, 2}};
    private static final int[][] outputCoords = {
            {5, 0}, {5, 1}, {5, 2},
            {6, 0}, {6, 1}, {6, 2}
    };


    public RockAnalyzerTileEntity() {
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
    public List<RockAnalyzerRecipe> getRecipes() {

        return KernCraftRecipes.ROCK_ANALYZER_RECIPES;
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
    public boolean canSmelt(RockAnalyzerRecipe recipe) {
        return false;
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
        return;

        // markDirty();
        // world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
    }
}
