package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.items.upgrades.UpgradeItem;
import com.R3DKn16h7.kerncraft.tileentities.utils.Upgrade;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Filippo on 14/12/2016.
 */
public abstract class MachineBlock extends BlockContainer {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public int gui;
    public boolean has_gui = false;

    public MachineBlock(String unlocalizedName) {
        super(Material.IRON);

        this.setUnlocalizedName(unlocalizedName);
        this.setHardness(2.0f);
        this.setResistance(6.0f);
        this.setHarvestLevel("pickaxe", 2);
        this.setCreativeTab(KernCraft.KERNCRAFT_CREATIVE_TAB);
        this.hasTileEntity = true;

        setRegistryName(unlocalizedName);
        this.setDefaultState(this.blockState
                .getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));

    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {

        double x = pos.getX(), y = pos.getY(), z = pos.getZ();

        ItemStack te_stack = new ItemStack(this);

        NBTTagCompound nbt = new NBTTagCompound();
        MachineTileEntity te = (MachineTileEntity) world.getTileEntity(pos);
        te.stop();
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

    protected void setGui(int gui) {
        this.gui = gui;
        this.has_gui = true;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        // TODO: improve this one by generifying
        if (world.isRemote) return true;

        if (player.getHeldItem(hand) != ItemStack.EMPTY) {
            MachineTileEntity te = (MachineTileEntity) world.getTileEntity(pos);
            if (te == null) return false;
            ItemStack itemStack = player.getHeldItem(hand);
            if (itemStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)
                    || itemStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
                IFluidHandler cap = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                // If an item implements only non-item capability: fiiiiiiiiine
                if (cap == null) {
                    cap = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
                }

                if (te instanceof SmeltingTileEntity) {
                    SmeltingTileEntity smeltingTe = ((SmeltingTileEntity) te);

                    FluidStack fluid = cap.drain(Fluid.BUCKET_VOLUME, false);
                    if (fluid != null && fluid.amount >= Fluid.BUCKET_VOLUME) {
                        int try_fill = ((FluidTank) smeltingTe.tank.get(0)).fill(fluid, false);
                        if (try_fill >= fluid.amount) {
                            // FIXME
//                            Minecraft.getMinecraft().getSoundHandler().playSound(
//                                    PositionedSoundRecord.getMasterRecord(
//                                            new SoundEvent(new ResourceLocation("item.bucket.empty")),
//                                            1)
//                            );
                            ((FluidTank) smeltingTe.tank.get(0)).fill(fluid, true);
                            cap.drain(Fluid.BUCKET_VOLUME, true);
                            if (cap instanceof IFluidHandlerItem) {
                                player.setHeldItem(hand, ((IFluidHandlerItem) cap).getContainer());
                            }
                        }
                    } else {
                        FluidStack try_drain = ((FluidTank) smeltingTe.tank.get(0)).drain(Fluid.BUCKET_VOLUME, false);
                        if (try_drain != null && try_drain.amount >= Fluid.BUCKET_VOLUME) {
                            // FIXME
//                            Minecraft.getMinecraft().getSoundHandler().playSound(
//                                    PositionedSoundRecord.getMasterRecord(
//                                            new SoundEvent(new ResourceLocation("item.bucket.full")),
//                                            1)
//                            );
                            FluidStack stack = ((FluidTank) smeltingTe.tank.get(0)).drain(Fluid.BUCKET_VOLUME, true);

                            cap.fill(stack, true);
                            if (cap instanceof IFluidHandlerItem) {
                                player.setHeldItem(hand, ((IFluidHandlerItem) cap).getContainer());
                            }
                        }
                    }
                }

                world.scheduleBlockUpdate(pos, te.getBlockType(), 0, 0);
                te.markDirty();
                return true;
            } else if (player.getHeldItem(hand).hasCapability(CapabilityEnergy.ENERGY, null)) {
                IEnergyStorage cap = player.getHeldItem(hand).getCapability(CapabilityEnergy.ENERGY, null);
                if (te instanceof SmeltingTileEntity) {
                    int try_extract = cap.extractEnergy(200, true);
                    int try_insert = ((SmeltingTileEntity) te).storage.receiveEnergy(try_extract, true);

                    cap.extractEnergy(try_insert, false);
                    ((SmeltingTileEntity) te).storage.receiveEnergy(try_insert, false);
                }
                world.scheduleBlockUpdate(pos, te.getBlockType(), 0, 0);
                te.markDirty();
                return true;
            } else if (player.getHeldItem(hand).getItem() instanceof UpgradeItem) {
                Upgrade upgrade = UpgradeItem.getUpgrade(player.getHeldItem(hand));
                if (upgrade != null) {
                    if (te.AddUpgrade(upgrade)) {
                        player.getHeldItem(hand).splitStack(1);
                    }
                }
                return true;
            } else {
                try {
                    boolean interrupt = this.teslaTrasnsferEnergy(world, pos, ((SmeltingTileEntity) te), player, hand);
                    if (interrupt) return true;
                } catch (NoSuchMethodError e) {
                    // ignore
                }
            }
        }

        if (!world.isRemote && has_gui) {
            player.openGui(KernCraft.instance, gui,
                    world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Optional.Method(modid = "tesla")
    boolean teslaTrasnsferEnergy(World world, BlockPos pos,
                                 SmeltingTileEntity te,
                                 EntityPlayer player, EnumHand hand) {
        if (player.getHeldItem(hand).hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, null)) {
            ITeslaProducer cap = player.getHeldItem(hand)
                    .getCapability(TeslaCapabilities.CAPABILITY_PRODUCER, null);
            long try_extract = cap.takePower(200, true);
            int try_insert = te.storage.receiveEnergy(((int) try_extract), true);

            cap.takePower(try_insert, false);
            te.storage.receiveEnergy(try_insert, false);
            world.scheduleBlockUpdate(pos, te.getBlockType(), 0, 0);
            te.markDirty();
            return true;
        }
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn,
                                BlockPos pos, Block blockIn, BlockPos pos2) {
        if (!worldIn.isRemote) {
            if (!worldIn.isBlockPowered(pos)) {
                worldIn.scheduleUpdate(pos, this, 4);
                setLightLevel(15);

            } else if (worldIn.isBlockPowered(pos)) {
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

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean powered = false;
        if (meta > 8) {
            powered = true;
            meta -= 8;
        }
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWERED, powered);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex() + (state.getValue(POWERED) ? 8 : 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, POWERED);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos,
                                IBlockState state, EntityLivingBase placer,
                                ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING,
                placer.getHorizontalFacing().getOpposite()), 2);

        MachineTileEntity te = (MachineTileEntity) world.getTileEntity(pos);
        NBTTagCompound nbt = stack.getTagCompound();
        if (te != null) {
            te.restoreFromNBT(nbt);
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
    }
}
