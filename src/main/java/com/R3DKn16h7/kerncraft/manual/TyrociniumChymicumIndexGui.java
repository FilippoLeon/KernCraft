package com.R3DKn16h7.kerncraft.manual;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;

/**
 * Created by Filippo on 08/12/2016.
 */
public class TyrociniumChymicumIndexGui extends TyrociniumChymicumGui {

    GuiButton PERIODIC_TABLE_BUTTON;
    GuiButton PERIODIC_TABLE_BUTTON2;

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

        PERIODIC_TABLE_BUTTON = new BetterButton(0, guiLeft + marginLeft, guiTop + marginTop + buttonHeight * i++, 200, buttonHeight,
                "Periodic Table", null, null, 0, 0, 0, 0, BetterButton.Alignment.LEFT, xmargin, 0);
        //PERIODIC_TABLE_BUTTON = new GuiButton(0, guiLeft + 10, guiTop + 10, "Periodic Table");
        PERIODIC_TABLE_BUTTON.setWidth(pagewidth);
        func_189646_b(PERIODIC_TABLE_BUTTON);
        if (isUnlocked(Content.FirstSteps)) {
            //PERIODIC_TABLE_BUTTON2 = new GuiButton(0, guiLeft + marginLeft, guiTop + 24, "First steps");
            PERIODIC_TABLE_BUTTON2 = new BetterButton(1, guiLeft + 10, guiTop + marginTop + buttonHeight * i++, 200, buttonHeight,
                    "First steps", null, null, 0, 0, 0, 0, BetterButton.Alignment.LEFT, xmargin, 0);
            PERIODIC_TABLE_BUTTON2.setWidth(pagewidth);
            func_189646_b(PERIODIC_TABLE_BUTTON2);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button == PERIODIC_TABLE_BUTTON) {
            mc.displayGuiScreen(new TyrociniumChymicumPeriodicTableGui());

            NBTTagCompound nbt = mc.thePlayer.getEntityData();
            nbt.setBoolean("first_Steps_unlocked", true);
            mc.thePlayer.writeToNBT(nbt);
        }
    }

    public boolean isUnlocked(Content content) {

        NBTTagCompound nbt = mc.thePlayer.getEntityData();
        return nbt.hasKey("first_Steps_unlocked");
        // mc.thePlayer.writeToNBT(nbt);
    }

    enum Content {
        FirstSteps
    }
}
