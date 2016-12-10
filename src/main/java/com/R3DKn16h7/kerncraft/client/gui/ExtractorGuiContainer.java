package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.guicontainer.ExtractorContainer;
import com.R3DKn16h7.kerncraft.tileentities.ExtractorTileEntity;
import net.minecraft.inventory.IInventory;

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


        String s = this.playerInv.getDisplayName().getUnformattedText();
        Text titleText = new Text(this, borderLeft, 6, width, 6, Text.Alignment.MIDDLE);
        AddWidget(titleText, true);

        progressText = new Text(this, 18 * 5 + 2, 0, 30, 6, Text.Alignment.LEFT);
        AddWidget(progressText, true);
    }

    @Override
    public void updateWidgets() {
        super.updateWidgets();
        ExtractorTileEntity cast_te = (ExtractorTileEntity) te;

        flame.setPercentage(cast_te.getFuelStoredPercentage());
        //flame.addHoveringText();

        String perc = String.format("%.2f%%", cast_te.getProgressPerc() * 100);
        progressText.setText(perc);

        brewing.setPercentage(cast_te.getProgressPerc());
    }
}