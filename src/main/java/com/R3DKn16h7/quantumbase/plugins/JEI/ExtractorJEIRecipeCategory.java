package com.R3DKn16h7.quantumbase.plugins.JEI;

import com.R3DKn16h7.quantumbase.tileentities.ExtractorTileEntity;
import com.sun.java.accessibility.util.Translator;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Created by filippo on 23/11/16.
 */
public class ExtractorJEIRecipeCategory implements IRecipeCategory {
    private final IDrawable background;
    private final String localizedName;
    protected static final int inputSlot = 1;
    protected static final int catalystSlot = 2;
    protected static final int canisterSlot = 3;
    protected static final int fuelSlot = 4;
    protected static final int outputSlot = 5;

    protected final ResourceLocation backgroundLocation;
    protected final IDrawableAnimated flame;
    protected final IDrawableAnimated arrow;

    public ExtractorJEIRecipeCategory(IGuiHelper guiHelper) {
        backgroundLocation = new ResourceLocation("minecraft", "textures/gui/container/furnace.png");

        IDrawableStatic flameDrawable = guiHelper.createDrawable(backgroundLocation, 176, 0, 14, 14);
        flame = guiHelper.createAnimatedDrawable(flameDrawable, 300, IDrawableAnimated.StartDirection.TOP, true);

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(backgroundLocation, 176, 14, 24, 17);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

        ResourceLocation location = new ResourceLocation("minecraft", "textures/gui/container/furnace.png");
        background = guiHelper.createDrawable(location, 55, 16, 82, 54);
        localizedName = "Extractor";
    }

    @Override
    public String getUid() {
        return "quantumbase.extractor";
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {

    }

    @Override
    public void drawAnimations(Minecraft minecraft) {
        flame.draw(minecraft, 2, 20);
        arrow.draw(minecraft, 24, 18);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(inputSlot, true, 0, 0);
        guiItemStacks.init(catalystSlot, false, 60, 18);

        IIngredients ingredients = null;
        recipeWrapper.getIngredients(ingredients);
        guiItemStacks.set(inputSlot, ingredients.getInputs(ItemStack.class).get(0));
        if(ingredients.getInputs(ItemStack.class).size() > 1)
            guiItemStacks.set(catalystSlot, ingredients.getInputs(ItemStack.class).get(1));
        //guiItemStacks.set(outputSlot, ingredients.getOutputs(ItemStack.class).get(0));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(inputSlot, true, 0, 0);
        guiItemStacks.init(catalystSlot, false, 60, 18);

        recipeWrapper.getIngredients(ingredients);
        guiItemStacks.set(inputSlot, ingredients.getInputs(ItemStack.class).get(0));
        if(ingredients.getInputs(ItemStack.class).size() > 1)
            guiItemStacks.set(catalystSlot, ingredients.getInputs(ItemStack.class).get(1));
    }
}
