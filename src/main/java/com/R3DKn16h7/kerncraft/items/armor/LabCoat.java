package com.R3DKn16h7.kerncraft.items.armor;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by filippo on 26/11/16.
 */
public class LabCoat extends ItemArmor {

    public static ArmorMaterial ARMOR = EnumHelper.addArmorMaterial("INFUSED_ARMOR",
            "kerncraft:labcoat", 200, new int[]{4, 9, 7, 4}, 30,
            SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 5.0F);

    public static String[] names = {"boots", "leggings", "chestplate", "helmet" };

    public LabCoat(EntityEquipmentSlot armorType) {
        super(ARMOR, 0, armorType);
        setCreativeTab(CreativeTabs.COMBAT);

        String str = "labcoat_" + names[armorType.getIndex()]; //armorType.getName();
        setUnlocalizedName(str);
        setRegistryName(str);

        GameRegistry.register(this);
    }

    public void setColor(ItemStack stack, int color) {
        NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound == null) {
            nbttagcompound = new NBTTagCompound();
            stack.setTagCompound(nbttagcompound);
        }

        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

        if (!nbttagcompound.hasKey("display", 10)) {
            nbttagcompound.setTag("display", nbttagcompound1);
        }

        nbttagcompound1.setInteger("color", color);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn,
                                                    EntityPlayer playerIn,
                                                    EnumHand hand) {
        this.setColor(playerIn.getHeldItem(hand), 0x00FF0000);
        return super.onItemRightClick(worldIn, playerIn, hand);
    }

    /**
     * Determines if this armor will be rendered with the secondary 'overlay' texture.
     * If this is true, the first texture will be rendered using a tint of the color
     * specified by getColor(ItemStack)
     *
     * @param stack The stack
     * @return true/false
     */
    public boolean hasOverlay(ItemStack stack) {
        return true;
    }


}
