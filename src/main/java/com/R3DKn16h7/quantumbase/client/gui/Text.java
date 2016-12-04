package com.R3DKn16h7.quantumbase.client.gui;

import net.minecraft.client.gui.FontRenderer;

import java.util.List;

/**
 * Created by Filippo on 27/11/2016.
 */
public class Text extends Widget {
    private String text;
    private Alignment alignment;

    public enum Alignment {
        LEFT, MIDDLE, RIGHT
    }

    Text(AdvancedGuiContainer container, int xPosition, int yPosition,
         int xSize, int ySize,
         Alignment alignment) {
        super(container, xPosition, yPosition, xSize, ySize);
        this.alignment = alignment;
    }

    @Override
    public void draw() {
        super.draw();

        FontRenderer fr = container.getFontRenderer();
    switch (alignment) {
        case LEFT:
            container.drawString(fr, text, xPosition, yPosition, 4210752);
            break;
        case MIDDLE:
            container.drawCenteredString(fr, text,
                    xPosition + xSize / 2, yPosition, 4210752);
            break;
        case RIGHT:
            container.drawString(fr, text,
                    xPosition + (xSize - fr.getStringWidth(text)), yPosition, 4210752);
            break;
    }

    }

    void setText(String text ) {
        this.text = text;
    }

    @Override
    public List<String> addHoveringText(List<String> hoveringText) {
        hoveringText.add("Button");
        return hoveringText;
    }

    @Override
    public void onClicked(int mouseButton) {
        System.out.print("Click!");
    }
}
