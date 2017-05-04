package com.R3DKn16h7.kerncraft.items.upgrades;

import com.R3DKn16h7.kerncraft.items.BasicItem;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import com.R3DKn16h7.kerncraft.tileentities.utils.Upgrade;
import net.minecraft.item.ItemStack;

public class UpgradeItem extends BasicItem {

    Upgrade upgradeType;

    public UpgradeItem(String name) {
        super(name);
    }

    public static void create(String name, Upgrade upgrade) {
        UpgradeItem item = new UpgradeItem(name);
        item.upgradeType = upgrade;
        KernCraftItems.UPGRADES.put(name, item);
    }


    public static Upgrade getUpgrade(ItemStack heldItem) {
        if (heldItem.getItem() instanceof UpgradeItem) {
            return ((UpgradeItem) heldItem.getItem()).upgradeType;
        }
//        if (heldItem.hasTagCompound()) {
//            NBTTagCompound nbt = heldItem.getTagCompound();
//            if(nbt.hasKey("Upgrade")) {
//                return Upgrade.valueOf(nbt.getString("Upgrade"));
//            } else {
//                return null;
//            }
//        }

        return null;
    }
}