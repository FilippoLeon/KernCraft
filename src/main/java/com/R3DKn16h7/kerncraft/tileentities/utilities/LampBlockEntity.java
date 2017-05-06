package com.R3DKn16h7.kerncraft.tileentities.utilities;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.network.ModGuiHandler;
import com.R3DKn16h7.kerncraft.tileentities.KernCraftTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class LampBlockEntity extends BlockContainer {

    /**
     * A block state containing the light level (i.e. power level).
     */
    public static final PropertyInteger POWERED = PropertyInteger.create("lightlevel", 0, 15);

    /**
     * Constructor, set names, set default params and itemRenderRegister to Block itemRenderRegister.
     * @param unlocalizedName
     */
    public LampBlockEntity(String unlocalizedName) {
        super(Material.IRON);
        this.setUnlocalizedName(unlocalizedName);
        this.setHardness(2.0f);
        this.setResistance(6.0f);
        this.setHarvestLevel("pickaxe", 2);
        this.setCreativeTab(KernCraft.KERNCRAFT_CREATIVE_TAB);

        setLightLevel(0f);
        setLightOpacity(1);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(POWERED, 0));

        setUnlocalizedName(unlocalizedName);
        setRegistryName(unlocalizedName);
    }

    /**
     * Light value is the value of the blockstate POWERED.
     * @param state
     * @param world
     * @param pos
     * @return
     */
    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(POWERED).intValue();
    }

    /**
     * The lamp block has a tile entity associated, that displays an UI and
     * allows basic configuration.
     * @param worldIn
     * @param meta
     * @return
     */
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new LampTileEntity();
    }

    /**
     *
     * @param state
     * @return
     */
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            player.openGui(KernCraft.instance, ModGuiHandler.LAMP_TILE_ENTITY_GUI,
                    world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    /**
     *
     */
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(KernCraft.MODID + ":"
                        + this.getUnlocalizedName().substring(5), "inventory"));
    }

    /**
     *
     * @param world
     * @param pos
     * @param neighbor
     */
    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);

        LampTileEntity tileentity = (LampTileEntity) world.getTileEntity(pos);
        tileentity.updateState();
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);

        LampTileEntity tileentity = (LampTileEntity) worldIn.getTileEntity(pos);
        tileentity.updateState();
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(KernCraftTileEntities.LAMP);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(KernCraftTileEntities.LAMP);
    }

    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(KernCraftTileEntities.LAMP);
    }


    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POWERED, meta);
    }


    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWERED).intValue();
    }

    protected BlockStateContainer createBlockState() {

        return new BlockStateContainer(this, POWERED);
    }

}
