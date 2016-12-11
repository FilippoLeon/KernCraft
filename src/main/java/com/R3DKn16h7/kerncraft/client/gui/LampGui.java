package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.client.gui.widgets.AnimatedTexturedElement;
import com.R3DKn16h7.kerncraft.client.gui.widgets.Text;
import com.R3DKn16h7.kerncraft.tileentities.ExtractorTileEntity;
import net.minecraft.inventory.IInventory;

public class LampGui extends AdvancedGui {

    AnimatedTexturedElement flame;
    AnimatedTexturedElement brewing;
    Text progressText;

    public LampGui(IInventory playerInv, ExtractorTileEntity te) {
        super(playerInv, te);

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateWidgets() {
        super.updateWidgets();

    }
}