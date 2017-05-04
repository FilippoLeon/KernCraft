package com.R3DKn16h7.kerncraft.capabilities.manual;

import com.R3DKn16h7.kerncraft.capabilities.IClonable;
import com.R3DKn16h7.kerncraft.network.MessageSyncTyrociniumProgress;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

/**
 * Created by Filippo on 19-Apr-17.
 */
public interface ITyrociniumProgressCapability extends ICapabilitySerializable<NBTTagCompound>, IClonable<ITyrociniumProgressCapability> {

    void unlockContent(String content);

    void setContentLock(String content, boolean lock);

    boolean isContentLocked(String content);

    boolean isContentUnlocked(String content);

    Map<String, Boolean> getMap();

    void unpackMessage(MessageSyncTyrociniumProgress message);

    /**
     *
     */
    @SideOnly(Side.CLIENT)
    void sync();
}
