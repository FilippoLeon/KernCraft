package com.R3DKn16h7.kerncraft.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RomanShield extends ExtraShield {
    public RomanShield(String name) {
        super(name);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == Items.IRON_INGOT ||
                super.getIsRepairable(toRepair, repair);
    }
}