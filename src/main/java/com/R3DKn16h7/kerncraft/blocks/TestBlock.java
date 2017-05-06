package com.R3DKn16h7.kerncraft.blocks;

import net.minecraft.block.material.Material;

public class TestBlock extends BasicBlock {

    public TestBlock(Material mat, String unlocalizedName, float hardness, float resistance) {
        super(mat, unlocalizedName, hardness, resistance, null);


    }

    public TestBlock(String unlocalizedName) {
        this(Material.ROCK, unlocalizedName,
                0.0f, 0.0f
        );
    }
}