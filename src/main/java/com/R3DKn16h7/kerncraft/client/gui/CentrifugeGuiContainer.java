package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.guicontainer.AdvancedContainer;
import com.R3DKn16h7.kerncraft.guicontainer.CentrifugeContainer;
import com.R3DKn16h7.kerncraft.tileentities.machines.CentrifugeTileEntity;
import net.minecraft.inventory.IInventory;

public class CentrifugeGuiContainer extends MachineGuiContainer {
    public CentrifugeGuiContainer(IInventory playerInv,
                                  CentrifugeTileEntity te) {
        super(new CentrifugeContainer(playerInv, te), playerInv, te);

        addSlotTextures((AdvancedContainer) inventorySlots);
        setDynamicBackground();
    }
}