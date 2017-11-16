package com.R3DKn16h7.kerncraft.items.molecules;

import com.R3DKn16h7.kerncraft.KernCraft;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ElectrolyticCell extends Item {
    public static String base_name = "electrolytic_cell";

    public ElectrolyticCell() {
        super();

        this.setUnlocalizedName(base_name);
        this.setRegistryName(base_name);
        this.setCreativeTab(KernCraft.KERNCRAFT_CREATIVE_TAB);

        ForgeRegistries.ITEMS.register(this);
    }

}