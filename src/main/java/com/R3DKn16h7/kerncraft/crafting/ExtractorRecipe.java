package com.R3DKn16h7.kerncraft.crafting;

import com.R3DKn16h7.kerncraft.elements.ElementStack;
import net.minecraft.item.Item;

/**
 * Represents the recipe for the extractor.
 */
public class ExtractorRecipe implements ISmeltingRecipe {
    public Item item;
    public Item catalyst;

    /**
     * TODO: Might need to switch to ArrayList to favour loops
     */
    public ElementStack[] outs;
    public int energy;

    public ExtractorRecipe(Item item_, Item catalyst_,
                           ElementStack[] outs_, int energy_) {
        item = item_;
        catalyst = catalyst_;
        outs = outs_;
        energy = energy_;
    }

    @Override
    public int getCost() {
        return energy;
    }
}
