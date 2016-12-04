package com.R3DKn16h7.quantumbase.client.gui;

/**
 * Created by Filippo on 27/11/2016.
 */
public class AnimatedGuiElement extends GuiElement {
    Direction dir;
    int speed;
    int time;

    AnimatedGuiElement(AdvancedGuiContainer container, String texture, int xPosition, int yPosition,
                       int xSize, int ySize,
                       int offsetX, int offsetY, Direction dir, int speed) {
        super(container, texture, xPosition, yPosition, xSize, ySize, offsetX, offsetY);
        this.dir = dir;
        this.speed = speed;
    }

    static public AnimatedGuiElement ARROW(AdvancedGuiContainer container, int xPosition, int yPosition) {
        return new AnimatedGuiElement(container, "textures/gui/container/furnace.png",
                yPosition, yPosition, 24, 18, 176, 14,
                Direction.LEFT, 200);
    }

    @Override
    public void draw() {
        container.mc.getTextureManager().bindTexture(textureLocation);

        int size, offset;
        switch (dir) {
            case LEFT:
                size = xSize * time / speed;
                container.drawTexturedModalRect(xPosition, yPosition,
                        offsetX, offsetY,
                        size, ySize + offsetY);
                break;
            case RIGHT:
                size = xSize * time / speed;
                offset = ySize - size;
                container.drawTexturedModalRect(xPosition + offset, yPosition,
                        offsetX + offset, offsetY,
                        size, ySize);
                break;
            case TOP:
                size = ySize * time / speed;
                container.drawTexturedModalRect(xPosition, yPosition,
                        offsetX, offsetY,
                        xSize, size);
                break;
            case BOTTOM:
                size = ySize * time / speed;
                offset = ySize - size;
                container.drawTexturedModalRect(xPosition, yPosition + offset,
                        offsetX, offsetY + offset,
                        xSize, size);
                break;
        }


        ++time;
        if (time >= speed) time = 0;
    }

    enum Direction {
        LEFT, RIGHT, TOP, BOTTOM
    }
}