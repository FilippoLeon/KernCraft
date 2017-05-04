package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.tileentities.KernCraftTileEntities;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
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

        elements.add(new Tuple<>(
                guiHelper.createDrawable(
                        new ResourceLocation("kerncraft:textures/gui/container/extractor_gui.png"),
                        182, 64,
                        18, 3 * 18
                ),
                new int[]{18 * 4, 0}
        ));
        IDrawableStatic temp = guiHelper.createDrawable(
                new ResourceLocation("kerncraft:textures/gui/container/extractor_gui.png"),
                182 + 18 + 3, 64,
                18 - 6, 3 * 18
        );
        elements.add(new Tuple<>(
                guiHelper.createAnimatedDrawable(
                        temp, 300,
                        IDrawableAnimated.StartDirection.LEFT, false
                ),
                new int[]{18 * 4 + 3, 0}
        ));
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
