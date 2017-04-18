package com.R3DKn16h7.kerncraft.guicontainer;

import com.R3DKn16h7.kerncraft.tileentities.ExtractorTileEntity;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.IItemHandler;

public class ExtractorContainer extends AdvancedContainer {

    private ExtractorTileEntity te;

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
    public ExtractorContainer(IInventory playerInv, ExtractorTileEntity te) {
        this.te = te;
//        sendpackettoserver
//        Dispatch
//        Packet
//                N
//        PacketDispatcher.sendPacketToPlayer(thisTileEntityTiny.getDescriptionPacket(), (Player)player);
//        NetworkManager.NETWORK_PACKETS_MARKER.se
//        PacketDispacher
        Packet packet = te.getUpdatePacket();
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        server.getPlayerList().sendPacketToAllPlayers(packet);

        IItemHandler input = te.getInput();
        IItemHandler output = te.getOutput();

        int id = -1;
        // Input (I) ID 0
        this.addSlotToContainer(new AdvancedSlotItemHandler(this,
                input, ++id,
                bdLeft + xSlotSize, bdTop, 64));
        // Catalyst (LAB_BOOTS) ID 1
        this.addSlotToContainer(new AdvancedSlotItemHandler(this,
                input, ++id, bdLeft + 3 * xSlotSize, bdTop, 64));
        // Container (R) ID 2
        this.addSlotToContainer(new AdvancedSlotItemHandler(this,
                input, ++id, bdLeft + 8 * xSlotSize, bdTop, 64));
        // Fuel slot (F) ID 3
        this.addSlotToContainer(new AdvancedSlotItemHandler(this,
                input, ++id,
                bdLeft + xSlotSize, bdTop + 2 * ySlotSize, 64));
        // Output (O) ID 4-7
        int numOutputs = 4;
        for (int i = 0; i < numOutputs; ++i) {
            this.addSlotToContainer(new AdvancedSlotItemHandler(this, output, i,
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
                ItemStack itemstack = slot.getStack();
                // Is slot non-empty but can accept some of those items
                if (itemstack != ItemStack.EMPTY && areItemStacksEqual(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();

                    int max_stack_size = Math.min(stack.getMaxStackSize(), slot.getSlotStackLimit());
                    // If total of items can fit in slot, then just do that
                    if (j <= max_stack_size) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.onSlotChanged();
                        flag = true;
                        break;
                    // If total of items cannot completely fill slot, do as much as possible
                    } else if (itemstack.getCount() < stack.getMaxStackSize()) {
                        stack.setCount(max_stack_size - itemstack.getCount());
                        itemstack.setCount(max_stack_size);
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
                if (itemstack1 == ItemStack.EMPTY && slot1.isItemValid(stack)) {
                    // If total of items can fit in slot, then just do that
                    if (stack.getCount() <= slot1.getSlotStackLimit()) {
                        slot1.putStack(stack.copy());
                        slot1.onSlotChanged();
                        stack.setCount(0);
                        stack = ItemStack.EMPTY;
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

    @Override
    @MethodsReturnNonnullByDefault
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
        ItemStack previous = ItemStack.EMPTY;
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
                    return ItemStack.EMPTY;
            } else {
                // We are shift clicking something from the tile entity
                if (!this.mergeItemStack(current, 0, 8, false))
                    return ItemStack.EMPTY;
            }

            if (current.getCount() == 0)
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();

            if (current.getCount() == previous.getCount())
                return null;
            slot.onTake(playerIn, current);
        }
        return previous;
    }

    public void onSlotChanged() {
        te.inputChanged = true;
    }

}
