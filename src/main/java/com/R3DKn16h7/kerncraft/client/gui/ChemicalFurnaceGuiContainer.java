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

public class ChemicalFurnaceGuiContainer extends AdvancedGuiContainer {

    AnimatedTexturedElement flame;
    AdvancedContainer container;

    public ChemicalFurnaceGuiContainer(IInventory playerInv,
                                       ChemicalFurnaceTileEntity te) {
        super(new ChemicalFurnaceContainer(playerInv, te), playerInv, te);

        addSlotTextures((AdvancedContainer) inventorySlots);

        setDynamicBackground(this.xSize, this.ySize);

        flame = AnimatedTexturedElement.FLAME(this, 18 * 4 + 9, 18 * 1);
        flame.setAutoAnimated(false, 0);
        AddWidget(flame, true);

        AnimatedTexturedElement energyBar = new AnimatedTexturedElement(this,
                "kerncraft:textures/gui/container/extractor_gui.png",
                -1, -1,
                6, 18 * 3 - 2, 176, 0,
                AnimatedTexturedElement.Direction.BOTTOM, 300);
        // energyBar.setTint(Color.blue);
        AddWidget(energyBar, true);

        String s;
        if (te.getDisplayName() != null)
            s = te.getDisplayName().getUnformattedText();
        else
            s = "Chemical furnace";
        Text titleText = new Text(this, 0, -borderTop + 4,
                this.xSize - 2 * borderLeft, 6, Text.Alignment.MIDDLE);
        titleText.setText(s);
        AddWidget(titleText, true);


        IntConsumer sdd = (int state) -> {
            te.setMode(state);
            KernCraftNetwork.networkWrapper.sendToAll(new MessageRedstoneControl(state, te.getPos()));
            KernCraftNetwork.networkWrapper.sendToServer(new MessageRedstoneControl(state, te.getPos()));
        };
        StateButton btb2 = StateButton.REDSTONE_MODE(this, te);
        AddWidget(btb2, true);

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
    public void initGui() {
        super.initGui();
    }

    @Override
    public void updateWidgets() {
        super.updateWidgets();
        ChemicalFurnaceTileEntity cast_te = (ChemicalFurnaceTileEntity) te;

        flame.setPercentage(cast_te.getFuelStoredPercentage());
        flame.setTooltip(String.format("Fuel %.2f%%", cast_te.getFuelStoredPercentage()));

        String perc = String.format("%.2f%%", cast_te.getProgressPerc() * 100);

        String perc2 = String.format("Progress %.2f%%", cast_te.getProgressPerc() * 100);
    }
}