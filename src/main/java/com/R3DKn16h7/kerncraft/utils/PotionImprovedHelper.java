package com.R3DKn16h7.kerncraft.utils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Created by Filippo on 10/12/2016.
 */
public class PotionImprovedHelper {
    public static final int SPECTRAL = 24;
    public static final int POISON = 19;
    public static final int WITHER = 20;
    public static final int HASTE = 1;

    public static Potion getPotion(int id) {
        return Potion.getPotionById(id);
    }

    public static PotionEffect getPotionEffect(int id, int duration) {
        return new PotionEffect(getPotion(id), duration);
    }

    public static PotionEffect hasPotionEffect(EntityLivingBase entity, int id) {
        return entity.getActivePotionEffect(getPotion(id));
    }

    public static void addPotionEffect(EntityLivingBase entity, int id, int duration) {
        entity.addPotionEffect(
                PotionImprovedHelper.getPotionEffect(id, duration)
        );
    }
}
