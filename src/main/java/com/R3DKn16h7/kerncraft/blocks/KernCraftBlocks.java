package com.R3DKn16h7.kerncraft.blocks;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public class KernCraftBlocks {
    public static Map<String, BasicBlock> BLOCKS = new HashMap<>();

    public static void createBlocks() {
        BasicBlock.create("test_block", null);
        BasicBlock.create("borosilicate_glass", "glassReinforced");
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        for (Map.Entry<String, BasicBlock> entry : BLOCKS.entrySet()) {
            entry.getValue().initModel();
        }
    }
}
