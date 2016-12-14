package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.client.gui.widgets.AnimatedTexturedElement;
import com.R3DKn16h7.kerncraft.client.gui.widgets.StateButton;
import com.R3DKn16h7.kerncraft.client.gui.widgets.Text;
import com.R3DKn16h7.kerncraft.guicontainer.ChemicalFurnaceContainer;
import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.MessageRedstoneControl;
import com.R3DKn16h7.kerncraft.tileentities.ChemicalFurnaceTileEntity;
import com.R3DKn16h7.kerncraft.tileentities.ExtractorTileEntity;
import net.minecraft.inventory.IInventory;

import java.awt.*;
import java.util.function.IntConsumer;

public class ChemicalFurnaceGuiContainer extends AdvancedGuiContainer {

    AnimatedTexturedElement flame;
    Text progressText;

    public ChemicalFurnaceGuiContainer(IInventory playerInv,
                                       ChemicalFurnaceTileEntity te) {
        super(new ChemicalFurnaceContainer(playerInv, te), playerInv, te);

        setBackground("kerncraft:textures/gui/container/extractor_gui.png",
                0, 0, this.xSize, this.ySize);

        flame = AnimatedTexturedElement.FLAME(this, 18 * 1, 18 * 1);
        flame.setAutoAnimated(false, 0);
        AddWidget(flame, true);

        AnimatedTexturedElement energyBar = new AnimatedTexturedElement(this,
                "kerncraft:textures/gui/container/extractor_gui.png", -1, -1,
                6, 18 * 3 - 2, 176, 0, AnimatedTexturedElement.Direction.BOTTOM, 300);
        // energyBar.setTint(Color.blue);
        AddWidget(energyBar, true);

        String s;
        if (te.getDisplayName() != null)
            s = te.getDisplayName().getUnformattedText();
        else
            s = "Extractor";

        Text titleText = new Text(this, 0, -borderTop + 4,
                this.xSize - 2 * borderLeft, 6, Text.Alignment.MIDDLE);
        titleText.setText(s);
        AddWidget(titleText, true);

        progressText = new Text(this, 18 * 5 + 2, 0, 10, 6, Text.Alignment.LEFT);
        AddWidget(progressText, true);

        int i = 0;
        Runnable r = () -> {
            //System.out.println("Click!");
        };
        IntConsumer sdd = (int state) -> {
            te.setMode(state);
            KernCraftNetwork.networkWrapper.sendToAll(new MessageRedstoneControl(state, te.getPos()));
            KernCraftNetwork.networkWrapper.sendToServer(new MessageRedstoneControl(state, te.getPos()));
        };

        StateButton btb2 = StateButton.REDSTONE_MODE(this, te);
        AddWidget(btb2, true);

        // Side configuration button
        StateButton btbX = new StateButton(this, 23, 48, 6, 6)
                .addState(new StateButton.State().setTooltip("Front").setTint(Color.red));

        AddWidget(btbX, true);
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void updateWidgets() {
        super.updateWidgets();
        ExtractorTileEntity cast_te = (ExtractorTileEntity) te;

        flame.setPercentage(cast_te.getFuelStoredPercentage());
        flame.setTooltip(String.format("Fuel %.2f%%", cast_te.getFuelStoredPercentage()));

        String perc = String.format("%.2f%%", cast_te.getProgressPerc() * 100);
        progressText.setText(perc);


        String perc2 = String.format("Progress %.2f%%", cast_te.getProgressPerc() * 100);
    }
}