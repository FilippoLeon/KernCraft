package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.crafting.ExtractorRecipe;
import com.R3DKn16h7.kerncraft.elements.ElementRegistry;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import com.R3DKn16h7.kerncraft.tileentities.KernCraftTileEntities;
import com.R3DKn16h7.kerncraft.tileentities.machines.ExtractorTileEntity;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippo on 23/11/16.
 */
public class ExtractorJEIRecipeWrapper
        extends KernCraftAbstractJEIRecipeWrapper {
    public ExtractorRecipe recipe;

    ExtractorJEIRecipeWrapper(ExtractorRecipe recipe) {
        this.recipe = recipe;
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

        // Draw each output element probability
        int j = 0;
        for (ElementStack i : recipe.outputs) {
            if (i == null || i.probability == 1) continue;
            String prob_string = String.format("%d%%",
                    (int) Math.floor(i.probability * 100));
            int color;
            if (i.probability >= 0.9f) color = Color.green.getRGB();
            else if (0.5f < i.probability && i.probability < 0.9f) color = Color.yellow.getRGB();
            else color = Color.red.getRGB();

            minecraft.fontRenderer.drawStringWithShadow(prob_string,
                    18 * (5 + j), 18 * 3 - 37, color);
            ++j;
        }
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {

        List<String> tooltip = new ArrayList<>();

        if (recipe.energy != 0) {
            if (isInBox(mouseX, mouseY, 0, 0,
                    10, 18 * 3)) {
                String color = TextFormatting.RED.toString();
                tooltip.add(String.format("%s%d RF for %d fuel", color,
                        ExtractorTileEntity.CONSUMED_ENERGY_PER_FUEL_REFILL,
                        ExtractorTileEntity.GENERATED_FUEL_PER_ENERGY_DRAIN
                    )
                );
            }
        }

        // Draw each output element
        if (isInBox(mouseX, mouseY, 18 * 3, 18*2, 18, 18*2)) {
            tooltip.add(String.format("Time: %s t",
                    recipe.getCost() * KernCraftTileEntities.EXTRACTOR_TE.TICS_PER_OPERATION)
            );
        }
        if(isInBox(mouseX, mouseY, 18 * 1, 18*1, 18, 18)) {

            tooltip.add(String.format(
                    "Consumed %d fuel per operation.",
                    ExtractorTileEntity.CONSUMED_FUEL_PER_TIC
                )
            );
        }
        if (recipe.fluids != null) {
            if (isInBox(mouseX, mouseY, 8, 0, 8, 18 * 3)) {
                for(FluidStack fluid: recipe.fluids) {
                    if (fluid.amount > 0) {
                        tooltip.add(String.format("+%d mb of %s",
                                fluid.amount,
                                fluid.getLocalizedName())
                        );
                    } else {
                        tooltip.add(String.format("%d mb of %s",
                                fluid.amount,
                                fluid.getLocalizedName())
                        );
                    }
                }
            }
        }


        return tooltip;
    }

    @Override
    public boolean handleClick(Minecraft minecraft,
                               int mouseX, int mouseY,
                               int mouseButton) {
        return false;
    }
}
