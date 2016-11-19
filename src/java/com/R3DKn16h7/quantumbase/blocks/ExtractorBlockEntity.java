package com.R3DKn16h7.quantumbase.blocks;

import com.R3DKn16h7.quantumbase.QuantumBase;
import com.R3DKn16h7.quantumbase.entities.ExtractorTileEntity;
import com.R3DKn16h7.quantumbase.network.ModGuiHandler;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
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

    protected ExtractorBlockEntity(String unlocalizedName) {
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
        GameRegistry.register(new ItemBlock(this), getRegistryName());
        GameRegistry.registerTileEntity(ExtractorTileEntity.class, QuantumBase.MODID + ":" + "extractor");
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
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
    
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, 
                new ModelResourceLocation(getRegistryName(), "inventory"));

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
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            // ((ExtractorEntity) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
        }
    }


    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
//                                  if (this.world.isBlockPowered(pos)) {
                                  
//                                      setLightLevel(5f);
                                   //Do stuff here
//                                  }
//                                  world.notifyBlocksOfNeighborChange(x, y - 1, z,
//                                          blockID);

//                                  par1World.setBlockWithNotify(par2, par3, par4, your active block);
   }
}
