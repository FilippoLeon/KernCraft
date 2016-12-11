package com.R3DKn16h7.kerncraft.manual;

import com.R3DKn16h7.kerncraft.client.gui.IAdvancedGuiContainer;
import com.R3DKn16h7.kerncraft.client.gui.widgets.BetterButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Filippo on 08/12/2016.
 */
public class TyrociniumChymicumIndexGui extends TyrociniumChymicumGui implements IAdvancedGuiContainer {

    BetterButton PERIODIC_TABLE_BUTTON;
    BetterButton PERIODIC_TABLE_FIRST_STEPS;

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

        PERIODIC_TABLE_BUTTON = new BetterButton(this,
                guiLeft + marginLeft, guiTop + marginTop + buttonHeight * i++)
                .setSize(pagewidth, buttonHeight)
                .setText("Periodic Table")
                .setTextColor(Color.black, false)
                .setTransparent()
                .setMargin(xmargin, 0);
        PERIODIC_TABLE_BUTTON.init();
        if (isUnlocked(Content.FirstSteps, 0)) {
            PERIODIC_TABLE_FIRST_STEPS = new BetterButton(this,
                    guiLeft + 10, guiTop + marginTop + buttonHeight * i++)
                    .setSize(pagewidth, buttonHeight)
                    .setText("First steps")
                    .setTransparent()
                    .setTextColor(Color.black, false)
                    .setMargin(xmargin, 0);
            PERIODIC_TABLE_FIRST_STEPS.init();
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
