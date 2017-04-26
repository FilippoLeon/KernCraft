package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.tileentities.KernCraftTileEntities;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippo on 23/11/16.
 */
public class ChemicalFurnaceJEIRecipeCategory implements IRecipeCategory {
    public static final String CATEGORY_UID = "kerncraft.chemical_furnace";

    protected final ResourceLocation backgroundLocation;
    //    protected final IDrawableAnimated flame;
//    protected final IDrawableAnimated arrow;
    private final IDrawable background, slot;
    private final String localizedName;
    private IDrawable[] elements;

    public ChemicalFurnaceJEIRecipeCategory(IGuiHelper guiHelper) {
        backgroundLocation = new ResourceLocation("minecraft",
                "textures/gui/demo_background.png");
//        ResourceLocation furnaceLocation = new ResourceLocation("minecraft",
//                "textures/gui/container/furnace.png");
        ResourceLocation location =
                new ResourceLocation(
                        "kerncraft:textures/gui/container/extractor_gui.png");

        background = guiHelper.createDrawable(backgroundLocation,
                7, 16, 163, 54);

        elements = new IDrawable[2];
        elements[0] = guiHelper.createDrawable(location,
                182, 28, 18, 18);
        IDrawableStatic brewing = guiHelper.createDrawable(location,
                182, 28 + 18, 18, 18);
        elements[1] = guiHelper.createAnimatedDrawable(brewing,
                300, IDrawableAnimated.StartDirection.TOP, false);
        slot = guiHelper.createDrawable(location,
                7 + 1 * 18, 16 + 2 * 18, 18, 18);
//        elements[0] = guiHelper.createDrawable(backgroundLocation,
//                7, 16, 18, 18);
//        elements[1] = guiHelper.createDrawable(furnaceLocation,
//                176, 14, 24, 17);
//        IDrawableStatic arrowRight = guiHelper.createDrawable(furnaceLocation,
//                176, 14, 24, 17);
//        elements[2] = guiHelper.createAnimatedDrawable(arrowRight,
//                200, IDrawableAnimated.StartDirection.TOP, false);

        localizedName = I18n.format(
                KernCraftTileEntities.CHEMICAL_FURNACE.getUnlocalizedName() + ".name");
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

    @Nullable
    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        for (IDrawable elem : elements) {
            elem.draw(minecraft, 18 * 3 + 18 + 4 * 18, 18 * 1);
        }
        slot.draw(minecraft, 18 * 3 + 9 + 4 * 18, 0);
        slot.draw(minecraft, 18 * 4 + 9 + 4 * 18, 0);
        slot.draw(minecraft, 18 * 3 + 9 + 4 * 18, 2 * 18);
        slot.draw(minecraft, 18 * 4 + 9 + 4 * 18, 2 * 18);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout,
                          IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 18 * 3 + 9 + 4 * 18, 0);
        guiItemStacks.init(1, true, 18 * 4 + 9 + 4 * 18, 0);
        guiItemStacks.init(2, false, 18 * 3 + 9 + 4 * 18, 2 * 18);
        guiItemStacks.init(3, false, 18 * 4 + 9 + 4 * 18, 2 * 18);

        recipeWrapper.getIngredients(ingredients);

        int i = 0;
        for (List<ItemStack> ing : ingredients.getInputs(ItemStack.class)) {
            guiItemStacks.set(i++, ing.get(0));
        }
        i = 2;
        for (List<ItemStack> ing : ingredients.getOutputs(ItemStack.class)) {
            guiItemStacks.set(i++, ing.get(0));
        }
    }

    @Override
    @MethodsReturnNonnullByDefault
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        ArrayList a = new ArrayList<String>();
        return a;
    }
}
