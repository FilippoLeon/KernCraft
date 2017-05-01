package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.guicontainer.ExtractorContainer;
import com.R3DKn16h7.kerncraft.tileentities.ExtractorTileEntity;
import net.minecraft.inventory.IInventory;

public class ExtractorGuiContainer extends MachineGuiContainer {

    public ExtractorGuiContainer(IInventory playerInv, ExtractorTileEntity te) {
        super(new ExtractorContainer(playerInv, te), playerInv, te);

        // TODO: remove
//        te.getUpdatePacket();

        setBackground("kerncraft:textures/gui/container/extractor_gui.png",
                0, 0, this.xSize, this.ySize);
    }

    @Override
    public void updateWidgets() {
        super.updateWidgets();
    }
}