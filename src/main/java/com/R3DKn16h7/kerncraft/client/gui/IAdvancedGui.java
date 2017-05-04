package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.client.gui.widgets.IWidget;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

/**
 * Created by Filippo on 10/12/2016.
 */
public interface IAdvancedGui {
    void add(GuiButton btn);

    int nextId();

    int getGuiLeft();

    int getGuiTop();

    void setActiveWidget(IWidget widget);

    int getBorderLeft();

    int getBorderTop();

    FontRenderer getFontRenderer();
}
