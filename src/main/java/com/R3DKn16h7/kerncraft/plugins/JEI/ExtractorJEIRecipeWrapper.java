package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.crafting.ExtractorRecipe;
import com.R3DKn16h7.kerncraft.elements.ElementRegistry;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippo on 23/11/16.
 */
public class ExtractorJEIRecipeWrapper extends BlankRecipeWrapper {
    public ExtractorRecipe recipe;

    ExtractorJEIRecipeWrapper(ExtractorRecipe recipe_) {
        recipe = recipe_;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ArrayList<ItemStack> inputs = new ArrayList<>();

        inputs.add(recipe.item);
        if (recipe.catalyst != null) inputs.add(recipe.catalyst);


        inputs.add(new ItemStack(KernCraftItems.CANISTER));
        ingredients.setInputs(ItemStack.class, inputs);

        ArrayList<ItemStack> outputs = new ArrayList<>();

        for (ElementStack i : recipe.outputs) {
            if (i == null) continue;
            outputs.add(ElementRegistry.getItem(i));
        }
        ingredients.setOutputs(ItemStack.class, outputs);
    }

    @Override
    public void drawInfo(Minecraft minecraft,
                         int recipeWidth, int recipeHeight,
                         int mouseX, int mouseY) {

        // Draw each output element
        int j = 0;
        for (ElementStack i : recipe.outputs) {
            if (i == null || i.probability == 1) continue;
            String prob_string = String.format("%d%%",
                    (int) Math.floor(i.probability * 100));
            int color;
            if (i.probability < 0.9f) color = Color.red.getRGB();
            if (0.6f < i.probability && i.probability <= 0.9f) color = Color.yellow.getRGB();
            else color = Color.green.getRGB();

            minecraft.fontRendererObj.drawStringWithShadow(prob_string,
                    18 * (5 + j), 18 * 3 - 37, color);
            ++j;
        }
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return null;
    }

    @Override
    public boolean handleClick(Minecraft minecraft,
                               int mouseX, int mouseY,
                               int mouseButton) {
        return false;
    }
}
