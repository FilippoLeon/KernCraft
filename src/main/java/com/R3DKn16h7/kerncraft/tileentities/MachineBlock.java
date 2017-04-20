package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Filippo on 14/12/2016.
 */
public abstract class MachineBlock extends BlockContainer {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public int gui;
    public boolean has_gui = false;

    public MachineBlock(String unlocalizedName) {
        super(Material.IRON);

        this.setUnlocalizedName(unlocalizedName);
        this.setHardness(2.0f);
        this.setResistance(6.0f);
        this.setHarvestLevel("pickaxe", 2);
        this.setCreativeTab(CreativeTabs.MISC);
        this.isBlockContainer = true;

        setRegistryName(unlocalizedName);
        this.setDefaultState(this.blockState
                .getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {

        double x = pos.getX(), y = pos.getY(), z = pos.getZ();

        ItemStack te_stack = new ItemStack(this);

        NBTTagCompound nbt = new NBTTagCompound();
        MachineTileEntity te = (MachineTileEntity) world.getTileEntity(pos);
        nbt = te.writeToNBT(nbt);
        te_stack.setTagCompound(nbt);

        float f = world.rand.nextFloat() * 0.8F + 0.1F;
        float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
        float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
        EntityItem entityitem = new EntityItem(world,
                x + f, y + f1, z + f2, te_stack.copy());
        entityitem.motionX = world.rand.nextGaussian() * 0.05;
        entityitem.motionY = world.rand.nextGaussian() * 0.05 + 0.2;
        entityitem.motionZ = world.rand.nextGaussian() * 0.05;
        world.spawnEntity(entityitem);

        super.breakBlock(world, pos, blockstate);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {

        return EnumBlockRenderType.MODEL;
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(KernCraft.MODID + ":" +
                        this.getUnlocalizedName().substring(5), "inventory"));
    }

    void setGui(int gui) {
        this.gui = gui;
        this.has_gui = true;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        if(player.getHeldItem(hand).getItem() == Items.WATER_BUCKET) {
            MachineTileEntity te = (MachineTileEntity) world.getTileEntity(pos);
            if(te instanceof SmeltingTileEntity) {
                Minecraft.getMinecraft().getSoundHandler().playSound(
                        PositionedSoundRecord.getMasterRecord(
                                new SoundEvent(new ResourceLocation("item.bucket.empty")),
                                1)
                );
                FluidStack stack = new FluidStack(FluidRegistry.WATER, 1000);
                ((SmeltingTileEntity) te).tank.fill(stack, true);

            }
            player.setHeldItem(hand, new ItemStack(Items.BUCKET));


            world.scheduleBlockUpdate(pos, te.getBlockType(),0,0);
            te.markDirty();

            return true;
        } else if(player.getHeldItem(hand).getItem() == KernCraftItems.POTATO_BATTERY) {
            MachineTileEntity te = (MachineTileEntity) world.getTileEntity(pos);
            if(te instanceof SmeltingTileEntity) {
                ((SmeltingTileEntity) te).storage.receiveEnergy(100, false);
            }
            world.scheduleBlockUpdate(pos, te.getBlockType(),0,0);
            te.markDirty();
            return true;
        }
        if (!world.isRemote && has_gui) {
            player.openGui(KernCraft.instance, gui,
                    world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn,
                                BlockPos pos, Block blockIn, BlockPos pos2) {
        if (!worldIn.isRemote) {
            if (true && !worldIn.isBlockPowered(pos)) {
                worldIn.scheduleUpdate(pos, this, 4);
                setLightLevel(15);

            } else if (!false && worldIn.isBlockPowered(pos)) {
                worldIn.scheduleUpdate(pos, this, 4);
                setLightLevel(0);
            }
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.setDefaultFacing(worldIn, pos, state);
    }

    private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            IBlockState iblockstate = worldIn.getBlockState(pos.north());
            IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
            IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
            IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
            EnumFacing enumfacing = state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() &&
                    !iblockstate1.isFullBlock()) {
                enumfacing = EnumFacing.SOUTH;
            } else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() &&
                    !iblockstate.isFullBlock()) {
                enumfacing = EnumFacing.NORTH;
            } else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() &&
                    !iblockstate3.isFullBlock()) {
                enumfacing = EnumFacing.EAST;
            } else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() &&
                    !iblockstate2.isFullBlock()) {
                enumfacing = EnumFacing.WEST;
            }
            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }
    }

    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }


    @Override
    public void onBlockPlacedBy(World world, BlockPos pos,
                                IBlockState state, EntityLivingBase placer,
                                ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING,
                placer.getHorizontalFacing().getOpposite()), 2);

        MachineTileEntity te = (MachineTileEntity) world.getTileEntity(pos);
        NBTTagCompound nbt = stack.getTagCompound();
        te.restoreFromNBT(nbt);
        if (stack.hasDisplayName()) {
            // ((ExtractorEntity) worldIn.getTileEntity(pos))
            // .setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
    }
}
