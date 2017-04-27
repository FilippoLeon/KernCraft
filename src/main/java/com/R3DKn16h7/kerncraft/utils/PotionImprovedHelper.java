package com.R3DKn16h7.kerncraft.utils;

import com.R3DKn16h7.kerncraft.potions.KernCraftPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Created by Filippo on 10/12/2016.
 */
public class PotionImprovedHelper {
    public static final int SPEED = 1;
    public static final int SLOWNESS = 2;
    public static final int HASTE = 3;
    public static final int MINING_FATIGUE = 4;
    public static final int STRENGTH = 5;
    public static final int INTANT_HEALTH = 6;
    public static final int INSTANT_DAMAGE = 7;
    public static final int JUMP_BOOST = 8;
    public static final int NAUSEA = 9;
    public static final int REGENERATION = 10;
    public static final int RESISTANCE = 11;
    public static final int FIRE_RESISTANCE = 12;
    public static final int WATER_BREATHING = 13;
    public static final int INVISIBILITY = 14;
    public static final int BLINDNESS = 15;
    public static final int NIGHT_VISION = 16;
    public static final int HUNGER = 17;
    public static final int WEAKNESS = 18;
    public static final int POISON = 19;
    public static final int WITHER = 20;
    public static final int HEALTH_BOOST = 21;
    public static final int ABSORPTION = 22;
    public static final int SATURATION = 23;
    public static final int SPECTRAL = 24;
    public static final int LEVITATION = 25;
    public static final int LUCK = 26;
    public static final int UNLUCK = 27;
    public static final int RANDOM_TELEPORT = 101;

    public static Potion getPotion(int id) {
        if (id < 100) {
            return Potion.getPotionById(id);
        } else {
            return KernCraftPotions.RANDOM_TELEPORT;
        }
    }

    public static PotionEffect getPotionEffect(int id, int duration) {
        return new PotionEffect(getPotion(id), duration);
    }

    public static PotionEffect getPotionEffect(int id, int duration, int amplifier, boolean particle) {
        return new PotionEffect(getPotion(id), duration, amplifier, false, particle);
    }

    public static PotionEffect hasPotionEffect(EntityLivingBase entity, int id) {
        return entity.getActivePotionEffect(getPotion(id));
    }

    public static void addPotionEffect(EntityLivingBase entity, int id, int duration) {
        entity.addPotionEffect(
                PotionImprovedHelper.getPotionEffect(id, duration, 0, false)
        );
    }

    public static void addPotionEffect(EntityLivingBase entity, int id, int duration, int amplifier) {
        entity.addPotionEffect(
                PotionImprovedHelper.getPotionEffect(id, duration, amplifier, false)
        );
    }

    public static boolean hasPotionEffectBool(EntityLivingBase player, int effect) {
        return player.getActivePotionEffect(getPotion(effect)) != null;
    }
}
