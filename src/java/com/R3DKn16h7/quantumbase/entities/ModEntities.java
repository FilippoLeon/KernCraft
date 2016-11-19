package com.R3DKn16h7.quantumbase.entities;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModEntities {
    public static TileEntity extractorEntity;

    public static void createEntities() {
    	
    	extractorEntity = new ExtractorTileEntity();
//    	GameRegistry.register(extractorEntity);
    	 GameRegistry.registerTileEntity(ExtractorTileEntity.class, "tutorial_tile_entity");
//    	 ModLoader.registerTileEntity(ExtractorEntity.class, "ExpBank");
    }
}

