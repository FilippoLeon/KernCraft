package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.client.gui.widgets.AnimatedTexturedElement;
import com.R3DKn16h7.kerncraft.client.gui.widgets.StateButton;
import com.R3DKn16h7.kerncraft.client.gui.widgets.Text;
import com.R3DKn16h7.kerncraft.client.gui.widgets.TexturedElement;
import com.R3DKn16h7.kerncraft.guicontainer.AdvancedContainer;
import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.MessageSideConfig;
import com.R3DKn16h7.kerncraft.tileentities.*;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

import java.awt.*;
import java.util.function.IntConsumer;

/**
 * Created by Filippo on 18-Apr-17.
 */
public class MachineGuiContainer extends AdvancedGuiContainer {
    public static final int itemStackIconSize = 18;
    protected AnimatedTexturedElement energyBar;
    protected AnimatedTexturedElement fluidBar;
    protected AnimatedTexturedElement flame;
    protected AnimatedTexturedElement brewing;
    protected Text progressText;

    public MachineGuiContainer(Container container, IInventory playerInv, MachineTileEntity te) {
        super(container, playerInv, te);

        energyBar = new AnimatedTexturedElement(this,
                 "kerncraft:textures/gui/container/extractor_gui.png",
                 -1, -1,
                 6,  gridCoord(3) - 2,
                 176, 0,
                AnimatedTexturedElement.Direction.BOTTOM, 300);
        energyBar.setAutoAnimated(false, 0);
        AddWidget(energyBar, true);

        fluidBar = new AnimatedTexturedElement(this,
                "kerncraft:textures/gui/container/extractor_gui.png",
                -1 + 9, -1,
                6,  gridCoord(3) - 2,
                176, 0,
                AnimatedTexturedElement.Direction.BOTTOM, 300);
        fluidBar.setTint(Color.blue);
        fluidBar.setAutoAnimated(false, 0);
        AddWidget(fluidBar, true);

        if(te instanceof IRedstoneSettable) {
            StateButton redstoneModeButton = StateButton.REDSTONE_MODE(this,
                    ((IRedstoneSettable) te)
            );
            redstoneModeButton.setState(((IRedstoneSettable) te).getRedstoneMode());
            AddWidget(redstoneModeButton, true);
        }
        if (te instanceof IFuelUser) {
            IFuelUser fuel_te = ((IFuelUser) te);
            int[] pos = fuel_te.getFuelIconCoordinate();
            flame = AnimatedTexturedElement.FLAME(this,
                    gridCoord(pos[0]),
                    gridCoord(pos[1]));
            flame.setAutoAnimated(false, 0);
            flame.setTint(null);
            AddWidget(flame, true);
        }
        if (te instanceof IProgressMachine) {
            IProgressMachine progr_te = ((IProgressMachine) te);
            int[] pos = progr_te.getProgressIconCoordinate();
            if(pos != null) {
                brewing = AnimatedTexturedElement.BREWING(this,
                        gridCoord(pos[0]) + 1,
                        gridCoord(pos[1]) + 8);
                brewing.setAutoAnimated(false, 0);
                brewing.setTint(null);
                AddWidget(brewing, true);
            }
            pos = progr_te.getProgressTextCoordinate();
            if(pos != null) {
                progressText = new Text(this,
                        gridCoord(pos[0]),
                        gridCoord(pos[1]) + 3,
                        10, 6, Text.Alignment.LEFT);
                AddWidget(progressText, true);
            }
        }

        // TODO:
        // 1. draw energy and water bar backgrounds
        // 2. draw slot config
        // 3. draw slot textures

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

        setupSideConfiguration();
    }

    private int gridCoord(int pos) {
        return pos >= 0 ? pos * itemStackIconSize : -pos * itemStackIconSize + itemStackIconSize / 2;
    }

    private void setupSideConfiguration() {
        if(!(te instanceof ISideConfigurable)) return;
        ISideConfigurable side_te = ((ISideConfigurable) te);

        // Side configuration button
        int I = 0;
        for(int[] ish: side_te.getInputCoords()) {
            int T1 = I;
            IntConsumer onSlotConfigurationChanged = (int state) -> {
                side_te.setSlotSide(T1, state);
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
            btbX.setState(side_te.getSideConfig().getSlotSide(I++).getValue());
            AddWidget(btbX, true);
        }
        for(int[] ish: side_te.getOutputCoords()) {
            int T1 = I;
            IntConsumer onSlotConfigurationChanged = (int state) -> {
                side_te.setSlotSide(T1, state);
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
            btbX.setState(side_te.getSideConfig().getSlotSide(I++).getValue());
            AddWidget(btbX, true);
        }
    }

    protected void addSlotTextures(AdvancedContainer container) {
        for (Slot slot : container.inventorySlots) {
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

        if (te instanceof IFuelUser) {
            float fuelStoredPercent = ((IFuelUser) te).getFuelStoredPercent();
            flame.setPercentage(fuelStoredPercent);
            flame.setTooltip(String.format("Fuel %.2f%%", fuelStoredPercent * 100));
        }

        if (te instanceof IProgressMachine) {
            IProgressMachine cast_te = ((IProgressMachine) te);
            if(progressText != null) {
                String progressPercent = String.format("%.2f%%", cast_te.getProgressPercent() * 100);
                progressText.setText(progressPercent);
            }
            if(brewing != null) {
                String fancyProgressPercent = String.format("Progress %.2f%%", cast_te.getProgressPercent() * 100);
                brewing.setPercentage(cast_te.getProgressPercent());
                brewing.setTooltip(fancyProgressPercent);
            }
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
                if (!name.equals("Water") && !name.equals("Lava")) {
                    fluidBar.setTint(new Color(fluid_te.getFluid().getFluid().getColor()));
                }
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
