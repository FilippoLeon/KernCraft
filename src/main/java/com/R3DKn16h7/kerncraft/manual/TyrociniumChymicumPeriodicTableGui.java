package com.R3DKn16h7.kerncraft.manual;

import com.R3DKn16h7.kerncraft.elements.ElementBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filippo on 08/12/2016.
 */
public class TyrociniumChymicumPeriodicTableGui extends TyrociniumChymicumGui {

    static int STARTING_ID = 20;
    private ArrayList<GuiButton> periodicButtonList = new ArrayList<GuiButton>(ElementBase.NUMBER_OF_ELEMENTS);

    public TyrociniumChymicumPeriodicTableGui() {

    }

    @Override
    public void superPage() {
        super.superPage();
        mc.displayGuiScreen(new TyrociniumChymicumIndexGui());
    }

    @Override
    public void initGui() {
        super.initGui();

        int id = STARTING_ID;
        int x = 0;
        int y = 0;
        int a = 0;
        int b = 0;
        int padLeft = guiLeft + 10; // 6*14;
        int padTop = guiTop + 20; //14*2;
        for (ElementBase.Element element : ElementBase.getElements()) {
            //GlStateManager.color(1.0F, 0.0F, 0.0F, 1.0F);
            int g = element.group;
            int p = element.period;
            if (g == ElementBase.GROUP.Actinide.getValue()) {
                g = 3 + a++;
                p += 3;
            } else if (g == ElementBase.GROUP.Lanthanide.getValue()) {
                g = 3 + b++;
                p += 3;
            }

            String formatting = element.state.toColor();

            Color color = element.family.toColor();

            GuiButton btn = new BetterButton(id++, 13 * (g - 1) + padLeft, 13 * (p - 1) + padTop, 14, 14,
                    formatting + element.symbol, color.getRed(), color.getGreen(), color.getBlue());
            //btn.setWidth(14);
            //btn.height = 14;
            func_189646_b(btn);
            periodicButtonList.add(btn);
        }

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id >= STARTING_ID && button.id < STARTING_ID + ElementBase.NUMBER_OF_ELEMENTS) {
            mc.displayGuiScreen(new TyrociniumChymicumElementGui(ElementBase.getElement(button.id - STARTING_ID + 1)));
        }
    }

    @Override
    public void drawBackground(int par1, int par2, float par3) {

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(
                new ResourceLocation("kerncraft:textures/gui/blank_paper2.png")
        );

        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);


        List<String> str = new ArrayList<String>();
        for (GuiButton btn : periodicButtonList) {
            if (par1 > btn.xPosition && par1 < btn.xPosition + btn.width &&
                    par2 > btn.yPosition && par2 < btn.yPosition + btn.height) {
                ElementBase.Element elem = ElementBase.getElement(btn.id - STARTING_ID + 1);


                String formatting = elem.state.toColor();

                str.add(formatting + StringUtils.capitalize(elem.name) + " (" + elem.symbol + ")");
                str.add(formatting + "Element " + elem.id);
                break;
            }
        }
        drawHoveringText(str, par1, par2);

    }

}