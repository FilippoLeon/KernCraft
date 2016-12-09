package com.R3DKn16h7.kerncraft.manual;

import net.minecraft.client.gui.GuiButton;

import java.io.IOException;

/**
 * Created by Filippo on 08/12/2016.
 */
public class TyrociniumChymicumIndexGui extends TyrociniumChymicumGui {

    GuiButton PERIODIC_TABLE_BUTTON;

    public TyrociniumChymicumIndexGui() {
    }

    @Override
    public void initGui() {
        super.initGui();

        PERIODIC_TABLE_BUTTON = new GuiButton(0, guiLeft + 10, guiTop + 10, "Periodic Table");
        PERIODIC_TABLE_BUTTON.setWidth(pagewidth);
        func_189646_b(PERIODIC_TABLE_BUTTON);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button == PERIODIC_TABLE_BUTTON) {
            mc.displayGuiScreen(new TyrociniumChymicumPeriodicTableGui());
        }
    }
}
