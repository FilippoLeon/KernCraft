package com.R3DKn16h7.kerncraft.client.gui;

import java.util.List;

/**
 * Created by Filippo on 27/11/2016.
 */
public class Tooltip extends Widget {

    Tooltip(AdvancedGuiContainer container, String texture,
            int xPosition, int yPosition,
            int xSize, int ySize) {
        super(container, xPosition, yPosition , xSize, ySize);
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public List<String> addHoveringText(List<String> hoveringText) {
        hoveringText.add("Tooltip");
        return hoveringText;
    }
}
