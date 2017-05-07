package com.R3DKn16h7.kerncraft.blocks;

import net.minecraft.block.material.Material;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public class KernCraftBlocks {
    public static Map<String, BasicBlock> BLOCKS = new HashMap<>();

    public static void createBlocks() {
        BasicBlock.create("test_block", null);
        GlassBlock.create("borosilicate_glass",
                new String[]{"blockGlassReinforced", "blockGlassBorosilicate"}
        );
        BlockBorosilicateGlassPane.create(Material.GLASS,
                "borosilicate_glass_pane", true,
                new String[]{"paneGlassBorosilicate", "paneGlassReinforced"}
        );
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        for (Map.Entry<String, BasicBlock> entry : BLOCKS.entrySet()) {
            entry.getValue().initModel();
        }
    }
}
