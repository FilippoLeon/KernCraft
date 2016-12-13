package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.client.gui.widgets.*;
import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.MessageInt;
import com.R3DKn16h7.kerncraft.tileentities.LampTileEntity;
import net.minecraft.inventory.IInventory;

public class LampGui extends AdvancedGui {

    AnimatedTexturedElement flame;
    AnimatedTexturedElement brewing;
    Text progressText;

    public LampGui(IInventory playerInv, LampTileEntity te) {
        super(playerInv, te);

        setDynamicBackground(200, 60);

        BetterButton bt = StateButton.REDSTONE_MODE(this, te);
        AddWidget(bt);

        Text txt = new Text(this, getMiddle() - 10 - borderLeft - borderLeft / 2, 0, Widget.Alignment.MIDDLE);
        txt.setSize(20, 20);
        txt.setTooltip("Light level");
        txt.setText(Integer.toString(te.lightLevel));
        AddWidget(txt);

        Runnable r = () -> {
            te.receiveMessage(0);
            //KernCraftNetwork.networkWrapper.sendToAll(new MessageInt(0, te.getPos()));
            KernCraftNetwork.networkWrapper.sendToServer(new MessageInt(0, te.getPos()));
            txt.setText(Integer.toString(te.lightLevel));
        };
        Runnable r2 = () -> {
            te.receiveMessage(1);
            //KernCraftNetwork.networkWrapper.sendToAll(new MessageInt(0, te.getPos()));
            KernCraftNetwork.networkWrapper.sendToServer(new MessageInt(1, te.getPos()));
            txt.setText(Integer.toString(te.lightLevel));
        };

        BetterButton btb3 = new BetterButton(this, getMiddle() - 30 - borderLeft / 2, borderTop, 20, 20);
        btb3.setText("-")
                .setAlignment(Widget.Alignment.MIDDLE)
                .setOnClicked(r2);
        AddWidget(btb3);

        BetterButton btb2 = new BetterButton(this, 10 + getMiddle() - borderLeft / 2, borderTop, 20, 20);
        btb2.setText("+")
                .setAlignment(Widget.Alignment.MIDDLE)
                .setOnClicked(r);
        AddWidget(btb2);

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateWidgets() {
        super.updateWidgets();

    }
}