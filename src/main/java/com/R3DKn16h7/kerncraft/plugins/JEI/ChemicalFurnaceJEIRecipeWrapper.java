package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.crafting.ChemicalFurnaceRecipe;
import com.R3DKn16h7.kerncraft.elements.ElementRegistry;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippo on 23/11/16.
 */
public class ChemicalFurnaceJEIRecipeWrapper extends BlankRecipeWrapper {

    public ChemicalFurnaceRecipe recipe;

    ChemicalFurnaceJEIRecipeWrapper(ChemicalFurnaceRecipe recipe_) {
        recipe = recipe_;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ArrayList<ItemStack> inputs = new ArrayList<>();
        int j = 0;
        for (ElementStack i : recipe.inputs) {
            if (recipe.inputs[j++] != null) {
                inputs.add(ElementRegistry.getItem(i));
            }
        }
        ingredients.setInputs(ItemStack.class, inputs);

        ArrayList<ItemStack> outputs = new ArrayList<>();
        j = 0;
        for (ElementStack i : recipe.outputs) {
            if (recipe.outputs[j++] != null) {
                outputs.add(ElementRegistry.getItem(i));
            }
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
            if (i.probability == 1) continue;
            String prob_string = String.format("%d%%",
                    (int) Math.floor(i.probability * 100));
            int color;
            if (i.probability < 0.9f) color = Color.red.getRGB();
            if (0.6f < i.probability && i.probability <= 0.9f) color = Color.yellow.getRGB();
            else color = Color.green.getRGB();

            minecraft.fontRendererObj.drawStringWithShadow(prob_string,
                    18 * (3 + j) + 9 + 1 + 4 * 18, 18 * 3 - 27, color);
            ++j;
        }
        minecraft.fontRendererObj.drawStringWithShadow(
                String.format("Time: %s t", recipe.cost * 5),
                0, 18 * 1 + 9, Color.white.getRGB()
        );
        if (recipe.energy != 0) {
            String color = recipe.energy > 0 ?
                    TextFormatting.RED.toString() : TextFormatting.GREEN.toString();
            minecraft.fontRendererObj.drawStringWithShadow(
                    String.format(String.format("%s%s%s RF", color,
                            recipe.energy > 0 ? "-" : "+", recipe.energy)
                    ),
                    0, 18 * 0 + 9, Color.white.getRGB()
            );
        }
        if (recipe.fluid != null) {
            if (recipe.fluid.amount > 0) {
                minecraft.fontRendererObj.drawStringWithShadow(
                        String.format(String.format("+%d mb of %s",
                                recipe.fluid.amount, recipe.fluid.getLocalizedName())
                        ),
                        0, 18 * 2 + 9, Color.white.getRGB()
                );
            } else {
                minecraft.fontRendererObj.drawStringWithShadow(
                        String.format(String.format("-%d mb of %s",
                                -recipe.fluid.amount, recipe.fluid.getLocalizedName())
                        ),
                        0, 18 * 2 + 9, Color.white.getRGB()
                );
            }
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
