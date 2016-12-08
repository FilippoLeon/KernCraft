package com.R3DKn16h7.kerncraft.client.gui;

import java.util.List;

/**
 * Created by Filippo on 27/11/2016.
 */
public class Widget implements IWidget {
    public static final int DEFAULT_SLOT_SIZE_X = 18;
    public static final int DEFAULT_SLOT_SIZE_Y = 18;
    public int xPosition;
    public int yPosition;
    public int xSize;
    public int ySize;
    protected AdvancedGuiContainer container;
    private Anchor anchor;

    Widget(AdvancedGuiContainer container, int xPosition, int yPosition,
           int xSize, int ySize,
           Anchor anchor) {
        this.container = container;
        this.xPosition = xPosition + container.borderLeft;
        this.yPosition = yPosition + container.borderTop;
        this.xSize = xSize;
        this.ySize = ySize;
        this.anchor = anchor;
    }

    Widget(AdvancedGuiContainer container, int xPosition, int yPosition,
           int xSize, int ySize) {
        this.container = container;
        this.xPosition = xPosition + container.borderLeft;
        this.yPosition = yPosition + container.borderTop;
        this.xSize = xSize;
        this.ySize = ySize;
        this.anchor = Anchor.LEFT;
    }

    public int getPositionX() {
        return xPosition;
    }

    public int getPositionY() {
        return yPosition;
    }

    @Override
    public boolean interceptKeyPress() {
        return false;
    }

    @Override
    public void init() {

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

    public int guiLeft() {
        return container.getGuiLeft() + xPosition;
    }

    public int guiTop() {
        return container.getGuiTop() + yPosition;
    }

    @Override
    public boolean isMouseInArea(int mouseX, int mouseY) {
        return (mouseX >= guiLeft() && mouseX <= guiLeft() + xSize &&
                mouseY >= guiTop() && mouseY <= guiTop() + ySize);
    }

    @Override
    public boolean canBecomeActive() {
        return true;
    }

    public enum Anchor {
        LEFT, MIDDLE, RIGHT
    }

}
