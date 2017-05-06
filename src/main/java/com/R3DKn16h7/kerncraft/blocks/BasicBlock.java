package com.R3DKn16h7.kerncraft.blocks;

import com.R3DKn16h7.kerncraft.KernCraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class BasicBlock extends Block {

    public BasicBlock(Material mat, String unlocalizedName,
                      float hardness, float resistance, Object oreDictNames) {
        super(mat);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(KernCraft.KERNCRAFT_CREATIVE_TAB);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setRegistryName(unlocalizedName);

        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());

        if (oreDictNames != null) {
            if (oreDictNames instanceof String) {
                OreDictionary.registerOre(((String) oreDictNames), this);
            } else if (oreDictNames instanceof String[]) {
                for (String str : ((String[]) oreDictNames)) {
                    OreDictionary.registerOre(str, this);
                }
            }
        }
    }

    public BasicBlock(String unlocalizedName, Object oreDictNames) {
        this(Material.ROCK, unlocalizedName,
                0.0f, 0.0f, oreDictNames
        );
    }

    public static void create(Material mat, String unlocalizedName,
                              float hardness, float resistance, Object oreDictNames) {
        KernCraftBlocks.BLOCKS.put(unlocalizedName,
                new BasicBlock(mat, unlocalizedName, hardness, resistance, oreDictNames)
        );
    }

    public static void create(String unlocalizedName, Object oreDictNames) {
        KernCraftBlocks.BLOCKS.put(unlocalizedName,
                new BasicBlock(unlocalizedName, oreDictNames)
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
        BlockStainedGlassPane

        return false;
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}