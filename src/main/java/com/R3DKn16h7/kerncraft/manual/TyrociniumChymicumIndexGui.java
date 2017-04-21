package com.R3DKn16h7.kerncraft.manual;

import com.R3DKn16h7.kerncraft.achievements.AchievementHandler;
import com.R3DKn16h7.kerncraft.capabilities.ITyrociniumProgressCapability;
import com.R3DKn16h7.kerncraft.capabilities.TyrociniumProgressDefaultCapability;
import com.R3DKn16h7.kerncraft.client.gui.IAdvancedGuiContainer;
import com.R3DKn16h7.kerncraft.client.gui.widgets.BetterButton;
import com.R3DKn16h7.kerncraft.manual.data.Manual;
import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.MessageUnlock;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Filippo on 08/12/2016.
 */
public class TyrociniumChymicumIndexGui extends TyrociniumChymicumGui implements IAdvancedGuiContainer {
    BetterButton PERIODIC_TABLE_BUTTON;
    BetterButton PERIODIC_TABLE_FIRST_STEPS;

    public TyrociniumChymicumIndexGui() {
        if (REPARSE_XML_AT_EACH_OPENING || !xmlHasBeenParsed) {
            manual = new Manual("assets/kerncraft/config/manual_pages.xml");
        }
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
                .setSize(PAGE_WIDTH, buttonHeight)
                .setText("Periodic Table")
                .setTextColor(Color.black, false)
                .setTransparent()
                .setMargin(xmargin, 0);
        PERIODIC_TABLE_BUTTON.init();
        if (isUnlocked(Content.FirstSteps, 0)) {
            PERIODIC_TABLE_FIRST_STEPS = new BetterButton(this,
                    guiLeft + 10, guiTop + marginTop + buttonHeight * i++)
                    .setSize(PAGE_WIDTH, buttonHeight)
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

        if(mc.player.hasCapability(TyrociniumProgressDefaultCapability.INSTANCE, null)) {
            ITyrociniumProgressCapability capability = mc.player.getCapability(TyrociniumProgressDefaultCapability.INSTANCE, null);
            capability.unlockContent("first_steps");
        }
        KernCraftNetwork.networkWrapper.sendToServer(new MessageUnlock("first_steps"));
    }

    public boolean isUnlocked(Content content, int param) {

        if(mc.player.hasCapability(TyrociniumProgressDefaultCapability.INSTANCE, null)) {
            ITyrociniumProgressCapability capability = mc.player.getCapability(TyrociniumProgressDefaultCapability.INSTANCE, null);
            return capability.isContentUnlocked("first_steps");
        }

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
