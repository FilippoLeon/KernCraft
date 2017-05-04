package com.R3DKn16h7.kerncraft.guicontainer;

import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.MessageFluidStackSync;
import com.R3DKn16h7.kerncraft.tileentities.SmeltingTileEntity;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Created by Filippo on 18-Apr-17.
 */
public class SmeltingContainer extends MachineContainer<SmeltingTileEntity> {
    // Fields for detect and send changes
    public static final int FIELDS = 5;
    public static final int FUEL_ID = 0;
    public static final int PROGRESS_ID = 1;
    public static final int FLUID_AMOUNT = 2;
    public static final int ENERGY = 3;
    public static final int REDSTONE_MODE = 4;
    int[] params = new int[FIELDS];
    int[] side_params;

    public SmeltingContainer(IInventory playerInv, SmeltingTileEntity te) {
        super(playerInv, te);

        side_params = new int[te.sideConfig.getSize()];
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i) {
            IContainerListener icontainerlistener = this.listeners.get(i);

            for (int j = 0; j < FIELDS; ++j) {
                if (this.params[j] != this.te.getField(j)) {
                    icontainerlistener.sendProgressBarUpdate(this, j, this.te.getField(j));
                }
            }
            for (int j = 0; j < te.sideConfig.getSize(); ++j) {
                if (this.side_params[j] != this.te.getField(-j)) {
                    icontainerlistener.sendProgressBarUpdate(this, -j, this.te.getField(-j));
                }
            }
        }

        for (int i = 0; i < FIELDS; ++i) {
            this.params[i] = this.te.getField(i);
        }
        for (int i = 0; i < te.sideConfig.getSize(); ++i) {
            this.side_params[i] = this.te.getField(-i);
        }

        KernCraftNetwork.networkWrapper.sendToAll(
                new MessageFluidStackSync(te.tank, te.getPos())
        );
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        this.te.setField(id, data);
    }

    protected void addPlayerSlots(IInventory playerInv) {
        // Player Inventory Slot 9-35, ID 9-35
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9,
                        bdLeft + x * xSlotSize, 84 + y * xSlotSize));
            }
        }

        // Player Hotbar Slot 0-8, ID 36-44
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(playerInv, x,
                    bdLeft + x * xSlotSize, 142));
        }
    }

    protected void addMachineSlots() {
        IItemHandler input = te.getInput();
        IItemHandler output = te.getOutput();

        for (int i = 0; i < te.getInputCoords().length; ++i) {
            this.addSlotToContainer(
                    createSlotItemHandler(input, i, ItemHandlerCategory.Input,
                            bdLeft + te.getInputCoords()[i][0] * xSlotSize,
                            bdTop + te.getInputCoords()[i][1] * ySlotSize)
            );
        }
        for (int i = 0; i < te.getOutputCoords().length; ++i) {
            this.addSlotToContainer(
                    createSlotItemHandler(output, i, ItemHandlerCategory.Output,
                            bdLeft + te.getOutputCoords()[i][0] * xSlotSize,
                            bdTop + te.getOutputCoords()[i][1] * ySlotSize)
            );
        }
    }

    public SlotItemHandler createSlotItemHandler(IItemHandler itemHandler,
                                                 int slotId, ItemHandlerCategory category,
                                                 int coordX, int coordY) {
        return new AdvancedSlotItemHandler(this,
                itemHandler, slotId, coordX, coordY, 64);
    }

    public enum ItemHandlerCategory {Input, Output}
}
