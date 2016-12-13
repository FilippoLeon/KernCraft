package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.network.ModGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
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


    public static final PropertyInteger POWERED = PropertyInteger.create("lightlevel", 0, 15);

    protected LampBlockEntity(int ll) {
        super(Material.IRON);
        String name = "lamp";
        this.setUnlocalizedName(name);
        this.setHardness(2.0f);
        this.setResistance(6.0f);
        this.setHarvestLevel("pickaxe", 2);
        if (ll == 0) this.setCreativeTab(CreativeTabs.MISC);

        setLightLevel((float) ll / 15.0F);
        setLightOpacity(1);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(POWERED, ll));
        //this.setLightLevel(1.0F);

        setUnlocalizedName(name);
        setRegistryName(name);
        //GameRegistry.register(this);
        //GameRegistry.register(new ItemBlock(this), getRegistryName());
        //GameRegistry.registerTileEntity(LampTileEntity.class, getRegistryName().toString());
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(POWERED).intValue();
        //return super.getLightValue(state, world, pos);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new LampTileEntity();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand, ItemStack heldItem,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            player.openGui(KernCraft.instance, ModGuiHandler.LAMP_TILE_ENTITY_GUI,
                    world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(KernCraft.MODID + ":"
                        + this.getUnlocalizedName().substring(5), "inventory"));
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        if (!worldIn.isRemote) {
            //IBlockState iblockstate = worldIn.getBlockState(pos);
            //LampTileEntity tileentity = (LampTileEntity) worldIn.getTileEntity(pos);

            //setLightLevel(tileentity.redstoneMode);
            //if (this.isOn && !worldIn.isBlockPowered(pos))
            //{
            //    worldIn.scheduleUpdate(pos, this, 4);
            //}
            //else if (!this.isOn && worldIn.isBlockPowered(pos))
            //{
            //    setLightLevel(1);
            //worldIn.setBlockState(pos, Blocks.LIT_REDSTONE_LAMP.getDefaultState(), 2);
            //}
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            //IBlockState iblockstate = worldIn.getBlockState(pos);
            //LampTileEntity tileentity = (LampTileEntity) worldIn.getTileEntity(pos);

            //setLightOpacity(1);
            // setLightLevel(tileentity.redstoneMode);
            //if (this.isOn && !worldIn.isBlockPowered(pos))
            //{
            //   worldIn.setBlockState(pos, ModTileEntities.LAMP.getDefaultState(), 2);
            //}
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModTileEntities.LAMP_DEFAULT);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(ModTileEntities.LAMP_DEFAULT);
    }

    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(ModTileEntities.LAMP_DEFAULT);
    }


    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POWERED, meta);
    }


    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWERED).intValue();
    }

    protected BlockStateContainer createBlockState() {

        return new BlockStateContainer(this, POWERED);
    }

}
