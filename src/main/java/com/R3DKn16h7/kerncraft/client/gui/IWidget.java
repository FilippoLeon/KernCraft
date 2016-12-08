package com.R3DKn16h7.kerncraft.client.gui;

import java.util.List;

/**
 * Interface to a "Widget" that is displayed on an AdvancedGuiContainer.
 */
public interface IWidget {
    /**
     * Returns anchor point position (x-coordinate), by default top-left corner.
     * @return The x coordinate in pixels relative to background top-left corner.
     */
    int getPositionX();
    /**
     * Returns anchor point position (y-coordinate), by default top-left corner.
     * @return The y coordinate in pixels relative to background top-left corner.
     */
    int getPositionY();

    void init();

    /**
     * Called when widget is drawn, actually does the rendering.
     */
    void draw();

    /**
     * Called when widget is drawn, add string to the hovering tooltip.
     * @param hoveringText A list of string added displayed on the tooltip.
     * @return A reference to the hoveringText.
     */
    List<String> addHoveringText(List<String> hoveringText);

    /**
     * Called when mouse is over the widget.
     * @param mouseX Local x coordinate from anchor point.
     * @param mouseY Local y coordinate form anchor point.
     */
    void onHover(int mouseX, int mouseY);

    /**
     * Called when widget is clicked.
     * @param mouseX Local x coordinate from anchor point.
     * @param mouseY Local y coordinate form anchor point.
     * @param mouseButton The clicked mouse button.
     */
    void onClicked(int mouseX, int mouseY, int mouseButton);

    /**
     * Called when text is typed, but only if widget is
     * the active widget.
     * @param typedChar The character that has been inputted.
     * @param keyCode The keyCode that has been inputted.
     */
    void onKeyTyped(char typedChar, int keyCode);

    /**
     * Return true if the coordinates are withing the widget area.
     * @param mouseX The x coordinate.
     * @param mouseY THe y coordinate.
     * @return True if coordinates are withing area, false otherwise.
     */
    boolean isMouseInArea(int mouseX, int mouseY);

    /**
     * Return true if widget intercept key presses, i.e. listens to
     * any key press and acts accordingly.
     * @return True if widget intercepts key presses.
     */
    boolean interceptKeyPress();

    /**
     * True if widget can be activated (i.e. obtain focus).
     * @return True if widget can become active.
     */
    boolean canBecomeActive();

}
