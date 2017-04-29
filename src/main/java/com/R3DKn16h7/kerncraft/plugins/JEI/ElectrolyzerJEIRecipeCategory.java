package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.tileentities.KernCraftTileEntities;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;

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

        elements.add(createArrowDown(18 * 5, 18 * 1));
        elements.add(createArrowDownAnimate(18 * 5, 18 * 1));
    }

    @Override
    public void drawExtras(Minecraft minecraft) {

        drawBackgrounds(minecraft);
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
