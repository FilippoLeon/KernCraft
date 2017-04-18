package com.R3DKn16h7.kerncraft.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class PotatoBattery extends EnergyContainerItem {
    public static String base_name = "potato_battery";

    public PotatoBattery() {
        super();

        this.setUnlocalizedName(base_name);
        this.setRegistryName(base_name);
        this.setCreativeTab(CreativeTabs.MISC);
        this.setMaxStackSize(1);
        GameRegistry.register(this);
    }

}