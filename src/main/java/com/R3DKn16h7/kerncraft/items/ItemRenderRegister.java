package com.R3DKn16h7.kerncraft.items;

import com.R3DKn16h7.kerncraft.KernCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class ItemRenderRegister {

    public static void registerItemRenderer() {
        register(KernCraftItems.PORTABLE_BEACON);
        register(KernCraftItems.CANISTER);
        register(KernCraftItems.FLASK);
        register(KernCraftItems.PRESSURIZED_CELL);
        register(KernCraftItems.ROMAN_SHIELD);
        register(KernCraftItems.GLASS_SHIELD);

        register(KernCraftItems.LAB_BONNET);
        register(KernCraftItems.LAB_COAT);
        register(KernCraftItems.LAB_BOOTS);
        register(KernCraftItems.LAB_PANTS);

        register(KernCraftItems.TEST_ITEM);

        register(KernCraftItems.TYROCINIUM_CHYMICUM);

        register(KernCraftItems.POTATO_BATTERY);
        register(KernCraftItems.ELECTROLYTIC_CELL);

        register(KernCraftItems.ALCHEMIST_RING);
    }


    public static void register(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(item, 0, new ModelResourceLocation(
                        KernCraft.MODID + ":" + item.getUnlocalizedName().substring(5),
                        "inventory"));
    }
}