package com.R3DKn16h7.kerncraft.manual;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

/**
 * Created by Filippo on 08/12/2016.
 */
public class TyrociniumChymicumGui extends ManualEntry {

    private static final int STARTING_HOME_ROW_ID = 1;
    public static ManualEntry lastOpened;
    protected final int xSize = 256;
    protected final int ySize = 180;
    protected int guiLeft;
    protected int guiTop;
    int pagewidth = 105;
    private GuiButton btn;
    private GuiButton btn1;
    private GuiButton btn2;
    private GuiButton btn3;
    private GuiButton btn4;
    private GuiButton btn5;
    private GuiButton btn6;

    public TyrociniumChymicumGui() {
        super();
        lastOpened = this;
    }

    public static ManualEntry Factory() {

        if (lastOpened != null) return lastOpened;
        else return new TyrociniumChymicumIndexGui();
    }

    @Override
    public void initGui() {
        super.initGui();

        guiLeft = (this.width - xSize) / 2;
        guiTop = (this.height - ySize) / 2;

        int ID = STARTING_HOME_ROW_ID;
        int r = 0 + guiLeft;
        int SPACE = 20;
        int size = 20;
        int ypos = (height - ySize) / 2 + ySize;
        btn3 = new GuiButton(ID++, r, ypos, "<--");
        btn3.setWidth(size);
        func_189646_b(btn3);
        btn5 = new GuiButton(ID++, r += SPACE, ypos, "<<");
        btn5.setWidth(size);
        func_189646_b(btn5);
        btn = new GuiButton(ID++, r += SPACE, ypos, "<");
        btn.setWidth(size);
        func_189646_b(btn);
        btn2 = new GuiButton(ID++, r += SPACE, ypos, "^");
        btn2.setWidth(size);
        func_189646_b(btn2);
        btn1 = new GuiButton(ID++, r += SPACE, ypos, ">");
        btn1.setWidth(size);
        func_189646_b(btn1);
        btn6 = new GuiButton(ID++, r += SPACE, ypos, ">>");
        btn6.setWidth(size);
        func_189646_b(btn6);
        btn4 = new GuiButton(ID++, r += SPACE, ypos, "HOME");
        btn4.setWidth(size);
        func_189646_b(btn4);
    }


    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button == btn5) {
            beginningPage();
        } else if (button == btn) {
            previousPage();
        } else if (button == btn1) {
            nextPage();
        } else if (button == btn6) {
            endPage();
        } else if (button == btn4) {
            homePage();
        } else if (button == btn3) {
            backPage();
        } else if (button == btn2) {
            superPage();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void drawBackground(int par1, int par2, float par3) {
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(
                new ResourceLocation("kerncraft:textures/gui/manual.png")
        );

        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        this.drawBackground(par1, par2, par3);

        super.drawScreen(par1, par2, par3);

    }

}
