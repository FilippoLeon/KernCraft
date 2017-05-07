package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.tileentities.KernCraftTileEntities;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippo on 23/11/16.
 */
public class ChemicalFurnaceJEIRecipeCategory
        extends KernCraftAbstractJEIRecipeCategory {
    public static final String CATEGORY_UID = "kerncraft.chemical_furnace";

    public ChemicalFurnaceJEIRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper,
                KernCraftTileEntities.CHEMICAL_FURNACE,
                KernCraftTileEntities.CHEMICAL_FURNACE_TE
        );

        // Energy
        elements.add(createBarBackground(0, 0));
        elements.add(createBarAnimate(1, 1));
        // Fluid
        elements.add(createBarBackground(8, 0));
        elements.add(createBarAnimate(9, 1, Color.blue, 200));

        elements.add(createArrowDown(18 * 9 / 2, 18));
        elements.add(createArrowDownAnimate(18 * 9 / 2, 18));
    }

    /**
     * @param mouseX
     * @param mouseY
     * @return
     */
    @Override
    @MethodsReturnNonnullByDefault
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return new ArrayList<>();
    }

    @Override
    public String getUid() {
        return CATEGORY_UID;
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
