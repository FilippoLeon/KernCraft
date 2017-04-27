package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.guicontainer.AdvancedContainer;
import com.R3DKn16h7.kerncraft.guicontainer.ChemicalFurnaceContainer;
import com.R3DKn16h7.kerncraft.tileentities.ChemicalFurnaceTileEntity;
import net.minecraft.inventory.IInventory;

public class ChemicalFurnaceGuiContainer extends MachineGuiContainer {
    public ChemicalFurnaceGuiContainer(IInventory playerInv,
                                       ChemicalFurnaceTileEntity te) {
        super(new ChemicalFurnaceContainer(playerInv, te), playerInv, te);

        addSlotTextures((AdvancedContainer) inventorySlots);
        setDynamicBackground();
    }
}