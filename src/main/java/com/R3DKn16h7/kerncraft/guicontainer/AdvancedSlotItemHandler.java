package com.R3DKn16h7.kerncraft.guicontainer;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Created by Filippo on 14/12/2016.
 */
public class AdvancedSlotItemHandler extends SlotItemHandler {
    /**
     * The AdvancedContainer to which this belong to.
     */
    private AdvancedContainer container;
    /**
     * The slot's max size.
     */
    private int maxStackSize;

    /**
     *
     * @param container
     * @param inventory
     * @param index
     * @param xPosition
     * @param yPosition
     * @param maxStackSize
     */
    public AdvancedSlotItemHandler(AdvancedContainer container,
                                   IItemHandler inventory,
                                   int index, int xPosition, int yPosition,
                                   int maxStackSize) {
        super(inventory, index, xPosition, yPosition);
        this.container = container;
        this.maxStackSize = maxStackSize;
    }

    public AdvancedSlotItemHandler(AdvancedContainer container,
                                   IItemHandler inventory,
                                   int index, int xPosition, int yPosition) {
        this(container, inventory, index, xPosition, yPosition, 1);
    }

    /**
     * Returns the max stack size of the slot.
     * @return
     */
    @Override
    public int getSlotStackLimit() {
        return maxStackSize;
    }

    /**
     * Performs action when slot is changed. Delegates the call to the parent container.
     */
    @Override
    public void onSlotChanged() {
        container.onSlotChanged();
    }

    public boolean itemStackIsValid(ItemStack itemstack) {
        return true;
    }
}
