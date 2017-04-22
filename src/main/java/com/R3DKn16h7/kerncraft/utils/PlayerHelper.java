package com.R3DKn16h7.kerncraft.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

/**
 * Created by Filippo on 22-Apr-17.
 */
public class PlayerHelper {

    static public EnumHand otherHand(EnumHand hand) {
        return hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isCtrlKeyDown() {
        boolean isCtrlKeyDown = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)
                || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        if (!isCtrlKeyDown && Minecraft.IS_RUNNING_ON_MAC)
            isCtrlKeyDown = Keyboard.isKeyDown(Keyboard.KEY_LMETA)
                    || Keyboard.isKeyDown(Keyboard.KEY_RMETA);
        return isCtrlKeyDown;
    }
}
