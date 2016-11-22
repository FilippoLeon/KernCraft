package com.R3DKn16h7.quantumbase.tileentities;

import com.R3DKn16h7.quantumbase.QuantumBase;
import com.R3DKn16h7.quantumbase.network.ModGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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

public class ExtractorBlockEntity extends BlockContainer {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public ExtractorBlockEntity(String unlocalizedName) {
        super(Material.IRON);
        this.setUnlocalizedName(unlocalizedName);
        this.setHardness(2.0f);
        this.setResistance(6.0f);
        this.setHarvestLevel("pickaxe", 2);
        this.setCreativeTab(CreativeTabs.MISC);
        this.isBlockContainer = true;

        setUnlocalizedName("extractor");
        setRegistryName("extractor");
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this).setRegistryName("extractor"));
        GameRegistry.registerTileEntity(ExtractorTileEntity.class, "extractor");

        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    public static void setState(boolean active, World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (active) {
            worldIn.setBlockState(pos, ModTileEntities.EXTRACTOR.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
            worldIn.setBlockState(pos, ModTileEntities.EXTRACTOR.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        } else {
            worldIn.setBlockState(pos, ModTileEntities.EXTRACTOR.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
            worldIn.setBlockState(pos, ModTileEntities.EXTRACTOR.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }


        if (tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        ExtractorTileEntity te = (ExtractorTileEntity) world.getTileEntity(pos);
        InventoryHelper.dropInventoryItems(world, pos, te);
        super.breakBlock(world, pos, blockstate);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new ExtractorTileEntity();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(QuantumBase.MODID + ":" + this.getUnlocalizedName().substring(5), "inventory"));

//        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
//        .register(Item.getItemFromBlock(this), 0,
//                new ModelResourceLocation(QuantumBase.MODID + ":" + this.getUnlocalizedName().substring(5),
//                        "inventory"));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand, ItemStack heldItem,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("Lol what a noob2");
            player.openGui(QuantumBase.instance, ModGuiHandler.MOD_TILE_ENTITY_GUI,
                    world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
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

            if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate1.isFullBlock()) {
                enumfacing = EnumFacing.SOUTH;
            } else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() && !iblockstate.isFullBlock()) {
                enumfacing = EnumFacing.NORTH;
            } else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() && !iblockstate3.isFullBlock()) {
                enumfacing = EnumFacing.EAST;
            } else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() && !iblockstate2.isFullBlock()) {
                enumfacing = EnumFacing.WEST;
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
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
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            // ((ExtractorEntity) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
        }
    }


    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
//                                  if (this.world.isBlockPowered(pos)) {

//                                      setLightLevel(5f);
        //Do stuff here
//                                  }
//                                  world.notifyBlocksOfNeighborChange(x, y - 1, z,
//                                          blockID);

//                                  par1World.setBlockWithNotify(par2, par3, par4, your active block);
    }
}
