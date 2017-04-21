package com.R3DKn16h7.kerncraft.manual;

import com.R3DKn16h7.kerncraft.achievements.AchievementHandler;
import com.R3DKn16h7.kerncraft.capabilities.ITyrociniumProgressCapability;
import com.R3DKn16h7.kerncraft.capabilities.TyrociniumProgressDefaultCapability;
import com.R3DKn16h7.kerncraft.client.gui.IAdvancedGui;
import com.R3DKn16h7.kerncraft.client.gui.widgets.BetterButton;
import com.R3DKn16h7.kerncraft.manual.data.IManualEntry;
import com.R3DKn16h7.kerncraft.manual.data.Manual;
import com.R3DKn16h7.kerncraft.manual.data.ManualEntry;
import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.MessageUnlock;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Filippo on 08/12/2016.
 */
public class TyrociniumChymicumIndexGui extends TyrociniumChymicumGui implements IAdvancedGui {

    static private int MARGIN_LEFT = 10;
    static private int MARGIN_TOP = 14;
    static private int BUTTON_HEIGHT = 14;
    static private int X_MARGIN = 7;
    private final int depth;
    BetterButton PERIODIC_TABLE_BUTTON;
    Map<BetterButton, Integer> dynamicallyGeneratedButtons = new HashMap<>();
    private IManualEntry entry;

    public TyrociniumChymicumIndexGui() {
        this(manual, 0);
    }

    public TyrociniumChymicumIndexGui(IManualEntry entry, int depth) {
        this.depth = depth;
        this.entry = entry;
        if (REPARSE_XML_AT_EACH_OPENING || !xmlHasBeenParsed) {
            manual = new Manual("assets/kerncraft/config/manual_pages.xml");
            if (this.entry == null) this.entry = manual;
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        int I = 0;

        if (depth == 0) {
            PERIODIC_TABLE_BUTTON = new BetterButton(this,
                    guiLeft + MARGIN_LEFT, guiTop + MARGIN_TOP + BUTTON_HEIGHT * I++)
                    .setSize(PAGE_WIDTH, BUTTON_HEIGHT)
                    .setText("Periodic Table")
                    .setTextColor(Color.black, false)
                    .setTransparent()
                    .setMargin(X_MARGIN, 0);
            PERIODIC_TABLE_BUTTON.init();
        }
        List<IManualEntry> entries = entry.getChilds();
        for (int j = 0; j < entry.getNumberOfChilds(); ++j) {
            if (!entries.get(j).isLocked()) {
                BetterButton chapterButton = new BetterButton(this,
                        guiLeft + 10, guiTop + MARGIN_TOP + BUTTON_HEIGHT * I++)
                        .setSize(PAGE_WIDTH, BUTTON_HEIGHT)
                        .setText(entries.get(j).getTitle())
                        .setTransparent()
                        .setTextColor(Color.black, false)
                        .setMargin(X_MARGIN, 0);
                chapterButton.init();
                dynamicallyGeneratedButtons.put(chapterButton, j);
            }
        }
    }

    @Override
    public void beginningPage() {
        if (depth > 0) {
            displayInRange(entry.getParent().getNumberOfChilds() - 1,
                    -1, true);
        }
    }

    @Override
    public void previousPage() {
        if (depth > 0) {
            displayInRange(entry.getIndex() - 1,
                    -1, true);
        }
    }

    @Override
    public void nextPage() {
        if (depth > 0) {
            displayInRange(entry.getIndex() + 1,
                    entry.getParent().getNumberOfChilds(), false);
        }
    }

    @Override
    public void endPage() {
        if (depth > 0) {
            displayInRange(0,
                    entry.getParent().getNumberOfChilds(), false);
        }
    }


    public void displayInRange(int start, int end, boolean reverse) {
        for (int i = start; (reverse ? i > end : i < end); i = i + (reverse ? -1 : 1)) {
            IManualEntry nextEntry = entry.getParent().getChilds().get(i);
            if (!nextEntry.isLocked()) {
                mc.displayGuiScreen(
                        new TyrociniumChymicumIndexGui(nextEntry, depth)
                );
            }
        }
    }

    @Override
    public void superPage() {
        if (depth > 0) {
            mc.displayGuiScreen(new TyrociniumChymicumIndexGui(entry.getParent(), depth - 1));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button == PERIODIC_TABLE_BUTTON) {
            mc.displayGuiScreen(new TyrociniumChymicumPeriodicTableGui());
            unlock(Content.FirstSteps);
        } else if (dynamicallyGeneratedButtons.containsKey(button)) {

            int indexInEntry = dynamicallyGeneratedButtons.get(button);
            IManualEntry childEntry = entry.getChilds().get(indexInEntry);

            TyrociniumChymicumGui newGui;
            if (childEntry instanceof ManualEntry) {
                newGui = new TyrociniumChymicumXmlGui(((ManualEntry) childEntry));
            } else {
                newGui = new TyrociniumChymicumIndexGui(childEntry, depth + 1);
            }

            mc.displayGuiScreen(newGui);
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

    @Override
    public String getTitle() {
        return entry.getTitle();
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
