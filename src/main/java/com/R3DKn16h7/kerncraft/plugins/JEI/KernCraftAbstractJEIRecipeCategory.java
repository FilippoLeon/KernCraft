package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.tileentities.MachineTileEntity;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filippo on 29-Apr-17.
 */
public abstract class KernCraftAbstractJEIRecipeCategory implements IRecipeCategory {
    private static final int DEFAULT_SLOT_SIZE_Y = 18;
    private static final int DEFAULT_SLOT_SIZE_X = 18;
    protected final IDrawable background;
    protected final IDrawable slot;
    protected final String localizedName;
    protected List<Tuple<IDrawable, int[]>> elements = new ArrayList<>();
    protected IGuiHelper guiHelper;
    protected MachineTileEntity te;

    public KernCraftAbstractJEIRecipeCategory(IGuiHelper guiHelper,
                                              Block block, MachineTileEntity te) {
        super();
        this.guiHelper = guiHelper;
        this.te = te;
        localizedName = I18n.format(block.getUnlocalizedName() + ".name");

        ResourceLocation backgroundLocation = new ResourceLocation(
                "minecraft",
                "textures/gui/demo_background.png"
        );
        background = guiHelper.createDrawable(backgroundLocation,
                7, 16, 163, 54);

        ResourceLocation location = new ResourceLocation(
                "kerncraft:textures/gui/container/extractor_gui.png"
        );
        slot = guiHelper.createDrawable(location,
                7 + 1 * DEFAULT_SLOT_SIZE_X, 16 + 2 * DEFAULT_SLOT_SIZE_Y,
                18, 18);
    }

    Tuple<IDrawable, int[]> createArrowDown(int coordX, int coordY) {
        ResourceLocation location = new ResourceLocation(
                "kerncraft:textures/gui/container/extractor_gui.png"
        );
        return new Tuple<>(
                guiHelper.createDrawable(location, 182, 28,
                        DEFAULT_SLOT_SIZE_X, DEFAULT_SLOT_SIZE_Y),
                new int[]{coordX, coordY}
        );
    }

    Tuple<IDrawable, int[]> createArrowDownAnimate(int coordX, int coordY) {
        ResourceLocation location = new ResourceLocation(
                "kerncraft:textures/gui/container/extractor_gui.png"
        );
        IDrawableStatic brewing = guiHelper.createDrawable(location,
                176, 0,
                6, 3 * DEFAULT_SLOT_SIZE_Y - 2);
        return new Tuple<>(
                guiHelper.createAnimatedDrawable(brewing,
                        300, IDrawableAnimated.StartDirection.TOP,
                        false),
                new int[]{coordX, coordY}
        );
    }


    Tuple<IDrawable, int[]> createBarBackground(int coordX, int coordY) {
        return new Tuple<>(
                guiHelper.createDrawable(
                        new ResourceLocation("kerncraft:textures/gui/container/extractor_gui.png"),
                        16, 16,
                        8, 3 * DEFAULT_SLOT_SIZE_Y),
                new int[]{coordX, coordY});
    }


    Tuple<IDrawable, int[]> createBarAnimate(int coordX, int coordY) {
        ResourceLocation location = new ResourceLocation(
                "kerncraft:textures/gui/container/extractor_gui.png"
        );
        IDrawableStatic energyBar = guiHelper.createDrawable(location,
                176, 0,
                6, 3 * DEFAULT_SLOT_SIZE_Y - 2);
        return new Tuple<>(
                guiHelper.createAnimatedDrawable(energyBar,
                        300, IDrawableAnimated.StartDirection.BOTTOM,
                        false),
                new int[]{coordX, coordY}
        );
    }


    protected Tuple<IDrawable, int[]> createBrewingBackground(int coordX, int coordY) {
        return new Tuple<>(
                guiHelper.createDrawable(
                        new ResourceLocation(
                                "textures/gui/container/brewing_stand.png"),
                        63, 14,
                        12, 29
                ),
                new int[]{coordX, coordY}
        );
    }


    protected Tuple<IDrawable, int[]> createBrewingAnimate(int coordX, int coordY) {
        ResourceLocation location = new ResourceLocation(
                "textures/gui/container/brewing_stand.png"
        );
        IDrawableStatic brewingStatic =
                guiHelper.createDrawable(
                        location,
                        185, 0,
                        12, 29
                );
        return new Tuple<>(
                guiHelper.createAnimatedDrawable(brewingStatic,
                        300, IDrawableAnimated.StartDirection.BOTTOM,
                        false),
                new int[]{coordX, coordY}
        );
    }


    @Override
    public String getTitle() {
        return localizedName;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return null;
    }


    @Override
    public IDrawable getBackground() {
        return background;
    }

    protected void drawBackgrounds(Minecraft minecraft) {
        int[][] slotsCoord = te.getInputCoords();
        for (int[] coordinate : slotsCoord) {
            slot.draw(minecraft, 18 * coordinate[0], 18 * coordinate[1]);
        }
        slotsCoord = te.getOutputCoords();
        for (int[] coordinate : slotsCoord) {
            slot.draw(minecraft, 18 * coordinate[0], 18 * coordinate[1]);
        }
    }

    protected void initSlots(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper,
                             IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        int i = 0;
        int[][] slotsCoord = te.getInputCoords();
        for (int[] coordinate : slotsCoord) {
            guiItemStacks.init(i++, true,
                    18 * coordinate[0], 18 * coordinate[1]
            );
        }
        slotsCoord = te.getOutputCoords();
        for (int[] coordinate : slotsCoord) {
            guiItemStacks.init(i++, false,
                    18 * coordinate[0], 18 * coordinate[1]
            );
        }

        recipeWrapper.getIngredients(ingredients);

        i = 0;
        for (List<ItemStack> ing : ingredients.getInputs(ItemStack.class)) {
            if (ing == null || ing.size() < 1) {
                ++i;
                continue;
            }
            guiItemStacks.set(i, ing.get(0));
            ++i;
        }
        i = te.getInputCoords().length;
        for (List<ItemStack> ing : ingredients.getOutputs(ItemStack.class)) {
            if (ing == null || ing.size() < 1) {
                ++i;
                continue;
            }
            guiItemStacks.set(i, ing.get(0));
            ++i;
        }

    }

    protected void drawWidgets(Minecraft minecraft) {
        for (Tuple<IDrawable, int[]> elem : elements) {
            elem.getFirst().draw(minecraft, elem.getSecond()[0], elem.getSecond()[1]);
        }
    }
}
