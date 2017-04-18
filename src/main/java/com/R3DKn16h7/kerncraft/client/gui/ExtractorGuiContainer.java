package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.client.gui.widgets.AnimatedTexturedElement;
import com.R3DKn16h7.kerncraft.client.gui.widgets.StateButton;
import com.R3DKn16h7.kerncraft.client.gui.widgets.Text;
import com.R3DKn16h7.kerncraft.guicontainer.ExtractorContainer;
import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.MessageRedstoneControl;
import com.R3DKn16h7.kerncraft.tileentities.ExtractorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

import java.awt.*;
import java.util.function.IntConsumer;

public class ExtractorGuiContainer extends AdvancedGuiContainer {

    AnimatedTexturedElement flame;
    AnimatedTexturedElement brewing;
    Text progressText;
    AnimatedTexturedElement energyBar, fluidBar;

    public ExtractorGuiContainer(IInventory playerInv, ExtractorTileEntity te) {
        super(new ExtractorContainer(playerInv, te), playerInv, te);


//        te.getUpdateTag();
          te.getUpdatePacket();
//        Minecraft.getMinecraft().world.scheduleBlockUpdate(te.getPos(), te.getBlockType(),0,0);


        setBackground("kerncraft:textures/gui/container/extractor_gui.png",
                0, 0, this.xSize, this.ySize);

        flame = AnimatedTexturedElement.FLAME(this, 18 * 1, 18 * 1);
        flame.setAutoAnimated(false, 0);
        AddWidget(flame, true);

        brewing = AnimatedTexturedElement.BREWING(this, 18 * 3 + 1, 18 * 1 + 8);
        brewing.setAutoAnimated(false, 0);
        AddWidget(brewing, true);


       energyBar = new AnimatedTexturedElement(this,
                "kerncraft:textures/gui/container/extractor_gui.png", -1, -1,
                6, 18 * 3 - 2, 176, 0, AnimatedTexturedElement.Direction.BOTTOM, 300);
        // energyBar.setTint(Color.blue);
        energyBar.setAutoAnimated(false, 0);
        AddWidget(energyBar, true);

        fluidBar = new AnimatedTexturedElement(this,
                "kerncraft:textures/gui/container/extractor_gui.png", -1 + 9, -1,
                6, 18 * 3 - 2, 176, 0, AnimatedTexturedElement.Direction.BOTTOM, 300);
        fluidBar.setTint(Color.blue);
        fluidBar.setAutoAnimated(false, 0);
        AddWidget(fluidBar, true);

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
        IntConsumer sdd = (int state) -> {
            te.setMode(state);
            KernCraftNetwork.networkWrapper.sendToAll(new MessageRedstoneControl(state, te.getPos()));
            KernCraftNetwork.networkWrapper.sendToServer(new MessageRedstoneControl(state, te.getPos()));
        };

        StateButton btb2 = StateButton.REDSTONE_MODE(this, te);
        btb2.setState(te.getRedstoneMode());
        AddWidget(btb2, true);

        // Side configuration button
//        for(Con)
        for(int[] ish: te.inputCoords) {
            IntConsumer onSlotConfigurationChanged = (int state) -> {
                System.out.println("Click! " + state);
            };
            StateButton btbX = new StateButton(this, ish[0] * 23, ish[1] * 23, 6, 6)
                    .addState(new StateButton.State().setTooltip("Front").setTint(Color.red))
                    .addState(new StateButton.State().setTooltip("Back").setTint(Color.blue))
                    .addState(new StateButton.State().setTooltip("Top").setTint(Color.cyan))
                    .addState(new StateButton.State().setTooltip("Bottom").setTint(Color.magenta))
                    .addState(new StateButton.State().setTooltip("Left").setTint(Color.green))
                    .addState(new StateButton.State().setTooltip("Right").setTint(Color.orange))
                    .addOnStateChanged(onSlotConfigurationChanged);
            AddWidget(btbX, true);
        }

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

        float perc1 = (float) cast_te.storage.getEnergyStored()
                / cast_te.storage.getMaxEnergyStored();
        String tooltip = String.format("Energy: %d/%d",
                cast_te.storage.getEnergyStored(), cast_te.storage.getMaxEnergyStored());
        energyBar.setTooltip(tooltip);
        energyBar.setPercentage(perc1);

        float perc3 = (float) cast_te.tank.getFluidAmount() / cast_te.tank.getCapacity();
        String name;
        if(cast_te.tank.getFluid() != null) {
            name = cast_te.tank.getFluid().getLocalizedName();
        } else {
            name = "Empty";
        }
        String tooltip1 = String.format("%s: %d/%d",
                name,
                cast_te.tank.getFluidAmount(), cast_te.tank.getCapacity());
        fluidBar.setTooltip(tooltip1);
        fluidBar.setPercentage(perc3);

        String perc2 = String.format("Progress %.2f%%", cast_te.getProgressPerc() * 100);
        brewing.setPercentage(cast_te.getProgressPerc());
        brewing.setTooltip(perc2);
    }
}