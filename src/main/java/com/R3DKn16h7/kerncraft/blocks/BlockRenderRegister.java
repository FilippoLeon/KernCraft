package com.R3DKn16h7.kerncraft.blocks;

import com.R3DKn16h7.kerncraft.KernCraft;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class BlockRenderRegister {
    public static void registerBlockRenderer() {
//        	reg(ModBlocks.TEST_BLOCK);
    }

    public static void reg(Block block) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(Item.getItemFromBlock(block), 0,
                        new ModelResourceLocation(KernCraft.MODID + ":" + block.getUnlocalizedName().substring(5),
                                "inventory"));
    }
}