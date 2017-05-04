package com.R3DKn16h7.kerncraft.items.upgrades;

import com.R3DKn16h7.kerncraft.items.BasicItem;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import com.R3DKn16h7.kerncraft.tileentities.utils.Upgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class UpgradeItem extends BasicItem {

    public UpgradeItem(String name) {
        super(name);
    }

    public static void create(String name) {
        UpgradeItem item = new UpgradeItem(name);
        KernCraftItems.UPGRADES.put(name, item);
    }


    public static Upgrade getUpgrade(ItemStack heldItem) {
        if (heldItem.hasTagCompound()) {
            NBTTagCompound nbt = heldItem.getTagCompound();
            return Upgrade.valueOf(nbt.getString("Upgrade"));
        }

        return null;
    }
}