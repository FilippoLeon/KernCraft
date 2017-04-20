package com.R3DKn16h7.kerncraft;

import com.R3DKn16h7.kerncraft.blocks.BlockRenderRegister;
import com.R3DKn16h7.kerncraft.blocks.KernCraftBlocks;
import com.R3DKn16h7.kerncraft.items.ItemRenderRegister;
import com.R3DKn16h7.kerncraft.tileentities.KernCraftTileEntities;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        KernCraftBlocks.initModels();
        KernCraftTileEntities.initModels();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);

        ItemRenderRegister.registerItemRenderer();
        BlockRenderRegister.registerBlockRenderer();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }
}
