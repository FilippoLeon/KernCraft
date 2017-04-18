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

    AnimatedTexturedElement flame;

    public ChemicalFurnaceGuiContainer(IInventory playerInv,
                                       ChemicalFurnaceTileEntity te) {
        super(new ChemicalFurnaceContainer(playerInv, te), playerInv, te);

        addSlotTextures((AdvancedContainer) inventorySlots);

        setDynamicBackground(this.xSize, this.ySize);

        flame = AnimatedTexturedElement.FLAME(this, 18 * 4 + 9, 18 * 1);
        flame.setAutoAnimated(false, 0);
        AddWidget(flame, true);
    }

    private void addSlotTextures(AdvancedContainer inventorySlots) {
        for (Slot slot : inventorySlots.inventorySlots) {
            TexturedElement element = new TexturedElement(this,
                    "kerncraft:textures/gui/container/extractor_gui.png",
                    slot.xPos - 1, slot.yPos - 1,
                    AdvancedContainer.xSlotSize, AdvancedContainer.ySlotSize,
                    borderLeft + 18 * 1 - 2, borderTop + 18 * 2 - 2);
            AddWidget(element, false);
        }
    }

    @Override
    public void updateWidgets() {
        super.updateWidgets();
    }
}