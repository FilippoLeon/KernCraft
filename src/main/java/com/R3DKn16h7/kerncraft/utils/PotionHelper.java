package com.R3DKn16h7.kerncraft.utils;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Created by Filippo on 10/12/2016.
 */
public class PotionHelper {
    public static Potion getPotion(int id) {
        return Potion.getPotionById(id);
    }

    public static PotionEffect getPotionEffect(int id, int duration) {
        return new PotionEffect(getPotion(id), duration);
    }

    public class Effect {
        public static final int SPECTRAL = 24;
        public static final int WITHER = 20;
        public static final int HASTE = 1;
    }
}
