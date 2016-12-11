package com.R3DKn16h7.kerncraft.manual;

import com.R3DKn16h7.kerncraft.client.gui.IAdvancedGuiContainer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

/**
 * Created by Filippo on 08/12/2016.
 */
public class ManualEntry extends GuiScreen implements IAdvancedGuiContainer {

    //static public HashMap<String, ManualEntry> pages = new HashMap<String, ManualEntry>();
    static public ManualEntry lastVisitedPage;
    private int btn_id = 0;

    public int nextId() {
        return btn_id++;
    }

    public int getGuiLeft() {
        return 0;
    }

    public int getGuiTop() {
        return 0;
    }

    public void add(GuiButton btn) {
        this.func_189646_b(btn);
    }

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
