package com.R3DKn16h7.quantumbase.client.gui;

import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Created by Filippo on 27/11/2016.
 */
public class TexturedElement extends Widget {

    public ResourceLocation textureLocation;
public int offsetX, offsetY;

    public TexturedElement(AdvancedGuiContainer container, String texture, int xPosition, int yPosition,
                           int xSize, int ySize,
                           int offsetX, int offsetY) {
        super(container, xPosition, yPosition, xSize, ySize);
        this.textureLocation = new ResourceLocation(texture);
        this.xPosition = container.borderLeft + container.backgroundStartx + xPosition;
        this.yPosition = container.borderTop + container.backgroundStarty + yPosition;
        this.xSize = xSize;
        this.ySize = ySize;
        // Offset within texture
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public void draw() {
        container.mc.getTextureManager().bindTexture(textureLocation);
        container.drawTexturedModalRect(xPosition, yPosition,
                offsetX, offsetY,
                xSize, ySize);
    }

    @Override
    public List<String> addHoveringText(List<String> hoveringText) {
        return hoveringText;
    }

    @Override
    public void onHover(int mouseX, int mouseY) {

    }

    @Override
    public void onClicked(int mouseX, int mouseY, int mouseButton) {

        return;
    }


}
