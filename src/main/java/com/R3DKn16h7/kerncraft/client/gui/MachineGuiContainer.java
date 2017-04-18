package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.client.gui.widgets.AnimatedTexturedElement;
import com.R3DKn16h7.kerncraft.client.gui.widgets.StateButton;
import com.R3DKn16h7.kerncraft.client.gui.widgets.Text;
import com.R3DKn16h7.kerncraft.tileentities.*;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.energy.IEnergyStorage;

import java.awt.*;

/**
 * Created by Filippo on 18-Apr-17.
 */
public class MachineGuiContainer extends AdvancedGuiContainer {
    protected AnimatedTexturedElement energyBar;
    protected AnimatedTexturedElement fluidBar;
    protected AnimatedTexturedElement flame;
    protected AnimatedTexturedElement brewing;
    protected Text progressText;

    public MachineGuiContainer(Container container, IInventory playerInv, MachineTileEntity te) {
        super(container, playerInv, te);

        energyBar = new AnimatedTexturedElement(this,
                 "kerncraft:textures/gui/container/extractor_gui.png", -1, -1,
                 6, 18 * 3 - 2, 176, 0, AnimatedTexturedElement.Direction.BOTTOM, 300);
        energyBar.setAutoAnimated(false, 0);
        AddWidget(energyBar, true);

        fluidBar = new AnimatedTexturedElement(this,
                "kerncraft:textures/gui/container/extractor_gui.png", -1 + 9, -1,
                6, 18 * 3 - 2, 176, 0, AnimatedTexturedElement.Direction.BOTTOM, 300);
        fluidBar.setTint(Color.blue);
        fluidBar.setAutoAnimated(false, 0);
        AddWidget(fluidBar, true);

        if(te instanceof IRedstoneSettable) {
            StateButton redstoneModeButton = StateButton.REDSTONE_MODE(this, ((IRedstoneSettable) te));
            redstoneModeButton.setState(((IRedstoneSettable) te).getRedstoneMode());
            AddWidget(redstoneModeButton, true);
        }
        if (te instanceof IFuelUser) {
            flame = AnimatedTexturedElement.FLAME(this, 18 * 1, 18 * 1);
            flame.setAutoAnimated(false, 0);
            flame.setTint(null);
            AddWidget(flame, true);
        }
        if (te instanceof IProgressMachine) {
            brewing = AnimatedTexturedElement.BREWING(this, 18 * 3 + 1, 18 * 1 + 8);
            brewing.setAutoAnimated(false, 0);
            brewing.setTint(null);
            AddWidget(brewing, true);

            progressText = new Text(this, 18 * 5 + 2,
                    0, 10, 6, Text.Alignment.LEFT);
            AddWidget(progressText, true);
        }

        String titleString;
        if (te.getDisplayName() != null) {
            titleString = te.getDisplayName().getUnformattedText();
        } else {
            titleString = "Inventory";
        }
        Text titleText = new Text(this, 0, -borderTop + 4,
                this.xSize - 2 * borderLeft, 6, Text.Alignment.MIDDLE);
        titleText.setText(titleString);
        AddWidget(titleText, true);
    }

    @Override
    public void updateWidgets() {
        super.updateWidgets();

        if (te instanceof IFuelUser) {
            float fuelStoredPercent = ((IFuelUser) te).getFuelStoredPercent();
            flame.setPercentage(fuelStoredPercent);
            flame.setTooltip(String.format("Fuel %.2f%%", fuelStoredPercent * 100));
        }

        if (te instanceof IProgressMachine) {
            IProgressMachine cast_te = ((IProgressMachine) te);
            String progressPercent = String.format("%.2f%%", cast_te.getProgressPercent() * 100);
            progressText.setText(progressPercent);
            String fancyProgressPercent = String.format("Progress %.2f%%", cast_te.getProgressPercent() * 100);
            brewing.setPercentage(cast_te.getProgressPercent());
            brewing.setTooltip(fancyProgressPercent);
        }

        if (te instanceof IEnergyContainer) {
            IEnergyContainer energy_te = ((IEnergyContainer) te);
            float energyStoredPrcent = (float) energy_te.getEnergyStored()
                    / energy_te.getMaxEnergyStored();
            String tooltip = String.format("Energy: %d/%d",
                    energy_te.getEnergyStored(), energy_te.getMaxEnergyStored());
            energyBar.setTooltip(tooltip);
            energyBar.setPercentage(energyStoredPrcent);
        }

        if (te instanceof IFluidStorage) {
            IFluidStorage fluid_te = ((IFluidStorage) te);
            float fluidAmountPercent = (float) fluid_te.getFluidAmount() / fluid_te.getCapacity();
            String name;
            if (fluid_te.getFluid() != null) {
                name = fluid_te.getFluid().getLocalizedName();
            } else {
                name = "Empty";
            }
            String tooltipFluid = String.format("%s: %d/%d",
                    name, fluid_te.getFluidAmount(), fluid_te.getCapacity());
            fluidBar.setTooltip(tooltipFluid);
            fluidBar.setPercentage(fluidAmountPercent);
        }
    }
}
