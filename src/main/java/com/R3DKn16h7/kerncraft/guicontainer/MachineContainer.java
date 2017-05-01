package com.R3DKn16h7.kerncraft.guicontainer;

import com.R3DKn16h7.kerncraft.tileentities.MachineTileEntity;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Filippo on 18-Apr-17.
 */
public class MachineContainer<T extends MachineTileEntity> extends AdvancedContainer {
    protected T te;

    public MachineContainer(IInventory playerInv, T te) {
        this.te = te;

//        Packet packet = te.getUpdatePacket();
//        MinecraftServer server = FMLCommonHandler.INSTANCE().getMinecraftServerInstance();
//        server.getPlayerList().sendPacketToAllPlayers(packet);

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
            while (stack.getCount() > 0 &&
                    (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)) {
                // Try and fill this slot
                Slot slot = this.inventorySlots.get(i);
                // Current stack in tested slot
                ItemStack itemStackInSlot = slot.getStack();
                // Is slot non-empty but can accept some of those items
                if (!itemStackInSlot.isEmpty() && areItemStacksEqual(stack, itemStackInSlot)) {
                    int j = itemStackInSlot.getCount() + stack.getCount();

                    int max_stack_size = Math.min(stack.getMaxStackSize(), slot.getSlotStackLimit());
                    // If total of items can fit in slot, then just do that
                    if (j <= max_stack_size) {
                        stack.setCount(0);
                        itemStackInSlot.setCount(j);
                        slot.onSlotChanged();
                        flag = true;
                        break;
                        // If total of items cannot completely fill slot, do as much as possible
                    } else if (itemStackInSlot.getCount() < max_stack_size) {
                        int itemsFittingInSlot = max_stack_size - itemStackInSlot.getCount();
                        stack.setCount(stack.getCount() - itemsFittingInSlot);
                        itemStackInSlot.setCount(max_stack_size);
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
        if (stack.getCount() > 0) {
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
                if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
                    // If total of items can fit in slot, then just do that
                    if (stack.getCount() <= slot1.getSlotStackLimit()) {
                        slot1.putStack(stack.copy());
                        slot1.onSlotChanged();
                        stack.setCount(0);
                        flag = true;
                        break;
                        // If total of items cannot completely fill slot, do as much as possible
                    } else {
                        ItemStack portion = stack.copy();
                        portion.setCount(slot1.getSlotStackLimit());
                        slot1.putStack(portion.copy());
                        slot1.onSlotChanged();
                        stack.setCount(stack.getCount() - slot1.getSlotStackLimit());
                        if (stack.getCount() == 0) stack = ItemStack.EMPTY;
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

    /**
     * This handles shift clicking
     *
     * @param playerIn
     * @param fromSlot
     * @return
     */
    @Override
    @MethodsReturnNonnullByDefault
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
        ItemStack previous = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(fromSlot);

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();

            // TODO: FIX, hardcoded magic number
            if (fromSlot < te.getTotalSlots()) {
                // We are shift clicking something in the tile entity
                if (!this.mergeItemStack(current,
                        te.getTotalSlots(), te.getTotalSlots() + numPlayerSlots, true))
                    return ItemStack.EMPTY;
            } else {
                // We are shift clicking something from the tile entity
                if (!this.mergeItemStack(current, 0, te.getTotalSlots(), false))
                    return ItemStack.EMPTY;
            }

            if (current.getCount() == 0)
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();

            if (current.getCount() == previous.getCount())
                return ItemStack.EMPTY;
            slot.onTake(playerIn, current);
        }
        te.inputChanged = true;

        return previous;
    }

    public void onSlotChanged() {
        te.inputChanged = true;
    }
}
