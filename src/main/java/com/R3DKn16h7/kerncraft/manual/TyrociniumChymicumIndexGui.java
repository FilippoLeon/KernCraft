package com.R3DKn16h7.kerncraft.manual;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Filippo on 08/12/2016.
 */
public class TyrociniumChymicumIndexGui extends TyrociniumChymicumGui {

    GuiButton PERIODIC_TABLE_BUTTON;
    GuiButton PERIODIC_TABLE_FIRST_STEPS;

    public TyrociniumChymicumIndexGui() {
    }

    @Override
    public void initGui() {
        super.initGui();
        int marginLeft = 10;
        int marginTop = 14;
        int buttonHeight = 14;
        int xmargin = 7;
        int i = 0;

        PERIODIC_TABLE_BUTTON = new BetterButton(0, guiLeft + marginLeft,
                guiTop + marginTop + buttonHeight * i++, 200, buttonHeight,
                "Periodic Table", null, null, 0, 0,
                0, 0, BetterButton.Alignment.LEFT, xmargin, 0, Color.black);
        PERIODIC_TABLE_BUTTON.setWidth(pagewidth);
        func_189646_b(PERIODIC_TABLE_BUTTON);
        if (isUnlocked(Content.FirstSteps, 0)) {
            PERIODIC_TABLE_FIRST_STEPS = new BetterButton(1, guiLeft + 10,
                    guiTop + marginTop + buttonHeight * i++, 200, buttonHeight,
                    "First steps", null, null, 0, 0,
                    0, 0, BetterButton.Alignment.LEFT, xmargin, 0, Color.black);
            PERIODIC_TABLE_FIRST_STEPS.setWidth(pagewidth);
            func_189646_b(PERIODIC_TABLE_FIRST_STEPS);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button == PERIODIC_TABLE_BUTTON) {
            mc.displayGuiScreen(new TyrociniumChymicumPeriodicTableGui());

            NBTTagCompound nbt = mc.thePlayer.getEntityData();
            nbt.setBoolean("kernkraft_first_steps_unlocked", true);
            mc.thePlayer.writeToNBT(nbt);
        }
    }

    public boolean isUnlocked(Content content, int param) {

        switch (content) {
            case FirstSteps:
                NBTTagCompound nbt = mc.thePlayer.getEntityData();
                return nbt.hasKey("kernkraft_first_steps_unlocked");
        }
        return false;
    }

    enum Content {
        FirstSteps
    }
}
