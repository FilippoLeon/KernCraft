package com.R3DKn16h7.kerncraft.tileentities;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LampBlockEntity extends BlockContainer {

    public static final PropertyInteger TIME = PropertyInteger.create("time", 0, 15);

//    static String name = "detector";

    protected LampBlockEntity() {
        super(Material.IRON);
        String name = "clock";
        this.setUnlocalizedName(name);
        this.setHardness(2.0f);
        this.setResistance(6.0f);
        this.setHarvestLevel("pickaxe", 2);
        this.setCreativeTab(CreativeTabs.MISC);
        //this.isBlockContainer = true;

        setDefaultState(this.blockState.getBaseState().withProperty(TIME, 0));

        setUnlocalizedName(name);
        setRegistryName(name);
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
        GameRegistry.registerTileEntity(ClockTileEntity.class, getRegistryName().toString());
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TIME, meta);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TIME).intValue();
    }

    @Override
    protected BlockStateContainer createBlockState() {

        return new BlockStateContainer(this, TIME);
    }

    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.getValue(TIME).intValue();
    }

    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.getValue(TIME).intValue();
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower(IBlockState state) {
        return true;
    }


    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        ClockTileEntity te = (ClockTileEntity) world.getTileEntity(pos);
        super.breakBlock(world, pos, blockstate);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new ClockTileEntity();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {

        return EnumBlockRenderType.MODEL;
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }

}
