package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.KernCraft;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TimeMachineBlockEntity extends BlockContainer {

    protected TimeMachineBlockEntity(String unlocalizedName) {
        super(Material.IRON);
        this.setUnlocalizedName(unlocalizedName);
        this.setHardness(2.0f);
        this.setResistance(6.0f);
        this.setHarvestLevel("pickaxe", 2);
        this.setCreativeTab(KernCraft.KERNCRAFT_CREATIVE_TAB);

        setUnlocalizedName(unlocalizedName);
        setRegistryName(unlocalizedName);
    }

    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        TimeMachineTileEntity te = (TimeMachineTileEntity) world.getTileEntity(pos);
        super.breakBlock(world, pos, blockstate);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TimeMachineTileEntity();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

}
