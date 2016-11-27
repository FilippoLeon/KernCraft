package com.R3DKn16h7.quantumbase.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

/**
 * Created by filippo on 26/11/16.
 */
public class LabCoat extends ItemArmor {

    public static ArmorMaterial ARMOR = EnumHelper.addArmorMaterial("INFUSED_ARMOR",
            "quantumbase:labcoat", 200, new int[] {4, 9, 7, 4}, 30,
            SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 5.0F);

    public static String[] names = {"boots", "leggings", "chestplate", "helmet" };

    protected LabCoat(EntityEquipmentSlot armorType) {
        super(ARMOR, 0, armorType);
        setCreativeTab(CreativeTabs.COMBAT);

        String str = "labcoat_" + names[armorType.getIndex()]; //armorType.getName();
        setUnlocalizedName(str);
        setRegistryName(str);

        GameRegistry.register(this);
    }


}
