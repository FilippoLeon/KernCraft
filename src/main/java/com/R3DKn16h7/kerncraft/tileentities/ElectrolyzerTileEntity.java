package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.capabilities.element.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.element.IElementContainer;
import com.R3DKn16h7.kerncraft.client.gui.MachineGuiContainer;
import com.R3DKn16h7.kerncraft.crafting.ElectrolyzerRecipe;
import com.R3DKn16h7.kerncraft.crafting.KernCraftRecipes;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

import java.util.List;
import java.util.Random;

public class ElectrolyzerTileEntity
        extends SmeltingTileEntity<ElectrolyzerRecipe> {

    // Slot IDs
    public static final int[][] inputCoords = {{2, 0}, {5, 0}, {8, 0}};
    public static final int[][] outputCoords = {{2, 2}, {8, 2}};
    private static final int ANODE_SLOT = 0;
    private static final int CATHODE_SLOT = 1;
    private static final int INPUT_SLOT = 2;

    public ElectrolyzerTileEntity() {
        super(1);
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
    public List<ElectrolyzerRecipe> getRecipes() {
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
    public boolean canSmelt(ElectrolyzerRecipe recipe) {
        if ((
                !input.getStackInSlot(ANODE_SLOT).isItemEqual(recipe.anode)
                        && !input.getStackInSlot(ANODE_SLOT).isItemEqual(recipe.cathode)
        ) || (
                !input.getStackInSlot(CATHODE_SLOT).isItemEqual(recipe.anode)
                        && !input.getStackInSlot(CATHODE_SLOT).isItemEqual(recipe.cathode)
        )) {
            return false;
        }

        outerloop:
        for (ItemStack recipeInput : recipe.input) {
            for (int i = INPUT_SLOT; i < input.getSlots(); ++i) {
                if (input.getStackInSlot(i).isItemEqual(recipeInput)) {
                    continue outerloop;
                }
            }
            return false;
        }

        if (recipe.fluid != null && getFluid(0) != null && getFluid(0).isFluidEqual(recipe.fluid)) {
            if (recipe.fluid.amount > tank.get(0).drain(recipe.fluid, false).amount) {
                return false;
            }
        }

        // TODO
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
        // TODO
        for (ItemStack item : currentlySmelting.input) {
            int to_be_removed = item.getCount();
            for (int i = INPUT_SLOT; i < input.getSlots(); ++i) {
                if (item.isItemEqual(input.getStackInSlot(i))) {
                    to_be_removed -= input.getStackInSlot(i).splitStack(to_be_removed).getCount();
                }
                if (to_be_removed <= 0) break;
            }
        }

        if (currentlySmelting.fluid != null) {
            tank.get(0).drain(currentlySmelting.fluid, true);
        }
        Random rand = new Random();

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
