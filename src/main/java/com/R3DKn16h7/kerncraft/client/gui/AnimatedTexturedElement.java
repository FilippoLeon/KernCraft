package com.R3DKn16h7.kerncraft.client.gui;

/**
 * Created by Filippo on 27/11/2016.
 */
public class AnimatedTexturedElement extends TexturedElement {
    Direction dir;
    int speed;
    int time;

    AnimatedTexturedElement(AdvancedGuiContainer container, String texture, int xPosition, int yPosition,
                            int xSize, int ySize,
                            int offsetX, int offsetY, Direction dir, int speed) {
        super(container, texture, xPosition, yPosition, xSize, ySize, offsetX, offsetY);
        this.dir = dir;
        this.speed = speed;
    }

    static public AnimatedTexturedElement ARROW(AdvancedGuiContainer container, int xPosition, int yPosition) {
        return new AnimatedTexturedElement(container, "textures/gui/container/furnace.png",
                yPosition, yPosition, 24, 18, 176, 14,
                Direction.LEFT, 200);
    }

    static public AnimatedTexturedElement FLAME(AdvancedGuiContainer container, int xPosition, int yPosition) {
        return new AnimatedTexturedElement(container, "textures/gui/container/furnace.png",
                yPosition, yPosition, 14, 14, 176, 0,
                Direction.LEFT, 200);
    }

    static public AnimatedTexturedElement BREWING(AdvancedGuiContainer container, int xPosition, int yPosition) {
        return new AnimatedTexturedElement(container, "textures/gui/container/furnace.png",
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
