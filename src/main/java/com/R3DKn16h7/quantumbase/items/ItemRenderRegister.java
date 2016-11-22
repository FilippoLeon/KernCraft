package com.R3DKn16h7.quantumbase.items;

import com.R3DKn16h7.quantumbase.QuantumBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class ItemRenderRegister {

    public static void registerItemRenderer() {
        reg(ModItems.portableBeacon);
        ModItems.canister.registerRender();
        reg(ModItems.EXTRA_SHIELD);
    }


    public static void reg(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(item, 0, new ModelResourceLocation(QuantumBase.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
    }
}