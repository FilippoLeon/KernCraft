package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.tileentities.MachineTileEntity;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_CURRENT_BIT;

/**
 * Created by Filippo on 29-Apr-17.
 */
public abstract class KernCraftAbstractJEIRecipeCategory implements IRecipeCategory {
    private static final int DEFAULT_SLOT_SIZE_Y = 18;
    private static final int DEFAULT_SLOT_SIZE_X = 18;
    protected IDrawable background;
    protected final IDrawable slot;
    protected final String localizedName;
    protected List<BetterDrawable> elements = new ArrayList<>();
    protected IGuiHelper guiHelper;
    protected MachineTileEntity te;

    public class BetterDrawable {
        IDrawable drawable;
        int[] position;
        Color tint;

        public BetterDrawable(IDrawable drawable,
                              int[] position, Color tint) {
            this.drawable = drawable;
            this.position = position;
            this.tint = tint;
        }

        public BetterDrawable(IDrawable drawable,
                              int[] position) {
            this(drawable, position, null);
        }
    }

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
        // Slot background
        ResourceLocation location = new ResourceLocation(
                "kerncraft:textures/gui/container/extractor_gui.png"
        );
        slot = guiHelper.createDrawable(location,
                7 + 1 * DEFAULT_SLOT_SIZE_X, 16 + 2 * DEFAULT_SLOT_SIZE_Y,
                18, 18);
        // Fluid bar background

        // Energy bar
    }

    BetterDrawable createArrowDown(int coordX, int coordY) {
        ResourceLocation location = new ResourceLocation(
                "kerncraft:textures/gui/container/extractor_gui.png"
        );
        return new BetterDrawable(
                guiHelper.createDrawable(location, 182, 28,
                        DEFAULT_SLOT_SIZE_X, DEFAULT_SLOT_SIZE_Y),
                new int[]{coordX, coordY}
        );
    }

    BetterDrawable createArrowDownAnimate(int coordX, int coordY) {
        ResourceLocation location = new ResourceLocation(
                "kerncraft:textures/gui/container/extractor_gui.png"
        );
        IDrawableStatic brewing = guiHelper.createDrawable(location,
                182, 28 + 18,
                DEFAULT_SLOT_SIZE_X, DEFAULT_SLOT_SIZE_Y);
        return new BetterDrawable(
                guiHelper.createAnimatedDrawable(brewing,
                        300, IDrawableAnimated.StartDirection.TOP,
                        false),
                new int[]{coordX, coordY}
        );
    }


    BetterDrawable createFlameAnimate(int coordX, int coordY) {
        ResourceLocation location = new ResourceLocation(
                "minecraft",
                "textures/gui/container/furnace.png"
        );
        IDrawableStatic flame = guiHelper.createDrawable(
                location,
                176, 0, 14, 14
        );
        return new BetterDrawable(
                guiHelper.createAnimatedDrawable(flame,
                        300,
                        IDrawableAnimated.StartDirection.TOP,
                        true),
                new int[]{coordX, coordY}
        );
    }

    BetterDrawable  createBarBackground(int coordX, int coordY) {
        return new BetterDrawable(
                guiHelper.createDrawable(
                        new ResourceLocation("kerncraft:textures/gui/container/extractor_gui.png"),
                        7, 16,
                        8, 3 * DEFAULT_SLOT_SIZE_Y),
                new int[]{coordX, coordY});
    }


    BetterDrawable createBarAnimate(int coordX, int coordY) {
        return createBarAnimate(coordX, coordY, null, 300);
    }

    BetterDrawable createBarAnimate(int coordX, int coordY,
                                             Color color, int speed) {
        ResourceLocation location = new ResourceLocation(
                "kerncraft:textures/gui/container/extractor_gui.png"
        );
        IDrawableStatic energyBar = guiHelper.createDrawable(location,
                176, 0,
                6, 3 * DEFAULT_SLOT_SIZE_Y - 2);
        return new BetterDrawable (
                guiHelper.createAnimatedDrawable(energyBar,
                        speed, IDrawableAnimated.StartDirection.BOTTOM,
                        false),
                new int[]{coordX, coordY}, color
        );
    }

    protected BetterDrawable  createBrewingBackground(int coordX, int coordY) {
        return new BetterDrawable (
                guiHelper.createDrawable(
                        new ResourceLocation(
                                "textures/gui/container/brewing_stand.png"),
                        63, 14,
                        12, 29
                ),
                new int[]{coordX, coordY}
        );
    }

    protected BetterDrawable createBrewingAnimate(int coordX, int coordY) {
        ResourceLocation location = new ResourceLocation(
                "textures/gui/container/brewing_stand.png"
        );
        IDrawableStatic brewingStatic =
                guiHelper.createDrawable(
                        location,
                        185, 0,
                        12, 29
                );
        return new BetterDrawable(
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
        for (BetterDrawable elem : elements) {
            if(elem.tint != null) {
                GlStateManager.pushAttrib();
                GlStateManager.color(elem.tint.getRed(),
                        elem.tint.getGreen(),
                        elem.tint.getBlue());
            }
            elem.drawable.draw(minecraft, elem.position[0], elem.position[1]);
            if(elem.tint != null) {
                GlStateManager.color(255,255,255);
                GlStateManager.popAttrib();
            }
        }
    }
}
