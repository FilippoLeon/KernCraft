package com.R3DKn16h7.kerncraft.crafting;

import com.R3DKn16h7.kerncraft.elements.ElementStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the recipe for the Electrolyzer.
 */
public class CentrifugeRecipe implements ISmeltingRecipe {
    // TODO: might want to switch to ArrayList to ease loop
    public ItemStack anode;
    public ItemStack cathode;
    public ItemStack input;
    public List<ElementStack> outputs = new ArrayList<>();
    public FluidStack fluid;
    public int energy;
    public int cost;

    public CentrifugeRecipe(ItemStack anode, ItemStack cathode,
                            ItemStack input, List<ElementStack> outputs,
                            FluidStack fluid, int energy, int cost) {
        this.anode = anode;
        this.cathode = cathode;
        this.input = input;
        this.outputs = outputs;

        this.energy = energy;
        this.fluid = fluid;
        this.cost = cost;
    }

    static void parseCentrifugeRecipe(Node nNode) {
    }

    @Override
    public int getCost() {
        return cost;
    }
}
