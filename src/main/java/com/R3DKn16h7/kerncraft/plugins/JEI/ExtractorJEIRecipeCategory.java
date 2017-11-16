package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import com.R3DKn16h7.kerncraft.tileentities.KernCraftTileEntities;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippo on 23/11/16.
 */
public class ExtractorJEIRecipeCategory
    extends KernCraftAbstractJEIRecipeCategory {
    public static final String CATEGORY_UID = "kerncraft.extractor";

    protected static final int inputSlot = 1;
    protected static final int catalystSlot = 2;
    protected static final int canisterSlot = 3;
    protected static final int fuelSlot = 4;
    protected static final int outputSlotStart = 5;
    protected static final int outputSlotSize = 4;

    @Override
    public String getModName() {
        return KernCraft.MODID;
    }

    public ExtractorJEIRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper,
                KernCraftTileEntities.EXTRACTOR,
                KernCraftTileEntities.EXTRACTOR_TE
        );

        elements.add(createBarBackground(0, 0));
        elements.add(createBarAnimate(1, 1));

        elements.add(createBarBackground(8, 0));
        elements.add(createBarAnimate(9, 1, Color.blue, 200));


        elements.add(createFlameAnimate(2 + 18, 2 + 18 * 1));

//        elements.add(createBrewingBackground(18 + 3 + 18 * 2, 18 + 12));
        elements.add(createBrewingAnimate(3 + 18 * 3, 12 + 18));

        ResourceLocation location =
                new ResourceLocation(
                        "kerncraft:textures/gui/container/extractor_gui.png");
        background = guiHelper.createDrawable(location,7, 16, 163, 57);
    }

    @Override
    public String getUid() {
        return CATEGORY_UID;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
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
        return new ArrayList<>();
    }
}
