package com.R3DKn16h7.quantumbase.client.gui;

import java.util.List;

/**
 * Created by Filippo on 27/11/2016.
 */
public class Widget implements IWidget {
    private Anchor anchor;

    @Override
    public boolean interceptKeyPress() {
        return false;
    }


    public int getPositionX() {
        return xPosition;
    }
    public int getPositionY() {
        return yPosition;
    }

    public int xPosition;
    public int yPosition;
    public int xSize;
    public int ySize;
    protected AdvancedGuiContainer container;

    public enum Anchor {
        LEFT, MIDDLE, RIGHT
    }

    Widget(AdvancedGuiContainer container, int xPosition, int yPosition,
           int xSize, int ySize,
           Anchor anchor) {
        this.container = container;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xSize = xSize;
        this.ySize = ySize;
        this.anchor = anchor;
    }

    Widget(AdvancedGuiContainer container, int xPosition, int yPosition,
           int xSize, int ySize) {
        this.container = container;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xSize = xSize;
        this.ySize = ySize;
        this.anchor = Anchor.LEFT;
    }

    @Override
    public void draw() {
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
        if(canBecomeActive())
        container.activeWidget = this;

        System.out.print("Click!");
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        return;
    }

    @Override
    public boolean isMouseInArea(int mouseX, int mouseY) {
        return (mouseX >= xPosition && mouseX <= xPosition + xSize &&
                mouseY >= yPosition && mouseY <= yPosition + ySize);
    }

    @Override
    public boolean canBecomeActive() {
        return true;
    }

}
