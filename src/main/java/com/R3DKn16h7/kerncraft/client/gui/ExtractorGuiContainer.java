package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.client.gui.widgets.AnimatedTexturedElement;
import com.R3DKn16h7.kerncraft.client.gui.widgets.StateButton;
import com.R3DKn16h7.kerncraft.client.gui.widgets.Text;
import com.R3DKn16h7.kerncraft.client.gui.widgets.Widget;
import com.R3DKn16h7.kerncraft.guicontainer.ExtractorContainer;
import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.MessageRedstoneControl;
import com.R3DKn16h7.kerncraft.tileentities.ExtractorTileEntity;
import net.minecraft.inventory.IInventory;

import java.util.function.IntConsumer;

public class ExtractorGuiContainer extends AdvancedGuiContainer {

    AnimatedTexturedElement flame;
    AnimatedTexturedElement brewing;
    Text progressText;

    public ExtractorGuiContainer(IInventory playerInv, ExtractorTileEntity te) {
        super(new ExtractorContainer(playerInv, te), playerInv, te);

        setBackground("kerncraft:textures/gui/container/extractor_gui.png",
                0, 0, this.xSize, this.ySize);

        flame = AnimatedTexturedElement.FLAME(this, 18 * 1, 18 * 1);
        flame.setAutoAnimated(false, 0);
        AddWidget(flame, true);

        brewing = AnimatedTexturedElement.BREWING(this, 18 * 3 + 1, 18 * 1 + 8);
        brewing.setAutoAnimated(false, 0);
        AddWidget(brewing, true);


        AnimatedTexturedElement energyBar = new AnimatedTexturedElement(this,
                "kerncraft:textures/gui/container/extractor_gui.png", -1, -1,
                6, 18 * 3 - 2, 176, 0, AnimatedTexturedElement.Direction.BOTTOM, 300);
        // energyBar.setTint(Color.blue);
        AddWidget(energyBar, true);

        String s = this.playerInv.getDisplayName().getUnformattedText();
        Text titleText = new Text(this, 0, -borderTop + 4,
                this.xSize - 2 * borderLeft, 6, Text.Alignment.MIDDLE);
        titleText.setText(s);
        AddWidget(titleText, true);

        progressText = new Text(this, 18 * 5 + 2, 0, 10, 6, Text.Alignment.LEFT);
        AddWidget(progressText, true);

//        BetterButton btb = new BetterButton(this, 0,0,20,20)
//                .setText("")
//                .setAlignment(Widget.Alignment.MIDDLE)
//                // .setTint(Color.black)
//                .setIcon("kerncraft:textures/gui/widgets.png", 16*2, 16*7, 16, 16);
//        AddWidget(btb, true);

        int i = 0;
        Runnable r = () -> {
            System.out.println("Click!");
        };
        IntConsumer sdd = (int state) -> {
            te.setMode(state);
            KernCraftNetwork.networkWrapper.sendToAll(new MessageRedstoneControl(state, te.getPos()));
            KernCraftNetwork.networkWrapper.sendToServer(new MessageRedstoneControl(state, te.getPos()));
        };

        StateButton btb2 = new StateButton(this, -20, 20, 20, 20);
        btb2.setText("")
                .setAlignment(Widget.Alignment.MIDDLE)
                .setOnClicked(r);
        btb2.addState(new StateButton.State("kerncraft:textures/gui/widgets.png",
                "", "Active with signal", 16 * 4, 16 * 7))
                .addState(new StateButton.State("kerncraft:textures/gui/widgets.png",
                        "", "Active without signal", 16 * 5, 16 * 7))
                .addState(new StateButton.State("kerncraft:textures/gui/widgets.png",
                        "", "Ignore", 16 * 2, 16 * 7))
                .addOnStateChanged(sdd);
        AddWidget(btb2, true);
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
        brewing.setPercentage(cast_te.getProgressPerc());
        brewing.setTooltip(perc2);
    }
}