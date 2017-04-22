package com.R3DKn16h7.kerncraft.utils;

import net.minecraft.util.EnumHand;

/**
 * Created by Filippo on 22-Apr-17.
 */
public class PlayerHelper {

    static public EnumHand otherHand(EnumHand hand) {
        return hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }
}
