package com.R3DKn16h7.kerncraft.manual;

import com.R3DKn16h7.kerncraft.manual.data.ManualEntry;

/**
 * Created by Filippo on 08/12/2016.
 */
public class TyrociniumChymicumXmlGui extends TyrociniumChymicumGui {
    TyrociniumChymicumXmlGui(ManualEntry entry) {
        super();


        System.out.println(entry.chapter.name + "/" + entry.name);
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);

    }

    @Override
    public String getTitle() {
        return "NULL";
    }
}
