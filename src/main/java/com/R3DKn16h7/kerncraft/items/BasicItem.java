package com.R3DKn16h7.kerncraft.items;

import com.R3DKn16h7.kerncraft.KernCraft;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

abstract public class BasicItem extends Item {
    String name;

    public BasicItem(String name) {
        super();
        this.name = name;

        this.setUnlocalizedName(getName());
        this.setRegistryName(getName());
        this.setCreativeTab(KernCraft.KERNCRAFT_CREATIVE_TAB);
        GameRegistry.register(this);
    }

    public String getName() {
        return name;
    }
}