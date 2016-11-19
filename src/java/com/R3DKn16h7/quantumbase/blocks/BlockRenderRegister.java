package com.R3DKn16h7.quantumbase.blocks;

import com.R3DKn16h7.quantumbase.QuantumBase;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class BlockRenderRegister {
        public static void registerBlockRenderer() {
//        	reg(ModBlocks.testBlock);
        }

public static void reg(Block block) {
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    .register(Item.getItemFromBlock(block), 0, 
    		new ModelResourceLocation(QuantumBase.MODID + ":" + block.getUnlocalizedName().substring(5), 
    				"inventory"));
}
}