package com.R3DKn16h7.kerncraft.guicontainer;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

/**
 * Created by Filippo on 14/12/2016.
 */
public abstract class AdvancedContainer extends Container {
    static public int xSlotSize = 18;
    static public int ySlotSize = 18;
    static public int bdLeft = 8;
    static public int bdTop = 17;
    static public int numPlayerSlots = 36;

    protected static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB) {
        return stackB.getItem() == stackA.getItem() &&
                (!stackA.getHasSubtypes() || stackA.getMetadata() == stackB.getMetadata()) &&
                ItemStack.areItemStackTagsEqual(stackA, stackB);
    }

    abstract public void onSlotChanged();
}
