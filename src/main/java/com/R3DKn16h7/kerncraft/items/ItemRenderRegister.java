package com.R3DKn16h7.kerncraft.items;

import com.R3DKn16h7.kerncraft.KernCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class ItemRenderRegister {

    public static void registerItemRenderer() {
        register(ModItems.PORTABLE_BEACON);
        register(ModItems.CANISTER);
        register(ModItems.ROMAN_SHIELD);
        register(ModItems.GLASS_SHIELD);

        register(ModItems.LAB_BONNET);
        register(ModItems.LAB_COAT);
        register(ModItems.LAB_BOOTS);
        register(ModItems.LAB_PANTS);

        register(ModItems.TEST_ITEM);

        register(ModItems.TYROCINIUM_CHYMICUM);

        register(ModItems.POTATO_BATTERY);
        register(ModItems.ELECTROLYTIC_CELL);
    }


    public static void register(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(item, 0, new ModelResourceLocation(
                        KernCraft.MODID + ":" + item.getUnlocalizedName().substring(5),
                        "inventory"));
    }
}