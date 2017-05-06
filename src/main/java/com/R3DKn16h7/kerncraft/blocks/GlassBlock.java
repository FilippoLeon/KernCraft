package com.R3DKn16h7.kerncraft.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GlassBlock extends BasicBlock {

    public GlassBlock(Material mat, String unlocalizedName,
                      float hardness, float resistance, Object oreDictNames) {
        super(mat, unlocalizedName, hardness, resistance, oreDictNames);
    }

    public GlassBlock(String unlocalizedName, Object oreDictNames) {
        this(Material.ROCK, unlocalizedName,
                0.0f, 0.0f, oreDictNames
        );
    }

    public static void create(Material mat, String unlocalizedName,
                              float hardness, float resistance, Object oreDictNames) {
        KernCraftBlocks.BLOCKS.put(unlocalizedName,
                new GlassBlock(mat, unlocalizedName,
                        hardness, resistance, oreDictNames)
        );
    }

    public static void create(String unlocalizedName, Object oreDictNames) {
        KernCraftBlocks.BLOCKS.put(unlocalizedName,
                new GlassBlock(unlocalizedName, oreDictNames)
        );
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

}