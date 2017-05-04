package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.guicontainer.AdvancedContainer;
import com.R3DKn16h7.kerncraft.guicontainer.ElectrolyzerContainer;
import com.R3DKn16h7.kerncraft.tileentities.ElectrolyzerTileEntity;
import net.minecraft.inventory.IInventory;

public class ElectrolyzerGuiContainer extends MachineGuiContainer {
    public ElectrolyzerGuiContainer(IInventory playerInv,
                                    ElectrolyzerTileEntity te) {
        super(new ElectrolyzerContainer(playerInv, te), playerInv, te);

        addSlotTextures((AdvancedContainer) inventorySlots);
        setDynamicBackground();
    }
}