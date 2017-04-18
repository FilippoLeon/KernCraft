package com.R3DKn16h7.kerncraft.guicontainer;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by Filippo on 14/12/2016.
 */
public class FilteredSlotItemHandler extends AdvancedSlotItemHandler {

    public FilteredSlotItemHandler(AdvancedContainer container,
                                   IItemHandler inventory,
                                   int index, int xPosition, int yPosition,
                                   int maxStackSize) {
        super(container, inventory, index, xPosition, yPosition);
    }

    public FilteredSlotItemHandler(AdvancedContainer container,
                                   IItemHandler inventory,
                                   int index, int xPosition, int yPosition) {
        this(container, inventory, index, xPosition, yPosition, 1);
    }

    @Override
    public boolean itemStackIsValid(ItemStack itemstack) {
        return true;
    }
}
