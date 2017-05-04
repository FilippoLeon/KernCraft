package com.R3DKn16h7.kerncraft.manual;

import com.R3DKn16h7.kerncraft.elements.Element;
import com.R3DKn16h7.kerncraft.elements.ElementRegistry;
import com.R3DKn16h7.kerncraft.utils.io.XmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

/**
 * Created by Filippo on 08/12/2016.
 *
 * The GUI that is displayed when an Element is viewed.
 */
public class TyrociniumChymicumElementGui extends TyrociniumChymicumGui {

    private static final int FIRST_COL_START = 15;
    private static final int FIRST_COL_WIDTH = 125;
    private static final int ROW_HEIGHT = 10;
    private static final int HEADER_HEIGHT = 45;
    private static int MAX_ROWS = 12;
    List<String> listOfStringToDisplay;
    private Element element;
    private int currentStartIndex = 0;
    private boolean summaryPage = false;

    TyrociniumChymicumElementGui(Element element) {
        this.element = element;
    }

    @Override
    public final void initGui() {
        super.initGui();

        String str = element.loadDescription();
        if (str != null) {
            String str2 = XmlUtils.parseBBCodeIntoMCFormat(str);

            listOfStringToDisplay = fontRendererObj.listFormattedStringToWidth(str2, PAGE_WIDTH);
        }

    }

    @Override
    public String getTitle() {
        return element.getLocalizedName();
    }

    @Override
    public final void previousPage() {
        if (summaryPage) {
            summaryPage = false;
        } else if (listOfStringToDisplay != null && currentStartIndex >= 2 * MAX_ROWS) {
            currentStartIndex -= 2 * MAX_ROWS;
        } else if (element.id > 1) {
            super.previousPage();
            mc.displayGuiScreen(
                    new TyrociniumChymicumElementGui(
                            ElementRegistry.getElement(element.id - 1)
                    ));
        } else {
            superPage();
        }
    }

    @Override
    public void beginningPage() {
        super.beginningPage();
        mc.displayGuiScreen(
                new TyrociniumChymicumElementGui(ElementRegistry.getElement(1)
                ));
    }

    @Override
    public void endPage() {
        super.endPage();
        mc.displayGuiScreen(
                new TyrociniumChymicumElementGui(
                        ElementRegistry.getElement(ElementRegistry.NUMBER_OF_ELEMENTS)
                ));
    }

    @Override
    public final void nextPage() {
        if (listOfStringToDisplay != null && currentStartIndex + 2 * MAX_ROWS < listOfStringToDisplay.size()) {
            currentStartIndex += 2 * MAX_ROWS;
        } else if (!summaryPage) {
            summaryPage = true;
        } else if (element.id < ElementRegistry.NUMBER_OF_ELEMENTS) {
            super.nextPage();
            mc.displayGuiScreen(
                    new TyrociniumChymicumElementGui(
                            ElementRegistry.getElement(element.id + 1)
                    ));
        } else {
            superPage();
        }
    }

    @Override
    public final void superPage() {
        super.superPage();
        mc.displayGuiScreen(new TyrociniumChymicumPeriodicTableGui());
    }

    @Override
    public final void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);

        GL11.glPushMatrix();
        GL11.glScalef(3, 3, 3);

        String ids = Integer.toString(element.id);

        //int color = element.family.toColor().getRGB();
        int color = ElementRegistry.StringToColor(element.color).getRGB();

        int le = fontRendererObj.getStringWidth(ids);
        fontRendererObj.drawString(element.symbol, (guiLeft + 15) / 3, (guiTop + 15) / 3, color);
        fontRendererObj.drawString(ids, (guiLeft + xSize - 10) / 3 - le, (guiTop + 15) / 3, 0x333333);
        //fontRendererObj.drawStringWithShadow(element.symbol, (guiLeft + 15) / 3, (guiTop + 15)/ 3, color );
        //fontRendererObj.drawStringWithShadow(ids, (guiLeft + xSize - 10) / 3 - le, (guiTop + 15)/ 3, 0x333333);

        //drawString(fontRendererObj, StringUtils.capitalize(element.symbol), (guiLeft + 15) / 3, (guiTop + 15)/ 3,0xff3333);

        //drawString(fontRendererObj, ids, (guiLeft + xSize - 15 - 20*ids.length()) / 3, (guiTop + 15)/ 3, 0x333333);
        GL11.glPopMatrix();
        fontRendererObj.drawString(StringUtils.capitalize(element.getName()),
                guiLeft + 50, guiTop + 10, 0x222222);
        fontRendererObj.drawString("Group " + Element.Group.toString(element.group),
                guiLeft + 50, guiTop + 20, 0x222222);
        fontRendererObj.drawString(element.toxic ? "Toxic" : "",
                guiLeft + 50, guiTop + 30, new Color(21, 121, 18).getRGB());
        fontRendererObj.drawString(StringUtils.capitalize(element.state.toString().toLowerCase()) + " " + element.family.I18n().toLowerCase(),
                guiLeft + 15 + 125, guiTop + 10, element.family.toColor().getRGB());
        fontRendererObj.drawString("Period " + element.period,
                guiLeft + 15 + 125, guiTop + 20, 0x222222);
        fontRendererObj.drawString(element.halfLife > 0 ? "Unstable" : "",
                guiLeft + 50, guiTop + 30, new Color(121, 116, 18).getRGB());
        //drawString(fontRendererObj , StringUtils.capitalize(element.name), guiLeft + 50, guiTop + 10, 0xdddddd);
        //drawString(fontRendererObj , StringUtils.capitalize(element.toxic ? "true" : "False"), guiLeft + 50, guiTop + 20, 0xdddddd);

        // TODO: i18n
        if (summaryPage) {
            int col = 0;
            fontRendererObj.drawString("Family: " + element.family.I18n().toLowerCase(),
                    guiLeft + FIRST_COL_START, guiTop + HEADER_HEIGHT + col++ * ROW_HEIGHT, 0x555555);
            fontRendererObj.drawString("State: " + element.state.toString().toLowerCase(),
                    guiLeft + FIRST_COL_START, guiTop + HEADER_HEIGHT + col++ * ROW_HEIGHT, 0x555555);
            fontRendererObj.drawString("Mass: " + element.mass,
                    guiLeft + FIRST_COL_START, guiTop + HEADER_HEIGHT + col++ * ROW_HEIGHT, 0x555555);
            fontRendererObj.drawString("Density: " + element.density,
                    guiLeft + FIRST_COL_START, guiTop + HEADER_HEIGHT + col++ * ROW_HEIGHT, 0x555555);
            fontRendererObj.drawString("Radius: " + element.radius,
                    guiLeft + FIRST_COL_START, guiTop + HEADER_HEIGHT + col++ * ROW_HEIGHT, 0x555555);
            fontRendererObj.drawString("Color: " + element.color,
                    guiLeft + FIRST_COL_START, guiTop + HEADER_HEIGHT + col++ * ROW_HEIGHT, 0x555555);
            col = 0;
            fontRendererObj.drawString("Stable: " + (element.halfLife > 0 ? "no" : "yes"),
                    guiLeft + FIRST_COL_START + FIRST_COL_WIDTH, guiTop + HEADER_HEIGHT + col++ * ROW_HEIGHT, 0x555555);
            if (element.halfLife > 0) {
                fontRendererObj.drawString(String.format("Half-life: %.2e", element.halfLife),
                        guiLeft + FIRST_COL_START + FIRST_COL_WIDTH, guiTop + HEADER_HEIGHT + col++ * ROW_HEIGHT, 0x555555);
            }
            fontRendererObj.drawString("Toxic: " + (element.toxic ? "yes" : "no"),
                    guiLeft + FIRST_COL_START + FIRST_COL_WIDTH, guiTop + HEADER_HEIGHT + col++ * ROW_HEIGHT, 0x555555);
            return;
        }

        if (listOfStringToDisplay == null) return;
        int ix = currentStartIndex;
        if (ix >= listOfStringToDisplay.size()) return;
        for (int col = 0; col < 2; ++col) {
            for (int row = 0; row < MAX_ROWS; ++row) {
//                int l = element.shortDescription.length();
                //if(start >=l ) break;
                //int end = Math.min(start + size, l);
                //String str = element.shortDescription.substring(start, end);
                //if(str.equals("")) break;
                //fontRendererObj.drawString(str, guiLeft + 15 + 125*col, guiTop + 45 + row * 10, 0x555555);
                fontRendererObj.drawString(listOfStringToDisplay.get(ix++),
                        guiLeft + FIRST_COL_START + FIRST_COL_WIDTH * col, guiTop + HEADER_HEIGHT
                                + row * ROW_HEIGHT,
                        0x555555);

                if (ix >= listOfStringToDisplay.size()) return;

                //drawString(fontRendererObj, str, guiLeft + 15 + 125*col, guiTop + 45 + row * 10, 0x555555);
                //start += size;
            }
        }
        //fontRendererObj.drawSplitString(element.shortDescription, guiLeft + 15, guiTop + 45, 120, 0x555555);
    }

}
