package com.R3DKn16h7.kerncraft.client.gui;

import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Created by Filippo on 27/11/2016.
 */
public class ProgressBar extends Widget {
    private Direction direction;

    private String TEXTURE_EMPTY, TEXTURE_FILL;

    ProgressBar(AdvancedGuiContainer container, int xPosition, int yPosition,
                int xSize, int ySize,
                Direction direction) {
        super(container, xPosition, yPosition, xSize, ySize);
        this.direction = direction;
    }

    @Override
    public void draw() {
        super.draw();

        ResourceLocation empty = new ResourceLocation(TEXTURE_EMPTY);
        ResourceLocation fill= new ResourceLocation(TEXTURE_FILL);

    switch (direction) {
        case LEFT:
            break;
        case RIGHT:
            //container.drawTexturedModalRect(empty, 0,0,0,0,0,0);

            //container.drawTexturedModalRect(fill, 0,0,0,0,0,0);
            break;
        case TOP:
            break;
        case BOTTOM:
            break;
    }

    }

    @Override
    public List<String> addHoveringText(List<String> hoveringText) {
        hoveringText.add("Button");
        return hoveringText;
    }

    @Override
    public void onClicked(int mouseX, int mouseY, int mouseButton) {
        System.out.print("Click!");
    }

    public enum Direction {
        LEFT, RIGHT, TOP, BOTTOM
    }
}
