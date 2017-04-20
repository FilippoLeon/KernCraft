package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.client.gui.widgets.AnimatedTexturedElement;
import com.R3DKn16h7.kerncraft.client.gui.widgets.StateButton;
import com.R3DKn16h7.kerncraft.client.gui.widgets.Text;
import com.R3DKn16h7.kerncraft.guicontainer.ExtractorContainer;
import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.MessageRedstoneControl;
import com.R3DKn16h7.kerncraft.network.MessageSideConfig;
import com.R3DKn16h7.kerncraft.tileentities.ExtractorTileEntity;
import com.R3DKn16h7.kerncraft.tileentities.IRedstoneSettable;
import net.minecraft.inventory.IInventory;

import java.awt.*;
import java.util.function.IntConsumer;

public class ExtractorGuiContainer extends MachineGuiContainer {

    public ExtractorGuiContainer(IInventory playerInv, ExtractorTileEntity te) {
        super(new ExtractorContainer(playerInv, te), playerInv, te);

        // TODO: remove
        te.getUpdatePacket();

        setBackground("kerncraft:textures/gui/container/extractor_gui.png",
                0, 0, this.xSize, this.ySize);
    }

    @Override
    public void updateWidgets() {
        super.updateWidgets();
    }
}