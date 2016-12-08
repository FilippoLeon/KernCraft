package com.R3DKn16h7.kerncraft.client.gui;

import java.util.List;

/**
 * Created by Filippo on 27/11/2016.
 */
public class Button extends TexturedElement {


    Button(AdvancedGuiContainer container, String texture, int xPosition, int yPosition,
           int xSize, int ySize,
           int offsetX, int offsetY) {
        super(container, texture, xPosition, yPosition, xSize, ySize, offsetX, offsetY);
    }

    @Override
    public void draw() {
        super.draw();
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
}
