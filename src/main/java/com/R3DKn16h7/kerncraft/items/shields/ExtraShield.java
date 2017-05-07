package com.R3DKn16h7.kerncraft.items.shields;

import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ExtraShield extends Item {
    public static String unlocalizedName;

    public ExtraShield(String name) {
        super();
        unlocalizedName = name;

        this.setUnlocalizedName(unlocalizedName);
        this.setRegistryName(unlocalizedName);
        GameRegistry.register(this);

        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.setMaxDamage(336);
        this.addPropertyOverride(new ResourceLocation("blocking"),
                new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn,
                               @Nullable EntityLivingBase entityIn) {
                return entityIn != null
                        && entityIn.isHandActive()
                        && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });


        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn,
                               List<String> tooltip, boolean advanced) {
        ItemBanner.appendHoverTextFromTileEntityTag(stack, tooltip);
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        ItemStack itemstack = new ItemStack(itemIn, 1, 0);
        subItems.add(itemstack);
    }

    @SideOnly(Side.CLIENT)
    public CreativeTabs getCreativeTab() {
        return CreativeTabs.COMBAT;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BLOCK;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn,
                                                    EntityPlayer playerIn, EnumHand handIn) {

        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == Item.getItemFromBlock(Blocks.PLANKS) || super.getIsRepairable(toRepair, repair);
    }
}