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

    public static LampBlockEntity LAMP;
    public static LampTileEntity LAMP_TE;

    public static ChemicalFurnaceBlockEntity CHEMICAL_FURNACE;
    public static ChemicalFurnaceTileEntity CHEMICAL_FURNACE_TE;

    public static void createEntities() {
        EXTRACTOR = new ExtractorBlockEntity("extractor");
        EXTRACTOR_TE = new ExtractorTileEntity();

        DETECTOR = new DetectorBlockEntity();
        DETECTOR_TE = new DetectorTileEntity();

        CLOCK = new ClockBlockEntity();
        CLOCK_TE = new ClockTileEntity();

        LAMP = new LampBlockEntity(0);
        GameRegistry.register(LAMP);
        GameRegistry.register(new ItemBlock(LAMP), LAMP.getRegistryName());
        GameRegistry.registerTileEntity(LampTileEntity.class, "lamp");
        LAMP_TE = new LampTileEntity();

        CHEMICAL_FURNACE = new ChemicalFurnaceBlockEntity("chemical_furnace");
        CHEMICAL_FURNACE_TE = new ChemicalFurnaceTileEntity();
    }


    @SideOnly(Side.CLIENT)
    public static void initModels() {
        EXTRACTOR.initModel();
        DETECTOR.initModel();
        CLOCK.initModel();
        LAMP.initModel();
        CHEMICAL_FURNACE.initModel();
        //for(int i = 0; i < 15; ++i) Lamp[i].initModel();
    }
}

