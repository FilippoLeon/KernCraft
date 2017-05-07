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

        if (recipe.outputs != null) {
            ingredients.setOutputs(ItemStack.class, recipe.outputs);
        }
    }

    @Override
    public void drawInfo(Minecraft minecraft,
                         int recipeWidth, int recipeHeight,
                         int mouseX, int mouseY) {
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {

        List<String> tooltip = new ArrayList<>();

        if (recipe.energy != 0) {
            if (isInBox(mouseX, mouseY, 0, 0,
                    10, 18 * 3)) {
                String color = recipe.energy < 0 ?
                        TextFormatting.RED.toString() : TextFormatting.GREEN.toString();
                tooltip.add(String.format("%s%s%s RF", color,
                        recipe.energy > 0 ? "+" : "", recipe.energy)
                );
            }
        }

        // Draw each output element
        if (isInBox(mouseX, mouseY, 10, 10, 10, 10)) {
            tooltip.add(String.format("Time: %s t", recipe.cost * 5));
        }
        if (recipe.fluid != null) {
            if (isInBox(mouseX, mouseY, 8, 0, 8, 18 * 3)) {
                if (recipe.fluid.amount > 0) {
                    tooltip.add(String.format("+%d mb of %s",
                            recipe.fluid.amount,
                            recipe.fluid.getLocalizedName())
                    );
                } else {
                    tooltip.add(String.format("-%d mb of %s",
                            -recipe.fluid.amount,
                            recipe.fluid.getLocalizedName())
                    );
                }
            }
        }


        return tooltip;
    }

    private boolean isInBox(int mouseX, int mouseY,
                            int startX, int startY,
                            int sizeX, int sizeY) {
        return mouseX > startX && mouseX < startX + sizeX
                && mouseY > startY && mouseY < startY + sizeY;
    }

    @Override
    public boolean handleClick(Minecraft minecraft,
                               int mouseX, int mouseY,
                               int mouseButton) {
        return false;
    }
}
