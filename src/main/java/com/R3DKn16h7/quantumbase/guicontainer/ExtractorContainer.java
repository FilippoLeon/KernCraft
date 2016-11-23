package com.R3DKn16h7.quantumbase.guicontainer;

import com.R3DKn16h7.quantumbase.tileentities.ExtractorTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class ExtractorContainer extends Container {

    static public int xSlotSize = 18;
    static public int ySlotSize = 18;
    static public int bdLeft = 8;
    static public int bdTop = 17;
    private ExtractorTileEntity te;


    /***
     *
     * @param playerInv
     * @param te
     * ###########
     * # I C    R#
     * #         #
     * # F   OOOO#
     * ___________
     * #PPPPPPPPP#
     * #PPPPPPPPP#
     * #PPPPPPPPP#
     * -----------
     * #HHHHHHHHH#
     * ###########
     */
    public ExtractorContainer(IInventory playerInv, ExtractorTileEntity te) {
        this.te = te;

        int id = -1;

        // Input (I) ID 0
        this.addSlotToContainer(new Slot(te, ++id, bdLeft + xSlotSize, bdTop));
        // Catalyst (C) ID 1
        this.addSlotToContainer(new Slot(te, ++id, bdLeft + 3 * xSlotSize, bdTop));
        // Container (R) ID 2
        this.addSlotToContainer(new Slot(te, ++id, bdLeft + 8 * xSlotSize, bdTop));
        // Fuel slot (F) ID 3
        this.addSlotToContainer(new Slot(te, ++id,
                bdLeft + xSlotSize, bdTop + 2 * ySlotSize));
        // Output (O) ID 4-7
        int numOutputs = 4;
        for (int i = 0; i < numOutputs; ++i) {
            this.addSlotToContainer(new SingleItemSlot(te, ++id,
                    bdLeft + (5 + i) * xSlotSize, bdTop + 2 * ySlotSize));
        }

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

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.te.isUseableByPlayer(playerIn);
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean useEndIndex) {
        boolean success = false;
        int index = startIndex;

        if (useEndIndex)
            index = endIndex - 1;

        Slot slot;
        ItemStack stackinslot;

        if (stack.isStackable()) {
            while (stack.stackSize > 0 && (!useEndIndex && index < endIndex || useEndIndex && index >= startIndex)) {
                slot = this.inventorySlots.get(index);
                stackinslot = slot.getStack();

                if (stackinslot != null && stackinslot.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == stackinslot.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, stackinslot)) {
                    int l = stackinslot.stackSize + stack.stackSize;
                    int maxsize = Math.min(stack.getMaxStackSize(), slot.getItemStackLimit(stack));

                    if (l <= maxsize) {
                        stack.stackSize = 0;
                        stackinslot.stackSize = l;
                        slot.onSlotChanged();
                        success = true;
                    } else if (stackinslot.stackSize < maxsize) {
                        stack.stackSize -= stack.getMaxStackSize() - stackinslot.stackSize;
                        stackinslot.stackSize = stack.getMaxStackSize();
                        slot.onSlotChanged();
                        success = true;
                    }
                }

                if (useEndIndex) {
                    --index;
                } else {
                    ++index;
                }
            }
        }

        if (stack.stackSize > 0) {
            if (useEndIndex) {
                index = endIndex - 1;
            } else {
                index = startIndex;
            }

            while (!useEndIndex && index < endIndex || useEndIndex && index >= startIndex && stack.stackSize > 0) {
                slot = this.inventorySlots.get(index);
                stackinslot = slot.getStack();

                // Forge: Make sure to respect isItemValid in the slot.
                if (stackinslot == null && slot.isItemValid(stack)) {
                    if (stack.stackSize < slot.getItemStackLimit(stack)) {
                        slot.putStack(stack.copy());
                        stack.stackSize = 0;
                        success = true;
                        break;
                    } else {
                        ItemStack newstack = stack.copy();
                        newstack.stackSize = slot.getItemStackLimit(stack);
                        slot.putStack(newstack);
                        stack.stackSize -= slot.getItemStackLimit(stack);
                        success = true;
                    }
                }

                if (useEndIndex) {
                    --index;
                } else {
                    ++index;
                }
            }
        }

        return success;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
        ItemStack previous = null;
        Slot slot = this.inventorySlots.get(fromSlot);

        int numPlayerSlots = 36;

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();

            if (fromSlot < te.getSizeInventory()) {
                // We are shift clicking something in the tile entity
                if (!this.mergeItemStack(current,
                        te.getSizeInventory(), te.getSizeInventory() + numPlayerSlots, true))
                    return null;
            } else {
                // We are shift clicking something from the tile entity
                if (!this.mergeItemStack(current, 0, te.getSizeInventory(), false))
                    return null;
            }

            if (current.stackSize == 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();

            if (current.stackSize == previous.stackSize)
                return null;
            slot.onPickupFromSlot(playerIn, current);
        }
        return previous;
    }

    static public class SingleItemSlot extends Slot {

        public SingleItemSlot(IInventory inventory, int index, int xPosition, int yPosition) {
            super(inventory, index, xPosition, yPosition);
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }
    }
}
