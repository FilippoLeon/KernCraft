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

        // Side configuration button
        int I = 0;
        for(int[] ish: te.inputCoords) {
            int T1 = I;
            IntConsumer onSlotConfigurationChanged = (int state) -> {
                te.setSlotSide(T1, state);
                KernCraftNetwork.networkWrapper.sendToServer(new MessageSideConfig(T1, state, te.getPos()));
                System.out.println("Click! " + state);
            };
            StateButton btbX = new StateButton(this, ish[0] *18 + borderLeft - 3,
                    (ish[1] -1) * 18 + borderTop - 4, 6, 6)
                    .addState(new StateButton.State().setTooltip("Side: Front").setTint(Color.red))
                    .addState(new StateButton.State().setTooltip("Side: Right").setTint(Color.orange))
                    .addState(new StateButton.State().setTooltip("Side: Back").setTint(Color.blue))
                    .addState(new StateButton.State().setTooltip("Side: Left").setTint(Color.green))
                    .addState(new StateButton.State().setTooltip("Side: Top").setTint(Color.cyan))
                    .addState(new StateButton.State().setTooltip("Side: Bottom").setTint(Color.magenta))
                    .addState(new StateButton.State().setTooltip("Side: Disabled").setTint(Color.black))
                    .addOnStateChanged(onSlotConfigurationChanged);
            btbX.setState(te.sideConfig.getSlotSide(I++).getValue());
            AddWidget(btbX, true);
        }
        for(int[] ish: te.outputCoords) {
            int T1 = I;
            IntConsumer onSlotConfigurationChanged = (int state) -> {
                te.setSlotSide(T1, state);
                KernCraftNetwork.networkWrapper.sendToServer(new MessageSideConfig(T1,  state, te.getPos()));
                System.out.println("Click! " + state);
            };
            StateButton btbX = new StateButton(this, ish[0] * 18 + borderLeft - 4,
                    (ish[1] - 1) * 18 + borderTop - 4, 6, 6)
                    .addState(new StateButton.State().setTooltip("Side: Front").setTint(Color.red))
                    .addState(new StateButton.State().setTooltip("Side: Right").setTint(Color.orange))
                    .addState(new StateButton.State().setTooltip("Side: Back").setTint(Color.blue))
                    .addState(new StateButton.State().setTooltip("Side: Left").setTint(Color.green))
                    .addState(new StateButton.State().setTooltip("Side: Top").setTint(Color.cyan))
                    .addState(new StateButton.State().setTooltip("Side: Bottom").setTint(Color.magenta))
                    .addState(new StateButton.State().setTooltip("Side: Disabled").setTint(Color.black))
                    .addOnStateChanged(onSlotConfigurationChanged);
            btbX.setState(te.sideConfig.getSlotSide(I++).getValue());
            AddWidget(btbX, true);
        }

    }

    @Override
    public void updateWidgets() {
        super.updateWidgets();
    }
}