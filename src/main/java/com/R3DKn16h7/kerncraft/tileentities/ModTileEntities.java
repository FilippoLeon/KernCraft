package com.R3DKn16h7.kerncraft.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModTileEntities {
    public static TileEntity EXTRACTOR_TE;
    public static TileEntity DETECTOR_TE;
    public static TileEntity CLOCK_TE;
    public static ExtractorBlockEntity EXTRACTOR;
    public static DetectorBlockEntity DETECTOR;
    public static ClockBlockEntity CLOCK;


    public static void createEntities() {
        EXTRACTOR = new ExtractorBlockEntity("extractor");
        DETECTOR = new DetectorBlockEntity();
        CLOCK = new ClockBlockEntity();
        EXTRACTOR_TE = new ExtractorTileEntity();
        DETECTOR_TE = new DetectorTileEntity();
        CLOCK_TE = new ClockTileEntity();
    }


    @SideOnly(Side.CLIENT)
    public static void initModels() {
        EXTRACTOR.initModel();
        DETECTOR.initModel();
        CLOCK.initModel();
    }
}

