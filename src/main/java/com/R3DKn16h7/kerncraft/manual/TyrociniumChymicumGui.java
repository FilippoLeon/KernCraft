package com.R3DKn16h7.kerncraft.manual;

import com.R3DKn16h7.kerncraft.client.gui.widgets.BetterButton;
import com.R3DKn16h7.kerncraft.client.gui.widgets.Text;
import com.R3DKn16h7.kerncraft.client.gui.widgets.Widget;
import com.R3DKn16h7.kerncraft.manual.data.Manual;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Filippo on 08/12/2016.
 */
public abstract class TyrociniumChymicumGui extends ManualEntryGui {

    static protected final int xSize = 256;
    static protected final int ySize = 180;
    /**
     * Used for Debug, so that we can reload the entire Xml at each Opening of the Book.
     */
    protected static final boolean REPARSE_XML_AT_EACH_OPENING = true;
    static final int PAGE_WIDTH = 105;
    private static final int STARTING_HOME_ROW_ID = 1;
    protected static Manual manual = null;
    private static ManualEntryGui lastOpened;
    protected boolean xmlHasBeenParsed = false;
    int guiLeft;
    int guiTop;
    Text title;
    private GuiButton btn;
    private GuiButton btn1;
    private GuiButton btn2;
    private GuiButton btn3;
    private BetterButton btn4;
    private GuiButton btn5;
    private GuiButton btn6;

    TyrociniumChymicumGui() {
        super();
        lastOpened = this;
    }


    public static ManualEntryGui Factory() {
        if (!REPARSE_XML_AT_EACH_OPENING && lastOpened != null) return lastOpened;
        else return new TyrociniumChymicumIndexGui();
    }

    @Override
    public void initGui() {
        super.initGui();

        guiLeft = (this.width - xSize) / 2;
        guiTop = (this.height - ySize) / 2;

        int ID = STARTING_HOME_ROW_ID;
        int SPACE = 30;
        int size = 20;
        int r = (xSize - (6 * (SPACE - size) + 7 * size)) / 2 + guiLeft;
        int ypos = (height - ySize) / 2 + ySize - 10;

        btn3 = new GuiButton(ID++, r, ypos, "<--");
        btn3.setWidth(size);
        add(btn3);
        btn5 = new GuiButton(ID++, r += SPACE, ypos, "<<");
        btn5.setWidth(size);
        add(btn5);
        btn = new GuiButton(ID++, r += SPACE, ypos, "<");
        btn.setWidth(size);
        add(btn);
        btn2 = new GuiButton(ID++, r += SPACE, ypos, "^");
        btn2.setWidth(size);
        add(btn2);
        btn1 = new GuiButton(ID++, r += SPACE, ypos, ">");
        btn1.setWidth(size);
        add(btn1);
        btn6 = new GuiButton(ID++, r += SPACE, ypos, ">>");
        btn6.setWidth(size);
        add(btn6);
//        btn4 = new GuiButton(ID, r += SPACE, ypos, "HOME");
//        btn4.setWidth(size);
//        add(btn4);

        int texX = 60;
        btn4 = new BetterButton(this,
                r + SPACE, ypos)
                .setSize(size, size * 2)
                .setText("")
                .setIcon("kerncraft:textures/gui/manual.png",
                        texX += size, 200, size, size)
                .setTextColor(Color.black, false)
                .setMargin(0, 0);
        btn4.setWidth(size);
        btn4.init();
        add(btn4);

        Text title = new Text(this, 0, 0, 200, 60, Widget.Alignment.MIDDLE);
        title.setText("sadasdsd");
        title.init();
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

    public abstract String getTitle();

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void drawBackground(int par1, int par2, float par3) {
        // Draw title paper
        this.mc.getTextureManager().bindTexture(
                new ResourceLocation("kerncraft:textures/gui/title_paper.png")
        );
        this.drawTexturedModalRect(guiLeft + (xSize - 184) / 2, guiTop - 40, 0, 0, 184, 60);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        // Draw manual background
        this.mc.getTextureManager().bindTexture(
                new ResourceLocation("kerncraft:textures/gui/manual.png")
        );
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        // Fall back to super background
        this.drawBackground(par1, par2, par3);

        // Fall back to super
        super.drawScreen(par1, par2, par3);

        title.draw();
    }

}
