package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.items.ModItems;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Created by filippo on 23/11/16.
 */
public class ExtractorJEIRecipeCategory implements IRecipeCategory {
    protected static final int inputSlot = 1;
    protected static final int catalystSlot = 2;
    protected static final int canisterSlot = 3;
    protected static final int fuelSlot = 4;
    protected static final int outputSlotStart = 5;
    protected static final int outputSlotSize = 4;
    public final String CATEGORY_UID = "kerncraft.extractor";
    protected final ResourceLocation backgroundLocation;
    protected final IDrawableAnimated flame;
    protected final IDrawableAnimated arrow;
    private final IDrawable background;
    private final String localizedName;

    public ExtractorJEIRecipeCategory(IGuiHelper guiHelper) {
        backgroundLocation = new ResourceLocation("minecraft",
                "textures/gui/container/furnace.png");

        IDrawableStatic flameDrawable = guiHelper.createDrawable(backgroundLocation,
                176, 0, 14, 14);
        flame = guiHelper.createAnimatedDrawable(flameDrawable,
                300, IDrawableAnimated.StartDirection.TOP, true);

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(backgroundLocation,
                176, 14, 24, 17);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable,
                200, IDrawableAnimated.StartDirection.LEFT, false);

        ResourceLocation location =
                new ResourceLocation(
                        "kerncraft:textures/gui/container/extractor_gui.png");
        background = guiHelper.createDrawable(location,
                7, 16, 163, 54);
        localizedName = "Extractor";
    }

    @Override
    public String getUid() {
        return CATEGORY_UID;
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
        flame.draw(minecraft, 2 + 18, 2 + 18 * 1);
        arrow.draw(minecraft, 18 + 18 * 2, 18 * 2);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(inputSlot, true, 0, 0);
        guiItemStacks.init(catalystSlot, false, 60, 18);
        guiItemStacks.init(canisterSlot, false, 1200, 18);


        IIngredients ingredients = null;
        recipeWrapper.getIngredients(ingredients);
        int i = -1;
        guiItemStacks.set(inputSlot, ingredients.getInputs(ItemStack.class).get(++i));
        if(ingredients.getInputs(ItemStack.class).size() > 1)
            guiItemStacks.set(catalystSlot,
                    ingredients.getInputs(ItemStack.class).get(++i));
        guiItemStacks.set(canisterSlot, new ItemStack(ModItems.CANISTER));
        //guiItemStacks.set(outputSlot, ingredients.getOutputs(ItemStack.class).get(0));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout,
                          IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(inputSlot, true, 18, 0);
        guiItemStacks.init(catalystSlot, true, 18 * 3, 0);
        guiItemStacks.init(canisterSlot, true, 18 * 8, 0);
        guiItemStacks.init(fuelSlot, true, 18, 18 * 2);

        recipeWrapper.getIngredients(ingredients);

        int i = -1;
        guiItemStacks.set(inputSlot, ingredients.getInputs(ItemStack.class).get(++i));
        if(((ExtractorJEIRecipeWrapper) recipeWrapper).recipe.catalyst != null)
            guiItemStacks.set(catalystSlot,
                    ingredients.getInputs(ItemStack.class).get(++i));
        guiItemStacks.set(canisterSlot, new ItemStack(ModItems.CANISTER));

        List<ItemStack> outs = ingredients.getOutputs(ItemStack.class);
        int min = Math.min(outs.size(), outputSlotSize);
        for (int j = 0; j < min; ++j) {
            guiItemStacks.init(j + outputSlotStart,
                    false, 0 + 18 * (j + 5), 18 * 2);
            ItemStack item = outs.get(j);
            if (item != null)
                guiItemStacks.set(j + outputSlotStart, item);
        }
    }
}
