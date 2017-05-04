package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.client.gui.widgets.*;
import com.R3DKn16h7.kerncraft.guicontainer.AdvancedContainer;
import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.MessageSideConfig;
import com.R3DKn16h7.kerncraft.tileentities.*;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.Tuple;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

/**
 * Created by Filippo on 18-Apr-17.
 */
public class MachineGuiContainer extends AdvancedGuiContainer {
    public static final int itemStackIconSize = 18;
    protected AnimatedTexturedElement energyBar;
    protected TexturedElement energyBarBackGround;
    protected List<AnimatedTexturedElement> fluidBar;
    protected List<TexturedElement> fluidBarBackground;
    protected AnimatedTexturedElement flame;
    protected AnimatedTexturedElement brewing;
    protected Text progressText;

    public MachineGuiContainer(Container container, IInventory playerInv, MachineTileEntity te) {
        super(container, playerInv, te);


        energyBarBackGround = TexturedElement.ENERGY_BAR_BACKGROUND(this,
                -2 + this.borderLeft, -2 + this.borderTop);
        AddWidget(energyBarBackGround, false);
        energyBar = AnimatedTexturedElement.ENERGY_BAR(this, -1, -1);
        energyBar.setAutoAnimated(false, 0);
        AddWidget(energyBar, true);

        fluidBar = new ArrayList<>(1);
        fluidBarBackground = new ArrayList<>(1);
        createFluidBars(1);

        if (te instanceof IRedstoneSettable) {
            StateButton redstoneModeButton = StateButton.REDSTONE_MODE(this,
                    ((IRedstoneSettable) te)
            );
            redstoneModeButton.setState(((IRedstoneSettable) te).getRedstoneMode());
            AddWidget(redstoneModeButton, true);
        }
        if (te instanceof IFuelUser) {
            IFuelUser fuel_te = ((IFuelUser) te);
            int[] pos = fuel_te.getFuelIconCoordinate();
            if (pos != null) {
                flame = AnimatedTexturedElement.FLAME(this,
                        gridCoord(pos[0]),
                        gridCoord(pos[1]));
                flame.setAutoAnimated(false, 0);
                flame.setTint(null);
                AddWidget(flame, true);
            }
        }
        if (te instanceof IProgressMachine) {
            IProgressMachine progr_te = ((IProgressMachine) te);
            Tuple<Integer[], ProgressIcon> progressInfo = progr_te.getProgressIconCoordinate();
            if (progressInfo != null) {
                switch (progressInfo.getSecond()) {
                    case BREWING:
                        brewing = AnimatedTexturedElement.BREWING(this,
                                gridCoord(progressInfo.getFirst()[0]) + 1,
                                gridCoord(progressInfo.getFirst()[1]) + 8);
                        break;
                    case ARROW_RIGHT:
                        brewing = AnimatedTexturedElement.ARROW(this,
                                gridCoord(progressInfo.getFirst()[0]),
                                gridCoord(progressInfo.getFirst()[1]));
                    case ARROW_DOWN:
                        brewing = AnimatedTexturedElement.ARROW_DOWN(this,
                                gridCoord(progressInfo.getFirst()[0]) - 2,
                                gridCoord(progressInfo.getFirst()[1]));
                }
                brewing.setAutoAnimated(false, 0);
                brewing.setTint(null);
                AddWidget(brewing, true);
            }
            int[] pos = progr_te.getProgressTextCoordinate();
            if (pos != null) {
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

    private void createFluidBars(int size) {
        if (fluidBar != null) {
            for (Widget wid : fluidBar) {
                RemoveWidget(wid);
            }
        }
        if (fluidBarBackground != null) {
            for (Widget wid : fluidBarBackground) {
                RemoveWidget(wid);
            }
        }

        fluidBar.clear();
        fluidBarBackground.clear();
        for (int i = 0; i < size; ++i) {
            fluidBarBackground.add(TexturedElement.ENERGY_BAR_BACKGROUND(this,
                    -2 + this.borderLeft + 9, -2
                            + this.borderTop + i * 3 * Widget.DEFAULT_SLOT_SIZE_Y / size));
            fluidBarBackground.get(i).setSize(8, 3 * Widget.DEFAULT_SLOT_SIZE_Y / size);
            fluidBarBackground.get(i).setDynamicSized();
            AddWidget(fluidBarBackground.get(i), false);
            fluidBar.add(AnimatedTexturedElement.ENERGY_BAR(this, -1 + 9, -1
                    + i * 3 * Widget.DEFAULT_SLOT_SIZE_Y / size - 1));
            fluidBar.get(i).setTint(Color.blue);
            fluidBar.get(i).setAutoAnimated(false, 0);
            fluidBar.get(i).setSize(6, 3 * Widget.DEFAULT_SLOT_SIZE_Y / size);
            AddWidget(fluidBar.get(i), true);
        }
    }

    private int gridCoord(int pos) {
        return pos >= 0 ? pos * itemStackIconSize : -pos * itemStackIconSize + itemStackIconSize / 2;
    }

    private void setupSideConfiguration() {
        if (!(te instanceof ISideConfigurable)) return;
        ISideConfigurable side_te = ((ISideConfigurable) te);

        // Side configuration button
        int I = 0;
        for (int inputIdx = 0; inputIdx < side_te.getInputSize(); ++inputIdx) {
            int[] inputCoord = side_te.getInputCoords()[inputIdx];
            int T1 = I;
            IntConsumer onSlotConfigurationChanged = (int state) -> {
                side_te.setSlotSide(T1, state);
                KernCraftNetwork.networkWrapper.sendToServer(new MessageSideConfig(T1, state, te.getPos()));
                System.out.println("Click! " + state);
            };
            StateButton btbX = new StateButton(this, inputCoord[0] * 18 + borderLeft - 3,
                    (inputCoord[1]) * 18 + borderTop - 4, 6, 6)
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
        for (int outputIdx = 0; outputIdx < side_te.getOutputSize(); ++outputIdx) {
            int[] outputCoord = side_te.getOutputCoords()[outputIdx];
            int T1 = I;
            IntConsumer onSlotConfigurationChanged = (int state) -> {
                side_te.setSlotSide(T1, state);
                KernCraftNetwork.networkWrapper.sendToServer(new MessageSideConfig(T1, state, te.getPos()));
                System.out.println("Click! " + state);
            };
            StateButton btbX = new StateButton(this, outputCoord[0] * 18 + borderLeft - 4,
                    (outputCoord[1]) * 18 + borderTop - 4, 6, 6)
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

        if (te instanceof IFuelUser && flame != null) {
            float fuelStoredPercent = ((IFuelUser) te).getFuelStoredPercent();
            flame.setPercentage(fuelStoredPercent);
            flame.setTooltip(String.format("Fuel %.2f%%", fuelStoredPercent * 100));
        }

        if (te instanceof IProgressMachine) {
            IProgressMachine cast_te = ((IProgressMachine) te);
            if (progressText != null) {
                String progressPercent = String.format("%.2f%%", cast_te.getProgressPercent() * 100);
                progressText.setText(progressPercent);
            }
            if (brewing != null) {
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
            // FIXME: display more fluids
            IFluidStorage fluid_te = ((IFluidStorage) te);
            if (fluid_te.getNumberOfTanks() != fluidBarBackground.size()) {
                createFluidBars(fluid_te.getNumberOfTanks());
            }
            for (int i = 0; i < fluid_te.getNumberOfTanks(); ++i) {
                float fluidAmountPercent = (float) fluid_te.getFluidAmount(i) / fluid_te.getCapacity(i);
                String name;
                if (fluid_te.getFluid(i) != null) {
                    name = fluid_te.getFluid(0).getLocalizedName();
                    if (name.equals("Water")) {
                        fluidBar.get(i).setTint(Color.blue);
                    } else if (name.equals("Lava")) {
                        fluidBar.get(i).setTint(Color.red);
                    } else {
                        fluidBar.get(i).setTint(new Color(fluid_te.getFluid(i).getFluid().getColor()));
                    }
                } else {
                    name = "Empty";
                }
                String tooltipFluid = String.format("%s: %d/%d",
                        name, fluid_te.getFluidAmount(i), fluid_te.getCapacity(i));
                fluidBar.get(i).setTooltip(tooltipFluid);
                fluidBar.get(i).setPercentage(fluidAmountPercent);
            }
        }
    }

    public enum ProgressIcon {BREWING, ARROW_DOWN, ARROW_LEFT, ARROW_RIGHT}
}
