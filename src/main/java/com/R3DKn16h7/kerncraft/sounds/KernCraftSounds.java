package com.R3DKn16h7.kerncraft.sounds;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/**
 * Created by Filippo on 26-Apr-17.
 */
public class KernCraftSounds {
    public static SoundEvent CENTRIFUGE_SPIN_UP;
    public static SoundEvent CENTRIFUGE;
    public static SoundEvent CENTRIFUGE_SPIN_DOWN;

    public KernCraftSounds() {
        ResourceLocation location =
                new ResourceLocation("kerncraft", "centrifuge");
        CENTRIFUGE = new SoundEvent(location);
        location =
                new ResourceLocation("kerncraft", "centrifuge_spin_up");
        CENTRIFUGE_SPIN_UP = new SoundEvent(location);
        location =
                new ResourceLocation("kerncraft", "centrifuge_spin_down");
        CENTRIFUGE_SPIN_DOWN = new SoundEvent(location);
    }
}
