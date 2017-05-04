package com.R3DKn16h7.kerncraft.client.gui.widgets;

import com.R3DKn16h7.kerncraft.client.gui.AdvancedGuiContainer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * Created by Filippo on 27/11/2016.
 */
public class TexturedElement extends Widget {

    public ResourceLocation textureLocation;
    public int offsetX, offsetY;
    public Color tint;
    public boolean dynamicSize = false;
    public int xTextureSize, yTextureSize;

    public TexturedElement(AdvancedGuiContainer container, String texture,
                           int xPosition, int yPosition,
                           int xSize, int ySize,
                           int offsetX, int offsetY) {
        super(container, xPosition, yPosition, xSize, ySize);
        this.textureLocation = new ResourceLocation(texture);
        // Offset within texture
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.xTextureSize = xSize;
        this.yTextureSize = ySize;
        this.tint = null;
    }

    public static TexturedElement ENERGY_BAR_BACKGROUND(AdvancedGuiContainer container,
                                                        int xPosition, int yPosition) {
        return new TexturedElement(container, "kerncraft:textures/gui/container/extractor_gui.png",
                xPosition, yPosition, 8, 3 * DEFAULT_SLOT_SIZE_Y,
                16, 16);

    }

    public TexturedElement setDynamicSized() {
        this.dynamicSize = true;
        return this;
    }

    public TexturedElement setTint(Color tint) {
        this.tint = tint;
        return this;
    }

    @Override
    public void draw() {
        if (!(container instanceof GuiScreen)) {
            return;
        }
        GuiScreen guiScreen = (GuiScreen) container;

        if (tint != null) {
            GlStateManager.color(tint.getRed() / 255.f,
                    tint.getGreen() / 255.f,
                    tint.getBlue() / 255.f,
                    1.0F);
        }

        guiScreen.mc.getTextureManager().bindTexture(textureLocation);


        if (dynamicSize) {
            int xPadding = xSize % 2;
            int yPadding = ySize % 2;
            guiScreen.drawTexturedModalRect(xPosition, yPosition,
                    offsetX, offsetY,
                    xSize / 2, ySize / 2);
            guiScreen.drawTexturedModalRect(xPosition + xSize / 2, yPosition,
                    offsetX + (xTextureSize - xSize / 2) - xPadding, offsetY,
                    xSize / 2 + xPadding, ySize / 2);
            guiScreen.drawTexturedModalRect(xPosition, yPosition + ySize / 2,
                    offsetX, offsetY + (yTextureSize - ySize / 2) - yPadding,
                    xSize / 2, ySize / 2 + yPadding);
            guiScreen.drawTexturedModalRect(xPosition + xSize / 2, yPosition + ySize / 2,
                    offsetX + (xTextureSize - xSize / 2) - xPadding, offsetY + (yTextureSize - ySize / 2) - yPadding,
                    xSize / 2 + xPadding, ySize / 2 + yPadding);
        } else {
            guiScreen.drawTexturedModalRect(xPosition, yPosition,
                    offsetX, offsetY,
                    xSize, ySize);
        }

        GlStateManager.resetColor();

    }

}
