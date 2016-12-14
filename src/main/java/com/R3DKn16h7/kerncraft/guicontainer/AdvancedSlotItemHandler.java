package com.R3DKn16h7.kerncraft.guicontainer;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Created by Filippo on 14/12/2016.
 */
public class AdvancedSlotItemHandler extends SlotItemHandler {

    private AdvancedContainer container;
    private int maxStackSize;

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

    @Override
    public int getSlotStackLimit() {
        return maxStackSize;
    }

    @Override
    public void onSlotChanged() {
        container.onSlotChanged();
    }
}
