package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.capabilities.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.IElementContainer;
import com.R3DKn16h7.kerncraft.client.gui.MachineGuiContainer;
import com.R3DKn16h7.kerncraft.crafting.ISmeltingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

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
        return null;
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
        return true;
    }

    @Override
    protected double progressPerOperation() {
        return 1. / 5.;
    }

    @Override
    public boolean tryProgress() {
        if (storage.extractEnergy(40, true) != 40) {
            return false;
        }

        // TODO
        int amount = 0;
        for (int i = 0; i < getInputCoords().length; ++i) {
            int maxTotAmount = 100;

            ItemStack from = getInput().getStackInSlot(i);
            if (from == ItemStack.EMPTY || !ElementCapabilities.hasCapability(from)) continue;
            IElementContainer capFrom = ElementCapabilities.getCapability(from);

            for (int j = 0; j < getOutputCoords().length; ++j) {

                ItemStack to = getOutput().getStackInSlot(j);
                if (to == ItemStack.EMPTY || !ElementCapabilities.hasCapability(to)) continue;
                IElementContainer capTo = ElementCapabilities.getCapability(to);

                amount += ElementCapabilities.transferAllElements(capFrom, capTo,
                        maxTotAmount, this,
                        true, null);
                if (amount > 0) break;
            }
            if (amount > 0) break;
        }
        if (amount == 0) return false;

//        storage.extractEnergy(40, false);
        return true;
    }

    @Override
    public void doneSmelting() {
        // TODO
        for (int i = 0; i < getInputCoords().length; ++i) {
            int maxTotAmount = 100;

            ItemStack from = getInput().getStackInSlot(i);
            if (from == ItemStack.EMPTY || !ElementCapabilities.hasCapability(from)) continue;
            IElementContainer capFrom = ElementCapabilities.getCapability(from);

            for (int j = 0; j < getOutputCoords().length; ++j) {

                ItemStack to = getOutput().getStackInSlot(j);
                if (to == ItemStack.EMPTY || !ElementCapabilities.hasCapability(to)) continue;
                IElementContainer capTo = ElementCapabilities.getCapability(to);

                maxTotAmount -= ElementCapabilities.transferAllElements(capFrom, capTo,
                        maxTotAmount, this,
                        false, null);
            }
        }

        markDirty();
        world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
    }
}
