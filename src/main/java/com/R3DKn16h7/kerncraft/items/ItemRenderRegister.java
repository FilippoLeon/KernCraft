package com.R3DKn16h7.kerncraft.items;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.items.molecules.MoleculeItem;
import com.R3DKn16h7.kerncraft.items.tools.SpecialRenderer;
import com.R3DKn16h7.kerncraft.items.upgrades.UpgradeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

import java.util.Map;

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
        register(KernCraftItems.GENERIC_BATTERY);
        register(KernCraftItems.ELECTROLYTIC_CELL);

        register(KernCraftItems.ALCHEMIST_RING);

//        register(KernCraftItems.MULTI_TOOL);
//        register(KernCraftItems.MULTI_TOOL);
//        Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI();
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(KernCraftItems.MULTI_TOOL, new SpecialRenderer());

        for (Map.Entry<String, MoleculeItem> item : KernCraftItems.MATERIALS.entrySet()) {
            register(item.getValue());
        }
        for (Map.Entry<String, UpgradeItem> item : KernCraftItems.UPGRADES.entrySet()) {
            register(item.getValue());
        }
    }

    public static void register(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(item, 0, new ModelResourceLocation(
                        KernCraft.MODID + ":" + item.getUnlocalizedName().substring(5),
                        "inventory"));
    }
}