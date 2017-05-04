package com.R3DKn16h7.kerncraft.manual;

import com.R3DKn16h7.kerncraft.manual.data.ManualEntry;
import com.R3DKn16h7.kerncraft.utils.io.XmlUtils;

import java.util.List;

/**
 * Created by Filippo on 08/12/2016.
 */
public class TyrociniumChymicumXmlGui extends TyrociniumChymicumGui {
    private static final int FIRST_COL_START = 15;
    private static final int FIRST_COL_WIDTH = 125;
    private static final int ROW_HEIGHT = 10;
    private static final int HEADER_HEIGHT = 12;
    private static int MAX_ROWS = 16;
    private final ManualEntry entry;
    List<String> listOfStringToDisplay;
    private int currentStartIndex = 0;

    TyrociniumChymicumXmlGui(ManualEntry entry) {
        super();
        this.entry = entry;
    }

    @Override
    public final void initGui() {
        super.initGui();

        String str = manual.getDescription(entry.chapter.name, entry.name);
        if (str != null) {
            String str2 = XmlUtils.parseBBCodeIntoMCFormat(str);

            listOfStringToDisplay = fontRendererObj.listFormattedStringToWidth(str2, PAGE_WIDTH);
        }
    }

    @Override
    public void beginningPage() {
        currentStartIndex = 0;
    }

    @Override
    public final void previousPage() {
        if (listOfStringToDisplay != null && currentStartIndex >= 2 * MAX_ROWS) {
            currentStartIndex -= 2 * MAX_ROWS;
        } else {
            super.previousPage();
        }
    }


    @Override
    public final void nextPage() {
        if (listOfStringToDisplay != null &&
                currentStartIndex + 2 * MAX_ROWS < listOfStringToDisplay.size()) {
            currentStartIndex += 2 * MAX_ROWS;
        } else {
            super.nextPage();
        }
    }

    @Override
    public void endPage() {
        currentStartIndex =
                2 * MAX_ROWS * ((listOfStringToDisplay.size() - 1) / (2 * MAX_ROWS));
    }

    @Override
    public final void superPage() {
        mc.displayGuiScreen(new TyrociniumChymicumIndexGui(entry.chapter, 1));
    }

    @Override
    public final void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);

        if (listOfStringToDisplay == null) return;
        int ix = currentStartIndex;
        if (ix >= listOfStringToDisplay.size()) return;
        for (int col = 0; col < 2; ++col) {
            for (int row = 0; row < MAX_ROWS; ++row) {
                fontRendererObj.drawString(listOfStringToDisplay.get(ix++),
                        guiLeft + FIRST_COL_START + FIRST_COL_WIDTH * col, guiTop + HEADER_HEIGHT
                                + row * ROW_HEIGHT,
                        0x555555);

                if (ix >= listOfStringToDisplay.size()) return;
            }
        }
    }


    @Override
    public String getTitle() {
        return entry.getTitle();
    }
}
