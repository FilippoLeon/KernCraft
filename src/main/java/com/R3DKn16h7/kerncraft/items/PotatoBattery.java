package com.R3DKn16h7.kerncraft.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class PotatoBattery extends Item {
    public static String base_name = "potato_battery";

    public PotatoBattery() {
        super();

        this.setUnlocalizedName(base_name);
        this.setRegistryName(base_name);
        this.setCreativeTab(CreativeTabs.MISC);
        GameRegistry.register(this);
    }
}