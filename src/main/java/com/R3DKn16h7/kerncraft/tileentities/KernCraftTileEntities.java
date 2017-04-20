package com.R3DKn16h7.kerncraft.tileentities;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KernCraftTileEntities {
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

    public static TimeMachineBlockEntity TIME_MACHINE;
    public static TimeMachineTileEntity TIME_MACHINE_TE;

    public static void createEntities() {
        EXTRACTOR = new ExtractorBlockEntity("extractor");
        EXTRACTOR_TE = new ExtractorTileEntity();

        DETECTOR = new DetectorBlockEntity();
        DETECTOR_TE = new DetectorTileEntity();

        CLOCK = new ClockBlockEntity();
        CLOCK_TE = new ClockTileEntity();

        LAMP = new LampBlockEntity("lamp");
        LAMP_TE = new LampTileEntity();

        CHEMICAL_FURNACE = new ChemicalFurnaceBlockEntity("chemical_furnace");
        CHEMICAL_FURNACE_TE = new ChemicalFurnaceTileEntity();

        TIME_MACHINE = new TimeMachineBlockEntity("time_machine");
        TIME_MACHINE_TE = new TimeMachineTileEntity();
        register(TIME_MACHINE, TimeMachineTileEntity.class);
    }


    @SideOnly(Side.CLIENT)
    public static void initModels() {
        EXTRACTOR.initModel();
        DETECTOR.initModel();
        CLOCK.initModel();
        LAMP.initModel();
        CHEMICAL_FURNACE.initModel();
        initModel(TIME_MACHINE);
    }

    public static void register(Block te, Class cl) {
        GameRegistry.register(te);
        GameRegistry.register(new ItemBlock(te), te.getRegistryName());
        GameRegistry.registerTileEntity(cl, te.getRegistryName().toString());
    }

    public static void initModel(Block te) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(te),
                0,
                new ModelResourceLocation(te.getRegistryName(),
                        "inventory"));
    }
}

