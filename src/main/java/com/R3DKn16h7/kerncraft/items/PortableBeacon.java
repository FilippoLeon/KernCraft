package com.R3DKn16h7.kerncraft.items;

import com.R3DKn16h7.kerncraft.utils.PotionHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Portable beacon that doesn't need to be placed in the world.
 * TODO: baubles+COFH compoatibility
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

    @Override
    public void onUpdate(ItemStack stack, World worldIn,
                         Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn != null && entityIn instanceof EntityPlayer)
            if (((EntityPlayer) entityIn).getActivePotionEffect(PotionHelper.getPotion(PotionHelper.Effect.HASTE)) == null)
                ((EntityPlayer) entityIn).addPotionEffect(PotionHelper.getPotionEffect(PotionHelper.Effect.HASTE, 300));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player,
                                      World world, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        // TODO DEPRECATED
        if (!world.isRemote) {
            player.addPotionEffect(PotionHelper.getPotionEffect(PotionHelper.Effect.HASTE, 300));
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        // TODO DEPRECATED
        if (!player.world.isRemote) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                if (entityLivingBase instanceof IMob) {
                    IMob mod = (IMob) entityLivingBase;
                    entityLivingBase.addPotionEffect(PotionHelper.getPotionEffect(PotionHelper.Effect.WITHER, 300));
                }
            }
            return true;
        }
        return false;
    }

}
