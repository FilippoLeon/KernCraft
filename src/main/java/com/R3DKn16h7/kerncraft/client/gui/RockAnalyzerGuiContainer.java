package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.guicontainer.AdvancedContainer;
import com.R3DKn16h7.kerncraft.guicontainer.RockAnalyzerContainer;
import com.R3DKn16h7.kerncraft.tileentities.machines.RockAnalyzerTileEntity;
import net.minecraft.inventory.IInventory;

public class RockAnalyzerGuiContainer extends MachineGuiContainer {
    public RockAnalyzerGuiContainer(IInventory playerInv,
                                    RockAnalyzerTileEntity te) {
        super(new RockAnalyzerContainer(playerInv, te), playerInv, te);

        addSlotTextures((AdvancedContainer) inventorySlots);
        setDynamicBackground();
    }
}