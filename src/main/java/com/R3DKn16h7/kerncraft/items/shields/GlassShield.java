package com.R3DKn16h7.kerncraft.items.shields;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GlassShield extends ExtraShield {
    public GlassShield(String name) {
        super(name);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == Item.getItemFromBlock(Blocks.GLASS) || super.getIsRepairable(toRepair, repair);
    }
}