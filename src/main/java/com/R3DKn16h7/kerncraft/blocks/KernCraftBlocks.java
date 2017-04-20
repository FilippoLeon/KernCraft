package com.R3DKn16h7.kerncraft.blocks;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KernCraftBlocks {
    public static TestBlock TEST_BLOCK;

    public static void createBlocks() {

        TEST_BLOCK = new TestBlock("test_block");

    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        TEST_BLOCK.initModel();
    }
}
