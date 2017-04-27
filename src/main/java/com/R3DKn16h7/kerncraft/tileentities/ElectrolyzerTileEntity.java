package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.capabilities.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.IElementContainer;
import com.R3DKn16h7.kerncraft.client.gui.MachineGuiContainer;
import com.R3DKn16h7.kerncraft.crafting.ElectrolyzerRecipe;
import com.R3DKn16h7.kerncraft.crafting.ISmeltingRecipe;
import com.R3DKn16h7.kerncraft.crafting.KernCraftRecipes;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

import java.util.List;

public class ElectrolyzerTileEntity extends SmeltingTileEntity {

    // Slot IDs
    public static final int[][] inputCoords = {{2, 0}, {5, 0}, {8, 0}};
    public static final int[][] outputCoords = {{4, 2}, {6, 2}};
    private static final int ANODE_SLOT = 0;
    private static final int CATHODE_SLOT = 2;
    private static final int INPUT_SLOT = 1;

    public ElectrolyzerTileEntity() {

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
        return KernCraftRecipes.ELECTROLYZER_RECIPES;
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
        ElectrolyzerRecipe recipe = ((ElectrolyzerRecipe) rec);
        if (recipe == null) return false;

        if ((
                !input.getStackInSlot(ANODE_SLOT).isItemEqual(recipe.anode)
                        && !input.getStackInSlot(ANODE_SLOT).isItemEqual(recipe.cathode)
        ) || (
                !input.getStackInSlot(CATHODE_SLOT).isItemEqual(recipe.anode)
                        && !input.getStackInSlot(CATHODE_SLOT).isItemEqual(recipe.cathode)
        )) {
            return false;
        }

        if (!recipe.input.isEmpty() && !input.getStackInSlot(INPUT_SLOT).isItemEqual(recipe.input)) {
            return false;
        }

        if (recipe.fluid != null && getFluid() != null && getFluid().isFluidEqual(recipe.fluid)) {
            if (recipe.fluid.amount > tank.drain(recipe.fluid, false).amount) {
                return false;
            }
        }

        // TODO
        return true;
    }

    @Override
    public boolean tryProgress() {
        if (storage.extractEnergy(currentlySmelting.getCost(), true) != currentlySmelting.getCost()) {
            return false;
        }

        storage.extractEnergy(currentlySmelting.getCost(), false);
        return true;
    }

    @Override
    public void doneSmelting() {
        ElectrolyzerRecipe recipe = ((ElectrolyzerRecipe) currentlySmelting);
        if (recipe == null) return;

        // TODO
        if (!recipe.input.isEmpty()) {
            input.getStackInSlot(INPUT_SLOT).splitStack(recipe.input.getCount());
        }

        if (recipe.fluid != null) {
            tank.drain(recipe.fluid, true);
        }

        for (ElementStack created : recipe.outputs) {
            int leftover = created.quantity;
            for (int i = 0; i < getOutputCoords().length; ++i) {
                ItemStack out = getOutput().getStackInSlot(i);
                if (!ElementCapabilities.hasCapability(out)) continue;
                IElementContainer cap = ElementCapabilities.getCapability(out);
                leftover -= cap.addAmountOf(created.id, leftover, false);
            }
        }

        markDirty();
        world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
    }
}
