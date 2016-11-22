package com.R3DKn16h7.quantumbase.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModTileEntities {
    public static TileEntity EXTRACTOR_TE;
    public static TileEntity DETECTOR_TE;
    public static ExtractorBlockEntity EXTRACTOR;
    public static DetectorBlockEntity DETECTOR;


    public static void createEntities() {

//    	 GameRegistry.registerTileEntity(ExtractorTileEntity.class, "extractor");
//         GameRegistry.registerTileEntity(DetectorTileEntity.class, "detector");
        EXTRACTOR = new ExtractorBlockEntity("extractor");
        DETECTOR = new DetectorBlockEntity();
        EXTRACTOR_TE = new ExtractorTileEntity();
        DETECTOR_TE = new DetectorTileEntity();
//    	GameRegistry.register(extractorEntity);
//    	 ModLoader.registerTileEntity(ExtractorEntity.class, "ExpBank");
    }


    @SideOnly(Side.CLIENT)
    public static void initModels() {
        EXTRACTOR.initModel();
        DETECTOR.initModel();
    }
}

