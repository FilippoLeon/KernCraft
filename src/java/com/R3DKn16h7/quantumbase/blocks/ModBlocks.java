package com.R3DKn16h7.quantumbase.blocks;


import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
    public static TestBlock testBlock;
    public static ItemBlock testItemBlock;
    public static ExtractorBlockEntity extractor;

    public static void createBlocks() {
    	
    	testBlock = new TestBlock("test_block");
    	extractor = new ExtractorBlockEntity("extractor");
//    	GameRegistry.register(testBlock);
//    	testItemBlock = (ItemBlock) new ItemBlock(testBlock).setRegistryName("test_block");
//    	GameRegistry.register(testItemBlock);
    	
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
    	testBlock.initModel();
    	extractor.initModel();
    }
}
