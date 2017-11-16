package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.tileentities.machines.*;
import com.R3DKn16h7.kerncraft.tileentities.utilities.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KernCraftTileEntities {
    public static ExtractorBlockEntity EXTRACTOR;
    public static ExtractorTileEntity EXTRACTOR_TE;

    public static DetectorBlockEntity DETECTOR;
    public static DetectorTileEntity DETECTOR_TE;

    public static ClockBlockEntity CLOCK;
    public static ClockTileEntity CLOCK_TE;

    public static LampBlockEntity LAMP;
    public static LampTileEntity LAMP_TE;

    public static ChemicalFurnaceBlockEntity CHEMICAL_FURNACE;
    public static ChemicalFurnaceTileEntity CHEMICAL_FURNACE_TE;

    public static TimeMachineBlockEntity TIME_MACHINE;
    public static TimeMachineTileEntity TIME_MACHINE_TE;

    public static FillerBlockEntity FILLER_MACHINE;
    public static FillerTileEntity FILLER_MACHINE_TE;

    public static CentrifugeBlockEntity CENTRIFUGE_MACHINE;
    public static CentrifugeTileEntity CENTRIFUGE_MACHINE_TE;

    public static ElectrolyzerBlockEntity ELECTROLYZER_MACHINE;
    public static ElectrolyzerTileEntity ELECTROLYZER_MACHINE_TE;

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

        FILLER_MACHINE = new FillerBlockEntity("filler");
        FILLER_MACHINE_TE = new FillerTileEntity();

        CENTRIFUGE_MACHINE = new CentrifugeBlockEntity("centrifuge");
        CENTRIFUGE_MACHINE_TE = new CentrifugeTileEntity();

        ELECTROLYZER_MACHINE = new ElectrolyzerBlockEntity("electrolyzer");
        ELECTROLYZER_MACHINE_TE = new ElectrolyzerTileEntity();

        registerEntities();
    }

    public static void registerEntities() {
        register(EXTRACTOR, ExtractorTileEntity.class);
        register(DETECTOR, DetectorTileEntity.class);
        register(CLOCK, ClockTileEntity.class);
        register(LAMP, LampTileEntity.class);
        register(CHEMICAL_FURNACE, ChemicalFurnaceTileEntity.class);
        register(TIME_MACHINE, TimeMachineTileEntity.class);
        register(FILLER_MACHINE, FillerTileEntity.class);
        register(CENTRIFUGE_MACHINE, CentrifugeTileEntity.class);
        register(ELECTROLYZER_MACHINE, ElectrolyzerTileEntity.class);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        initModel(EXTRACTOR);
        initModel(DETECTOR);
        initModel(CLOCK);
        initModel(LAMP);
        initModel(CHEMICAL_FURNACE);
        initModel(TIME_MACHINE);
        initModel(FILLER_MACHINE);
        initModel(CENTRIFUGE_MACHINE);
        initModel(ELECTROLYZER_MACHINE);
    }

    /**
     * Register Block, Item and TileEntity
     *
     * @param tileEntity
     * @param tileEntityClass
     */
    public static void register(Block tileEntity, Class tileEntityClass) {
        ForgeRegistries.BLOCKS.register(tileEntity);

        Item it = new ItemBlock(tileEntity);
//        it.setUnlocalizedName(tileEntity.getRegistryName().toString());
        it.setRegistryName(tileEntity.getRegistryName().toString());

        ForgeRegistries.ITEMS.register(it);
        GameRegistry.registerTileEntity(tileEntityClass, tileEntity.getRegistryName().toString());
    }

    /**
     * Registermodel for block
     *
     * @param tileEntity
     */
    public static void initModel(Block tileEntity) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(tileEntity),
                0,
                new ModelResourceLocation(tileEntity.getRegistryName(),
                        "inventory"));
    }
}

