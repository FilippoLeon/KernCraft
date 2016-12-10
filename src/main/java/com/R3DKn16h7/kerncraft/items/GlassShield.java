package com.R3DKn16h7.kerncraft.items;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GlassShield extends ExtraShield {
    public GlassShield(String name) {
        super(name);
    }


    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == Item.getItemFromBlock(Blocks.GLASS) || super.getIsRepairable(toRepair, repair);
    }
}