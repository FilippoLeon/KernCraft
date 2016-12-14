package com.R3DKn16h7.kerncraft.guicontainer;

import com.R3DKn16h7.kerncraft.tileentities.ChemicalFurnaceTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;


public class ChemicalFurnaceContainer extends AdvancedContainer {

    private ChemicalFurnaceTileEntity te;

    /***
     *
     * @param playerInv
     * @param te
     * ###########
     * # I L    R#
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
    public ChemicalFurnaceContainer(IInventory playerInv,
                                    ChemicalFurnaceTileEntity te) {
        this.te = te;

        IItemHandler input = te.getInput();
        IItemHandler output = te.getOutput();

        int numInputs = 2;
        for (int i = 0; i < numInputs; ++i) {
            this.addSlotToContainer(new AdvancedSlotItemHandler(this,
                    input, i,
                    bdLeft + (4 + i) * xSlotSize,
                    bdTop + 0 * ySlotSize, 64));
        }
        int numOutputs = 2;
        for (int i = 0; i < numOutputs; ++i) {
            this.addSlotToContainer(new AdvancedSlotItemHandler(this,
                    output, i,
                    bdLeft + (4 + i) * xSlotSize,
                    bdTop + 2 * ySlotSize, 64));
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

            // TODO: FIX
            if (fromSlot < 8) {
                // We are shift clicking something in the tile entity
                if (!this.mergeItemStack(current,
                        8, 8 + numPlayerSlots, true))
                    return null;
            } else {
                // We are shift clicking something from the tile entity
                if (!this.mergeItemStack(current, 0, 4, false))
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

    public void onSlotChanged() {
        te.inputChanged = true;
    }

}
