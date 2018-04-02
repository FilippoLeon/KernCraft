package com.R3DKn16h7.kerncraft.crafting;

import com.R3DKn16h7.kerncraft.elements.ElementStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the recipe for the Centrifuge.
 */
public class RockAnalyzerRecipe implements ISmeltingRecipe {
    public NonNullList<ItemStack> inputs = NonNullList.create();
    public List<ElementStack> outputs = new ArrayList<>();
    public FluidStack fluid;
    public int energy;
    public int cost;

    public RockAnalyzerRecipe() {
    }

    static void parseCentrifugeRecipe(Node nNode) {
    }

    @Override
    public int getCost() {
        return cost;
    }
}
