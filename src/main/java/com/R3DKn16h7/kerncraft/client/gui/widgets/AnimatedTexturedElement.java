package com.R3DKn16h7.kerncraft.client.gui.widgets;

import com.R3DKn16h7.kerncraft.client.gui.AdvancedGuiContainer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Created by Filippo on 27/11/2016.
 */
public class AnimatedTexturedElement extends TexturedElement {
    Direction dir;
    private int time;
    private int speed;
    private float perc;
    private boolean auto_animated = true;

    public AnimatedTexturedElement(AdvancedGuiContainer container, String texture,
                                   int xPosition, int yPosition,
                                   int xSize, int ySize,
                                   int offsetX, int offsetY, Direction dir, int speed) {
        super(container, texture, xPosition, yPosition, xSize, ySize, offsetX, offsetY);
        this.dir = dir;
        this.speed = speed;
    }


    public static AnimatedTexturedElement ENERGY_BAR(AdvancedGuiContainer container,
                                                     int xPosition, int yPosition) {
        return new AnimatedTexturedElement(container,
                "kerncraft:textures/gui/container/extractor_gui.png",
                xPosition, yPosition,
                6, 3 * DEFAULT_SLOT_SIZE_Y - 2,
                176, 0,
                AnimatedTexturedElement.Direction.BOTTOM, 300);
    }


    static public AnimatedTexturedElement ARROW(AdvancedGuiContainer container,
                                                int xPosition, int yPosition) {
        return new AnimatedTexturedElement(container, "textures/gui/container/furnace.png",
                xPosition, yPosition, 24, 18, 176, 14,
                Direction.LEFT, 200);
    }

    static public AnimatedTexturedElement FLAME(AdvancedGuiContainer container,
                                                int xPosition, int yPosition) {
        return new AnimatedTexturedElement(container, "textures/gui/container/furnace.png",
                xPosition, yPosition, 14, 14, 176, 0,
                Direction.BOTTOM, 200);
    }

    static public AnimatedTexturedElement BREWING(AdvancedGuiContainer container,
                                                  int xPosition, int yPosition) {
        return new AnimatedTexturedElement(container, "textures/gui/container/brewing_stand.png",
                xPosition, yPosition, 12, 29, 185, 0,
                Direction.BOTTOM, 200);
    }

    static public AnimatedTexturedElement ARROW_DOWN(AdvancedGuiContainer container,
                                                     int xPosition, int yPosition) {
        return new AnimatedTexturedElement(container, "kerncraft:textures/gui/container/extractor_gui.png",
                xPosition, yPosition, 18, 18, 182, 28 + 18,
                Direction.TOP, 200);
    }

    public void setAutoAnimated(boolean yesorno, int speed) {
        this.auto_animated = yesorno;
        this.speed = speed;
    }

    public void setPercentage(float perc) {
        this.perc = perc;
    }

    @Override
    public void draw() {
        if (!(container instanceof GuiScreen)) {
            return;
        }
        GuiScreen C = (GuiScreen) container;

        if (tint != null) {
            GlStateManager.color(tint.getRed() / 255.f,
                    tint.getGreen() / 255.f,
                    tint.getBlue() / 255.f,
                    1.0F);
        } else {
            // TODO: this is just an hack, something must be awfully wrong somewhere
            GlStateManager.color(255.f,
                    255.f,
                    255.f,
                    1.0F);
        }

        C.mc.getTextureManager().bindTexture(textureLocation);

        if (auto_animated) {
            perc = (float) time / speed;
        }


        int size, offset;
        switch (dir) {
            case LEFT:
                size = Math.round(xSize * perc);
                C.drawTexturedModalRect(xPosition, yPosition,
                        offsetX, offsetY,
                        size, ySize + offsetY);
                break;
            case RIGHT:
                size = Math.round(xSize * perc);
                offset = ySize - size;
                C.drawTexturedModalRect(xPosition + offset, yPosition,
                        offsetX + offset, offsetY,
                        size, ySize);
                break;
            case TOP:
                size = Math.round(ySize * perc);
                C.drawTexturedModalRect(xPosition, yPosition,
                        offsetX, offsetY,
                        xSize, size);
                break;
            case BOTTOM:
                size = Math.round(ySize * perc);
                offset = ySize - size;
                C.drawTexturedModalRect(xPosition, yPosition + offset,
                        offsetX, offsetY + offset,
                        xSize, size);
                break;
        }

        GlStateManager.resetColor();

        if (auto_animated) {
            ++time;
            if (time >= speed) time = 0;
        }
    }

    public enum Direction {
        LEFT, RIGHT, TOP, BOTTOM
    }
}
