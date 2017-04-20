package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.client.gui.widgets.AnimatedTexturedElement;
import com.R3DKn16h7.kerncraft.client.gui.widgets.StateButton;
import com.R3DKn16h7.kerncraft.client.gui.widgets.Text;
import com.R3DKn16h7.kerncraft.client.gui.widgets.TexturedElement;
import com.R3DKn16h7.kerncraft.guicontainer.AdvancedContainer;
import com.R3DKn16h7.kerncraft.guicontainer.ChemicalFurnaceContainer;
import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.MessageRedstoneControl;
import com.R3DKn16h7.kerncraft.tileentities.ChemicalFurnaceTileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

import java.util.function.IntConsumer;

public class ChemicalFurnaceGuiContainer extends MachineGuiContainer {
    public ChemicalFurnaceGuiContainer(IInventory playerInv,
                                       ChemicalFurnaceTileEntity te) {
        super(new ChemicalFurnaceContainer(playerInv, te), playerInv, te);

        addSlotTextures((AdvancedContainer) inventorySlots);

        setDynamicBackground();
    }
}