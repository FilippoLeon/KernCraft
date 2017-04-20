package com.R3DKn16h7.kerncraft.elements;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Filippo on 20-Apr-17.
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

    public boolean isContainedInStack(ItemStack stack) {
        if(stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("Element") && nbt.getInteger("Element") == id &&
                    quantity > 0 && nbt.hasKey("Quantity") && nbt.getInteger("Quantity") >= quantity) {
                return true;
            }
        }
        return false;
    }

    public boolean isContainedInStackAnyQuantity(ItemStack stack) {
        if(stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("Element") && nbt.getInteger("Element") == id) {
                return true;
            }
        }
        return false;
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

    public static void removeFromStack(ItemStack stack, int removedQuantity) {
        if(stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("Element") && nbt.hasKey("Quantity")) {
                nbt.setInteger("Quantity", nbt.getInteger("Quantity") - removedQuantity);
            }
        }
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
}
