package com.R3DKn16h7.kerncraft.manual;

import com.R3DKn16h7.kerncraft.client.gui.widgets.BetterButton;
import com.R3DKn16h7.kerncraft.client.gui.widgets.Text;
import com.R3DKn16h7.kerncraft.client.gui.widgets.Widget;
import com.R3DKn16h7.kerncraft.manual.data.Manual;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

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
    private BetterButton btn;
    private BetterButton btn1;
    private BetterButton btn2;
    private BetterButton btn3;
    private BetterButton btn4;
    private BetterButton btn5;
    private BetterButton btn6;

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
        int r = (xSize - (6 * (SPACE - size) + 7 * size)) / 2 + guiLeft - 1;
        int ypos = (height - ySize) / 2 + ySize - 15;

        int texX = 60;
        btn3 = new BetterButton(this,
                r, ypos)
                .setSize(size, size * 2)
                .setTransparent()
                .setText("")
                .setIcon("kerncraft:textures/gui/manual.png",
                        texX, 200, size, size)
                .setTextColor(Color.black, false)
                .setTooltip("Back")
                .setMargin(0, 0);
        btn3.setWidth(size);
        btn3.init();

        btn5 = new BetterButton(this,
                r += SPACE, ypos)
                .setSize(size, size * 2)
                .setTransparent()
                .setText("")
                .setIcon("kerncraft:textures/gui/manual.png",
                        texX += size, 200, size, size)
                .setTextColor(Color.black, false)
                .setTooltip("Goto begin")
                .setMargin(0, 0);
        btn5.setWidth(size);
        btn5.init();

        btn = new BetterButton(this,
                r += SPACE, ypos)
                .setSize(size, size * 2)
                .setTransparent()
                .setText("")
                .setIcon("kerncraft:textures/gui/manual.png",
                        texX += size, 200, size, size)
                .setTextColor(Color.black, false)
                .setTooltip("Previous")
                .setMargin(0, 0);
        btn.setWidth(size);
        btn.init();

        btn2 = new BetterButton(this,
                r += SPACE, ypos)
                .setSize(size, size * 2)
                .setTransparent()
                .setText("")
                .setIcon("kerncraft:textures/gui/manual.png",
                        texX += size, 200, size, size)
                .setTextColor(Color.black, false)
                .setTooltip("Super")
                .setMargin(0, 0);
        btn2.setWidth(size);
        btn2.init();

        btn1 = new BetterButton(this,
                r += SPACE, ypos)
                .setSize(size, size * 2)
                .setTransparent()
                .setText("")
                .setIcon("kerncraft:textures/gui/manual.png",
                        texX += size, 200, size, size)
                .setTextColor(Color.black, false)
                .setTooltip("Next")
                .setMargin(0, 0);
        btn1.setWidth(size);
        btn1.init();

        btn6 = new BetterButton(this,
                r += SPACE, ypos)
                .setSize(size, size * 2)
                .setTransparent()
                .setText("")
                .setIcon("kerncraft:textures/gui/manual.png",
                        texX += size, 200, size, size)
                .setTextColor(Color.black, false)
                .setTooltip("Goto end")
                .setMargin(0, 0);
        btn6.setWidth(size);
        btn6.init();

        btn4 = new BetterButton(this,
                r += SPACE, ypos)
                .setSize(size, size * 2)
                .setTransparent()
                .setText("")
                .setIcon("kerncraft:textures/gui/manual.png",
                        texX += size, 200, size, size)
                .setTextColor(Color.black, false)
                .setTint(Color.blue)
                .setTooltip("Home")
                .setMargin(0, 0);
        btn4.setWidth(size);
        btn4.init();

        title = new Text(this, (int) (guiLeft / 1.5), (int) ((guiTop - 55) / 1.5f),
                ySize, 60, Widget.Alignment.MIDDLE);
        title.setShadow(true);
        title.init();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
//        super.actionPerformed(button);

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

        GL11.glPushMatrix();
        GL11.glScalef(1.5f, 1.5f, 1.5f);
        title.setText(getTitle());
        if (title != null) title.draw();
        GL11.glPopMatrix();
    }

}
