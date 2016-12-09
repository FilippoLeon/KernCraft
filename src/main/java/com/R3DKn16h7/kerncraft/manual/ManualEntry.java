package com.R3DKn16h7.kerncraft.manual;

import net.minecraft.client.gui.GuiScreen;

/**
 * Created by Filippo on 08/12/2016.
 */
public class ManualEntry extends GuiScreen {

    //static public HashMap<String, ManualEntry> pages = new HashMap<String, ManualEntry>();
    static public ManualEntry lastVisitedPage;

    public void previousPage() {

        lastVisitedPage = this;
    }

    public void endPage() {
        lastVisitedPage = this;
    }

    public void beginningPage() {
        lastVisitedPage = this;
    }

    public void nextPage() {
        lastVisitedPage = this;
    }

    public void homePage() {
        lastVisitedPage = this;
        mc.displayGuiScreen(new TyrociniumChymicumIndexGui());
    }

    public void superPage() {
        lastVisitedPage = this;
    }

    public void backPage() {
        if (lastVisitedPage != null) {
            ManualEntry temp = lastVisitedPage;
            lastVisitedPage = this;
            mc.displayGuiScreen(temp);
        }
    }

}
