package com.R3DKn16h7.kerncraft.guicontainer;

import com.R3DKn16h7.kerncraft.tileentities.ExtractorTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;


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
     * # I LAB_BOOTS    R#
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

        IItemHandler input = te.getInput();
        IItemHandler output = te.getOutput();

        int id = -1;
        // Input (I) ID 0
        this.addSlotToContainer(new SlotItemHandler(input, ++id, bdLeft + xSlotSize, bdTop));
        // Catalyst (LAB_BOOTS) ID 1
        this.addSlotToContainer(new SlotItemHandler(input, ++id, bdLeft + 3 * xSlotSize, bdTop));
        // Container (R) ID 2
        this.addSlotToContainer(new SlotItemHandler(input, ++id, bdLeft + 8 * xSlotSize, bdTop));
        // Fuel slot (F) ID 3
        this.addSlotToContainer(new SlotItemHandler(input, ++id,
                bdLeft + xSlotSize, bdTop + 2 * ySlotSize));
        // Output (O) ID 4-7
        int numOutputs = 4;
        for (int i = 0; i < numOutputs; ++i) {
            this.addSlotToContainer(new SingleItemSlotItemHandler(output, i,
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

    private static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB) {
        return stackB.getItem() == stackA.getItem() &&
                (!stackA.getHasSubtypes() || stackA.getMetadata() == stackB.getMetadata()) &&
                ItemStack.areItemStackTagsEqual(stackA, stackB);
    }

//    @Override
//    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean useEndIndex) {
//        boolean success = false;
//        int index = startIndex;
//
//        if (useEndIndex)
//            index = endIndex - 1;
//
//        Slot slot;
//        ItemStack stackinslot;
//
//        if (stack.isStackable()) {
//            while (stack.stackSize > 0 && (!useEndIndex && index < endIndex || useEndIndex && index >= startIndex)) {
//                slot = this.inventorySlots.get(index);
//                stackinslot = slot.getStack();
//
//                if (stackinslot != null && stackinslot.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == stackinslot.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, stackinslot)) {
//                    int l = stackinslot.stackSize + stack.stackSize;
//                    int maxsize = Math.min(stack.getMaxStackSize(), slot.getItemStackLimit(stack));
//
//                    if (l <= maxsize) {
//                        stack.stackSize = 0;
//                        stackinslot.stackSize = l;
//                        slot.onSlotChanged();
//                        success = true;
//                    } else if (stackinslot.stackSize < maxsize) {
//                        stack.stackSize -= stack.getMaxStackSize() - stackinslot.stackSize;
//                        stackinslot.stackSize = stack.getMaxStackSize();
//                        slot.onSlotChanged();
//                        success = true;
//                    }
//                }
//
//                if (useEndIndex) {
//                    --index;
//                } else {
//                    ++index;
//                }
//            }
//        }
//
//        if (stack.stackSize > 0) {
//            if (useEndIndex) {
//                index = endIndex - 1;
//            } else {
//                index = startIndex;
//            }
//
//            while (!useEndIndex && index < endIndex || useEndIndex && index >= startIndex && stack.stackSize > 0) {
//                slot = this.inventorySlots.get(index);
//                stackinslot = slot.getStack();
//
//                // Forge: Make sure to respect isItemValid in the slot.
//                if (stackinslot == null && slot.isItemValid(stack)) {
//                    if (stack.stackSize < slot.getItemStackLimit(stack)) {
//                        slot.putStack(stack.copy());
//                        stack.stackSize = 0;
//                        success = true;
//                        break;
//                    } else {
//                        ItemStack newstack = stack.copy();
//                        newstack.stackSize = slot.getItemStackLimit(stack);
//                        slot.putStack(newstack);
//                        stack.stackSize -= slot.getItemStackLimit(stack);
//                        success = true;
//                    }
//                }
//
//                if (useEndIndex) {
//                    --index;
//                } else {
//                    ++index;
//                }
//            }
//        }
//
//        if(success) {
//            te.inputChanged = true;
//        }
//
//        return success;
//    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    /**
     * Merges provided ItemStack with the first avaliable one in the container/player inventor between minIndex
     * (included) and maxIndex (excluded). Args : stack, minIndex, maxIndex, negativDirection. /!\ the Container
     * implementation do not check if the item is valid for the slot
     */
    protected boolean mergeItemStack(ItemStack stack,
                                     int startIndex, int endIndex,
                                     boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;

        if (reverseDirection) {
            i = endIndex - 1;
        }

        if (stack.isStackable()) {
            while (stack.stackSize > 0 &&
                    (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)) {
                // Try and fill this slot
                Slot slot = this.inventorySlots.get(i);
                // Current stack in tested slot
                ItemStack itemstack = slot.getStack();
                // Is slot non-empty but can accept some of those items
                if (itemstack != null && areItemStacksEqual(stack, itemstack)) {
                    int j = itemstack.stackSize + stack.stackSize;

                    int max_stack_size = Math.min(stack.getMaxStackSize(), slot.getSlotStackLimit());
                    // If total of items can fit in slot, then just do that
                    if (j <= max_stack_size) {
                        stack.stackSize = 0;
                        itemstack.stackSize = j;
                        slot.onSlotChanged();
                        flag = true;
                        break;
                    // If total of items cannot completely fill slot, do as much as possible
                    } else if (itemstack.stackSize < stack.getMaxStackSize()) {
                        stack.stackSize -= max_stack_size - itemstack.stackSize;
                        itemstack.stackSize = max_stack_size;
                        slot.onSlotChanged();
                        flag = true;
                    }
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        // Still left over items? Fill empty slots
        if (stack.stackSize > 0) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while (!reverseDirection && i < endIndex ||
                    reverseDirection && i >= startIndex) {
                // Test a new slot
                Slot slot1 = this.inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();

                // Only if slot is empty or can place item in slot
                if (itemstack1 == null && slot1.isItemValid(stack)) {
                    // If total of items can fit in slot, then just do that
                    if (stack.stackSize <= slot1.getSlotStackLimit()) {
                        slot1.putStack(stack.copy());
                        slot1.onSlotChanged();
                        stack.stackSize = 0;
                        stack = null;
                        flag = true;
                        break;
                    // If total of items cannot completely fill slot, do as much as possible
                    } else {
                        ItemStack portion = stack.copy();
                        portion.stackSize = slot1.getSlotStackLimit();
                        slot1.putStack(portion.copy());
                        slot1.onSlotChanged();
                        stack.stackSize -= slot1.getSlotStackLimit();
                        if (stack.stackSize == 0) stack = null;
                    }
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        te.inputChanged = true;

        return flag;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
        ItemStack previous = null;
        Slot slot = this.inventorySlots.get(fromSlot);

        int numPlayerSlots = 36;

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();

            if (fromSlot < 8) {
                // We are shift clicking something in the tile entity
                if (!this.mergeItemStack(current,
                        8, 8 + numPlayerSlots, true))
                    return null;
            } else {
                // We are shift clicking something from the tile entity
                if (!this.mergeItemStack(current, 0, 8, false))
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



    @Override
    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {

    }

    static public class SingleItemSlotItemHandler extends SlotItemHandler {

        public SingleItemSlotItemHandler(IItemHandler inventory,
                                         int index, int xPosition, int yPosition) {
            super(inventory, index, xPosition, yPosition);
        }

        @Override
        public int getSlotStackLimit() {

            return 1;
        }

        @Override
        public void onSlotChanged() {

        }
    }
}
