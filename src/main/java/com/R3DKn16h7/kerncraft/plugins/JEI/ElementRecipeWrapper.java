package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.crafting.ElementRecipe;
import com.R3DKn16h7.kerncraft.elements.ElementRegistry;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Filippo on 01-May-17.
 */
public class ElementRecipeWrapper
        extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper {
    private final IJeiHelpers jeiHelpers;
    private final ElementRecipe recipe;

    public ElementRecipeWrapper(IJeiHelpers jeiHelpers, ElementRecipe recipe) {
        this.jeiHelpers = jeiHelpers;
        this.recipe = recipe;
    }

    public List<List<ItemStack>> expandRecipeItemStackInputs(IStackHelper stackHelper,
                                                             List inputs, boolean expandSubtypes) {
        if (inputs == null) {
            return Collections.emptyList();
        }

        List<List<ItemStack>> expandedInputs = new ArrayList<>();
        for (Object input : inputs) {
            if (input instanceof ElementStack) {
                List<ItemStack> expandedInput = new ArrayList<>();
                expandedInput.add(ElementRegistry.getItem((ElementStack) input));

                expandedInputs.add(expandedInput);
            } else {
                List<ItemStack> expandedInput = stackHelper.toItemStackList(input);
                expandedInputs.add(expandedInput);
            }
        }
        return expandedInputs;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        IStackHelper stackHelper = jeiHelpers.getStackHelper();
        ItemStack recipeOutput = recipe.getRecipeOutput();

        try {
            List<List<ItemStack>> inputs = expandRecipeItemStackInputs(
                    stackHelper, Arrays.asList(recipe.getInput()), true
            );
            ingredients.setInputLists(ItemStack.class, inputs);
            ingredients.setOutput(ItemStack.class, recipeOutput);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getWidth() {
        return recipe.width;
    }

    @Override
    public int getHeight() {
        return recipe.height;
    }

}