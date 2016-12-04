package com.R3DKn16h7.quantumbase.client.gui;

import java.util.List;

/**
 * Created by Filippo on 27/11/2016.
 */
public interface IWidget {

    void draw();

    List<String> addHoveringText(List<String> hoveringText);

    void click(int mouseButton);

    boolean isMouseInArea(int mouseX, int mouseY);
}
