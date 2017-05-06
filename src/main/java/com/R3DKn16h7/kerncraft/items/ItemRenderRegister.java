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
        itemRenderRegister(KernCraftItems.PORTABLE_BEACON);
        itemRenderRegister(KernCraftItems.CANISTER);
        itemRenderRegister(KernCraftItems.FLASK);
        itemRenderRegister(KernCraftItems.PRESSURIZED_CELL);
        itemRenderRegister(KernCraftItems.ROMAN_SHIELD);
        itemRenderRegister(KernCraftItems.GLASS_SHIELD);

        itemRenderRegister(KernCraftItems.LAB_BONNET);
        itemRenderRegister(KernCraftItems.LAB_COAT);
        itemRenderRegister(KernCraftItems.LAB_BOOTS);
        itemRenderRegister(KernCraftItems.LAB_PANTS);

        itemRenderRegister(KernCraftItems.TEST_ITEM);

        itemRenderRegister(KernCraftItems.TYROCINIUM_CHYMICUM);

        itemRenderRegister(KernCraftItems.POTATO_BATTERY);
        itemRenderRegister(KernCraftItems.GENERIC_BATTERY);
        itemRenderRegister(KernCraftItems.ELECTROLYTIC_CELL);

        itemRenderRegister(KernCraftItems.ALCHEMIST_RING);

//        itemRenderRegister(KernCraftItems.MULTI_TOOL);
//        itemRenderRegister(KernCraftItems.MULTI_TOOL);
//        Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI();
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(KernCraftItems.MULTI_TOOL, new SpecialRenderer());

        for (Map.Entry<String, MoleculeItem> item : KernCraftItems.MATERIALS.entrySet()) {
            itemRenderRegister(item.getValue());
        }
        for (Map.Entry<String, UpgradeItem> item : KernCraftItems.UPGRADES.entrySet()) {
            itemRenderRegister(item.getValue());
        }
    }

    public static void itemRenderRegister(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(item, 0, new ModelResourceLocation(
                        KernCraft.MODID + ":" + item.getUnlocalizedName().substring(5),
                        "inventory"));
    }
}