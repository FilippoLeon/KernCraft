package com.R3DKn16h7.quantumbase.items;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * A beacon that doesn't need to be placed in the world.
 * TODO: baubles+COfH compoatibility
 *
 * @author R3DKn16h7
 */
public class PortableBeacon extends Item {
    public static String unlocalizedName = "portable_beacon";

    public PortableBeacon() {
        super();

        this.setUnlocalizedName(unlocalizedName);
        this.setRegistryName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.MISC);
        GameRegistry.register(this);
    }

    /**
     * Called each tick as long the item is on a player inventory.
     * Used to restore potion effect.
     */
    @Override
    public void onUpdate(ItemStack stack, World worldIn,
                         Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn != null && entityIn instanceof EntityPlayer)
            if (((EntityPlayer) entityIn).getActivePotionEffect(Potion.getPotionById(1)) == null)
                ((EntityPlayer) entityIn).addPotionEffect(new PotionEffect(Potion.getPotionById(1), 300));
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player,
                                      World world, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {

        // TODO DEPRECATED
        if (!world.isRemote) {
            player.addPotionEffect(new PotionEffect(Potion.getPotionById(1), 1000));
            System.out.println("Whatever Text");
            Minecraft.getMinecraft().thePlayer.sendChatMessage("Lol what a noob");
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        // TODO DEPRECATED
        if (!player.worldObj.isRemote) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                if (entityLivingBase instanceof IMob) {
                    IMob mod = (IMob) entityLivingBase;
                    entityLivingBase.addPotionEffect(new PotionEffect(Potion.getPotionById(20), 1000));
                }
            }
            return true;
        }
        return false;
    }

    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4,
                             int par5, int par6, int par7, float par8, float par9, float par10) {
        // if (!world.isRemote) {
        // Block block = world.getBlockState(pos).getBlock();
        // if (block == ModBlocks.lightBlock) {
        // world.setBlockToAir(pos);
        // return true;
        // }
        //
        // if (!checkUsage(stack, player, 1.0f)) {
        // return true;
        // }
        //
        // world.setBlockState(pos.offset(side),
        // ModBlocks.lightBlock.getDefaultState(), 3);
        //
        // registerUsage(stack, player, 1.0f);
        // }
        System.out.println("Whatever Text");
        Minecraft.getMinecraft().thePlayer.sendChatMessage("Lol what a noob");
        return true;
    }

    // public static final void init() {
    // item = new Item()
    // .setUnlocalizedName("tutorialItem")
    // .setRegistryName("portable_beacon")
    // .setCreativeTab(CreativeTabs.MISC);
    // GameRegistry.register(item);
    // }


}
