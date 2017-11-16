package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.tileentities.KernCraftTileEntities;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippo on 23/11/16.
 */
public class ElectrolyzerJEIRecipeCategory extends KernCraftAbstractJEIRecipeCategory {
    private static final String CATEGORY_UID = "kerncraft.electrolyzer";

    public ElectrolyzerJEIRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper,
                KernCraftTileEntities.ELECTROLYZER_MACHINE,
                KernCraftTileEntities.ELECTROLYZER_MACHINE_TE
        );

        elements.add(createBarBackground(0, 0));
        elements.add(createBarAnimate(1, 1));

        elements.add(createBarBackground(8, 0));
        elements.add(createBarAnimate(9, 1));

        elements.add(createBrewingBackground(18 * 5 + 4, 18 * 1));
        elements.add(createBrewingAnimate(18 * 5 + 4, 18 * 1));

        elements.add(new BetterDrawable(
                guiHelper.createDrawable(
                        new ResourceLocation("kerncraft:textures/gui/container/extractor_gui.png"),
                        218, 64,
                        18, 3 * 18
                ),
                new int[]{18 * 2, 0}
        ));
        elements.add(new BetterDrawable(
                guiHelper.createDrawable(
                        new ResourceLocation("kerncraft:textures/gui/container/extractor_gui.png"),
                        218, 64,
                        18, 3 * 18
                ),
                new int[]{18 * 8, 0}
        ));
    }

    @Override
    public String getModName() {
        return "kernkraft";
    }

    @Override
    public void drawExtras(Minecraft minecraft) {

        drawBackgrounds(minecraft);

        drawWidgets(minecraft);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout,
                          IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        initSlots(recipeLayout, recipeWrapper, ingredients);
    }

    @Override
    @MethodsReturnNonnullByDefault
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        ArrayList a = new ArrayList<String>();
        return a;
    }

    @Override
    public String getUid() {
        return ElectrolyzerJEIRecipeCategory.CATEGORY_UID;
    }
}
