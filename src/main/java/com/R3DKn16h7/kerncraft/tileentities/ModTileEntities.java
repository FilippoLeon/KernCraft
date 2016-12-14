package com.R3DKn16h7.kerncraft.tileentities;

import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModTileEntities {
    public static ExtractorBlockEntity EXTRACTOR;
    public static TileEntity EXTRACTOR_TE;

    public static DetectorBlockEntity DETECTOR;
    public static TileEntity DETECTOR_TE;

    public static ClockBlockEntity CLOCK;
    public static TileEntity CLOCK_TE;

    //public static LampBlockEntity LAMP;
    public static LampBlockEntity[] Lamp = new LampBlockEntity[16];
    public static LampBlockEntity LAMP_DEFAULT;
    public static LampTileEntity LAMP_TE;

    public static void createEntities() {
        EXTRACTOR = new ExtractorBlockEntity("extractor");
        EXTRACTOR_TE = new ExtractorTileEntity();

        DETECTOR = new DetectorBlockEntity();
        DETECTOR_TE = new DetectorTileEntity();

        CLOCK = new ClockBlockEntity();
        CLOCK_TE = new ClockTileEntity();

        //LAMP = new LampBlockEntity();
        for (int i = 0; i < 1; ++i) Lamp[i] = new LampBlockEntity(i);
        LAMP_DEFAULT = Lamp[0];

        GameRegistry.register(LAMP_DEFAULT);
        GameRegistry.register(new ItemBlock(LAMP_DEFAULT), LAMP_DEFAULT.getRegistryName());
        GameRegistry.registerTileEntity(LampTileEntity.class, "lamp");

        LAMP_TE = new LampTileEntity();
    }


    @SideOnly(Side.CLIENT)
    public static void initModels() {
        EXTRACTOR.initModel();
        DETECTOR.initModel();
        CLOCK.initModel();
        Lamp[0].initModel();
        //for(int i = 0; i < 15; ++i) Lamp[i].initModel();
    }
}

