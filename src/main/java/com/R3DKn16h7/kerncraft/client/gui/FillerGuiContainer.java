package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.guicontainer.AdvancedContainer;
import com.R3DKn16h7.kerncraft.guicontainer.FillerContainer;
import com.R3DKn16h7.kerncraft.tileentities.machines.FillerTileEntity;
import net.minecraft.inventory.IInventory;

public class FillerGuiContainer extends MachineGuiContainer {
    public FillerGuiContainer(IInventory playerInv,
                              FillerTileEntity te) {
        super(new FillerContainer(playerInv, te), playerInv, te);

        addSlotTextures((AdvancedContainer) inventorySlots);
        setDynamicBackground();
    }
}