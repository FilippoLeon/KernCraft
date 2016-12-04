package com.R3DKn16h7.quantumbase.client.gui;

import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Created by Filippo on 27/11/2016.
 */
public class GuiElement implements IWidget {

    public ResourceLocation textureLocation;

    public int xPosition, yPosition, xSize, ySize, offsetX, offsetY;

    protected AdvancedGuiContainer container;

    public GuiElement(AdvancedGuiContainer container, String texture, int xPosition, int yPosition,
                      int xSize, int ySize,
                      int offsetX, int offsetY) {
        this.container = container;
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
    public void click(int mouseButton) {
        return;
    }

    @Override
    public boolean isMouseInArea(int mouseX, int mouseY) {
        return (mouseX >= xPosition && mouseX <= xPosition + xSize &&
                mouseY >= yPosition && mouseY <= yPosition + ySize);
    }

}
