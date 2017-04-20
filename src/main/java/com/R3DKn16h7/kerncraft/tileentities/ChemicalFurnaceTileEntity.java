package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.crafting.ISmeltingRecipe;
import com.R3DKn16h7.kerncraft.crafting.KernCraftRecipes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

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
        return null;
    }

    @Override
    public int[] getFuelIconCoordinate() {
        return new int[]{4,5};
    }

    @Override
    public int[] getProgressIconCoordinate() {
        return null;
    }

    @Override
    public int[] getProgressTextCoordinate() {
        return new int[]{3,3};
    }

    @Override
    public int getTotalSlots() {
        return totalSlots;
    }

    @Override
    public boolean canSmelt(ISmeltingRecipe rec) {
        return !input.getStackInSlot(0).isEmpty() &&
                input.getStackInSlot(0).getItem() ==
                        Item.getItemFromBlock(Blocks.IRON_BLOCK);
    }

    @Override
    public boolean tryProgress() {
        return false;
    }

    @Override
    public void doneSmelting() {
        int a = storage.receiveEnergy(100, false);
    }
}
