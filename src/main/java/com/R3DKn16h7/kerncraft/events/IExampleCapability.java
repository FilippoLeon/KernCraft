package com.R3DKn16h7.kerncraft.events;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * Created by Filippo on 19-Apr-17.
 */
public interface IExampleCapability {

    void unlockContent(int content);
    void unlockContent(int content, boolean unlock);

    boolean isContentLocked(int content);
}
