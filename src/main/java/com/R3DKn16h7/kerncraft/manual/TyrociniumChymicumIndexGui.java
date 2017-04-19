package com.R3DKn16h7.kerncraft.manual;

import com.R3DKn16h7.kerncraft.achievements.AchievementHandler;
import com.R3DKn16h7.kerncraft.client.gui.IAdvancedGuiContainer;
import com.R3DKn16h7.kerncraft.client.gui.widgets.BetterButton;
import com.R3DKn16h7.kerncraft.events.IExampleCapability;
import com.R3DKn16h7.kerncraft.events.TestCabability;
import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.MessageUnlock;
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

            unlock(Content.FirstSteps);
        }
    }

    protected void unlock(Content content) {
        switch (content) {
            case FirstSteps:
                mc.player.addStat(AchievementHandler.LEARNER, 1);
                break;
        }

//        NBTTagCompound nbt = mc.player.getEntityData();
//        nbt.setBoolean(content.toString(), true);
//        mc.player.writeToNBT(nbt);

        KernCraftNetwork.networkWrapper.sendToServer(new MessageUnlock(1));
    }

    public boolean isUnlocked(Content content, int param) {

        if(mc.player.hasCapability(TestCabability.INSTANCE, null)) {
            IExampleCapability capability = mc.player.getCapability(TestCabability.INSTANCE, null);
            capability.unlockContent(1);
        }
//        switch (content) {
//            case FirstSteps:
//                NBTTagCompound nbt = mc.player.getEntityData();
//                return nbt.hasKey(content.toString());
//        }
        return false;
    }

    private enum Content {
        FirstSteps("kernkraft_first_steps_unlocked");

        public String nbtKey;

        Content(String nbtKey) {
            this.nbtKey = nbtKey;
        }

        public String toString() {
            return nbtKey;
        }

    }
}
