package com.R3DKn16h7.kerncraft.items.upgrades;

import com.R3DKn16h7.kerncraft.items.BasicItem;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;

public class UpgradeItem extends BasicItem {

    public UpgradeItem(String name) {
        super(name);
    }

    public static void create(String name) {
        KernCraftItems.UPGRADES.put(name, new UpgradeItem(name));
    }
}