package com.R3DKn16h7.kerncraft.crafting;

import com.R3DKn16h7.kerncraft.elements.ElementStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

/**
 * Represents the recipe for the extractor.
 */
public class ChemicalFurnaceRecipe implements ISmeltingRecipe {
    // TODO: might want to switch to ArrayList to ease loop
    public ElementStack[] inputs;
    public NonNullList<ItemStack> outputs;
    public int energy;
    public FluidStack fluid;
    public int cost;

    public ChemicalFurnaceRecipe(ElementStack[] inputs,
                                 NonNullList<ItemStack> outputs,
                                 int energy, FluidStack fluid, int cost) {

        this.inputs = inputs;
        this.outputs = NonNullList.create();
        if (outputs != null) {
            if (outputs.size() > 0) this.outputs.add(outputs.get(0));
            if (outputs.size() > 1) this.outputs.add(outputs.get(1));
        }
        this.energy = energy;
        this.fluid = fluid;
        this.cost = cost;
    }

    @Override
    public int getCost() {
        return cost;
    }
}
