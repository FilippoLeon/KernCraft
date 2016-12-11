package com.R3DKn16h7.kerncraft.client.gui;

import net.minecraft.client.gui.GuiButton;

/**
 * Created by Filippo on 10/12/2016.
 */
public interface IAdvancedGuiContainer {
    void add(GuiButton btn);

    int nextId();

    int getGuiLeft();

    int getGuiTop();
}
