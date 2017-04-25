package com.R3DKn16h7.kerncraft.crafting;

import com.R3DKn16h7.kerncraft.elements.ElementStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Represents the recipe for the extractor.
 */
public class ChemicalFurnaceRecipe implements ISmeltingRecipe {
    // TODO: might want to switch to ArrayList to ease loop
    public ElementStack[] inputs;
    public ElementStack[] outputs;
    public int energy;
    public FluidStack fluid;
    public int cost;

    public ChemicalFurnaceRecipe(ElementStack[] inputs, ElementStack[] outputs,
                                 int energy, FluidStack fluid, int cost) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.energy = energy;
        this.fluid = fluid;
        this.cost = cost;
    }

    @Override
    public int getCost() {
        return cost;
    }
}
