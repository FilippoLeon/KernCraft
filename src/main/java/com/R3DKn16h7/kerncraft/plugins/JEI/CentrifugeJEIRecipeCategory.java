package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.tileentities.KernCraftTileEntities;
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
public class CentrifugeJEIRecipeCategory extends KernCraftAbstractJEIRecipeCategory {
    private static final String CATEGORY_UID = "kerncraft.centrifuge";

    public CentrifugeJEIRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper,
                KernCraftTileEntities.CENTRIFUGE_MACHINE,
                KernCraftTileEntities.CENTRIFUGE_MACHINE_TE
        );

        elements.add(createBarBackground(0, 0));
        elements.add(createBarAnimate(1, 1));

        elements.add(createBarBackground(8, 0));
        elements.add(createBarAnimate(9, 1));
    }

    @Override
    public String getUid() {
        return CATEGORY_UID;
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return new ArrayList<>();
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

}
