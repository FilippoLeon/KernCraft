package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.KernCraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by Filippo on 14/12/2016.
 */
public abstract class MachineBlock extends BlockContainer {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public static int gui = 0;
    public static boolean has_gui = false;

    public MachineBlock(String unlocalizedName) {
        super(Material.IRON);

        this.setUnlocalizedName(unlocalizedName);
        this.setHardness(2.0f);
        this.setResistance(6.0f);
        this.setHarvestLevel("pickaxe", 2);
        this.setCreativeTab(CreativeTabs.MISC);
        this.isBlockContainer = true;

        setRegistryName(unlocalizedName);
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this).setRegistryName(unlocalizedName));
        GameRegistry.registerTileEntity(ExtractorTileEntity.class, unlocalizedName);

        this.setDefaultState(this.blockState
                .getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        MachineTileEntity te = (MachineTileEntity) world.getTileEntity(pos);

        double x = pos.getX(), y = pos.getY(), z = pos.getZ();

        ItemStackHandler inv_in = te.getInput();
        ItemStackHandler inv_out = te.getOutput();

        ItemStack te_stack = new ItemStack(this);

        NBTTagCompound nbt = new NBTTagCompound();
        if (inv_in != null)
            nbt.setTag("Input", inv_in.serializeNBT());
        if (inv_out != null)
            nbt.setTag("Output", inv_out.serializeNBT());
        te_stack.setTagCompound(nbt);

        float f = world.rand.nextFloat() * 0.8F + 0.1F;
        float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
        float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
        EntityItem entityitem = new EntityItem(world,
                x + f, y + f1, z + f2, te_stack.copy());
        entityitem.motionX = world.rand.nextGaussian() * 0.05;
        entityitem.motionY = world.rand.nextGaussian() * 0.05 + 0.2;
        entityitem.motionZ = world.rand.nextGaussian() * 0.05;
        world.spawnEntityInWorld(entityitem);

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
        MachineBlock.gui = gui;
        has_gui = true;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand, ItemStack heldItem,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && has_gui) {
            player.openGui(KernCraft.instance, gui,
                    world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn,
                                BlockPos pos, Block blockIn) {
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

        ExtractorTileEntity te = (ExtractorTileEntity) world.getTileEntity(pos);
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null && nbt.hasKey("Input")) {
            te.input.deserializeNBT(nbt.getCompoundTag("Input"));
        }
        if (nbt != null && nbt.hasKey("Output")) {
            te.output.deserializeNBT(nbt.getCompoundTag("Output"));
        }
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
