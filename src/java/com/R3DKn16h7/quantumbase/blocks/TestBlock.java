package com.R3DKn16h7.quantumbase.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TestBlock extends Block {

    public TestBlock(String unlocalizedName, float hardness, float resistance) {
        super(Material.ROCK);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.MISC);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setRegistryName("test_block");

        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
        
    }

    public TestBlock(String unlocalizedName) {
        this(unlocalizedName, 
        		0.0f, 0.0f);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, 
        		new ModelResourceLocation(getRegistryName(), "inventory"));
    }

}