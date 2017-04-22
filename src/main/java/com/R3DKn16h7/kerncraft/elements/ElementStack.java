package com.R3DKn16h7.kerncraft.elements;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Filippo on 20-Apr-17.
 *
 * This is used to specify custom recipes.
 */
public class ElementStack {
    public int id;
    public int quantity;
    public float prob = 1.0f;

    public ElementStack(int id_, int quantity_) {
        id = id_;
        quantity = quantity_;
    }

    public ElementStack(String name_, int quantity_) {
        id = ElementBase.symbolToId(name_);
        quantity = quantity_;
    }

    public ElementStack(int id_, int quantity_, float prob_) {
        this(id_, quantity_);
        prob = prob_;
    }

    public ElementStack(String name_, int quantity_, float prob_) {
        this(name_, quantity_);
        prob = prob_;
    }

    public static int getQuantity(ItemStack stack) {
        if(stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("Element") && nbt.hasKey("Quantity")) {
                return nbt.getInteger("Quantity");
            }
        }
        return 0;
    }

    public static int removeFromStack(ItemStack stack, int removedQuantity) {
        return removeFromStack(stack, removedQuantity, false);
    }

    public static int removeFromStack(ItemStack stack, int removedQuantity, boolean simulate) {
        int removable = 0;
        if(stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("Element") && nbt.hasKey("Quantity")) {
                removable = Math.min(removedQuantity, nbt.getInteger("Quantity"));

                if (!simulate) {
                    nbt.setInteger("Quantity", nbt.getInteger("Quantity") - removable);
                }
            }
        }
        return removable;
    }

    public static Integer getElementId(ItemStack stack) {
        if(stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("Element")) {
                return nbt.getInteger("Element");
            }
        }
        return 0;
    }

    public boolean isContainedInStack(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("Element") && nbt.getInteger("Element") == id &&
                    quantity > 0 && nbt.hasKey("Quantity") && nbt.getInteger("Quantity") >= quantity) {
                return true;
            }
        }
        return false;
    }

    public boolean isContainedInStackAnyQuantity(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("Element") && nbt.getInteger("Element") == id) {
                return true;
            }
        }
        return false;
    }
}
