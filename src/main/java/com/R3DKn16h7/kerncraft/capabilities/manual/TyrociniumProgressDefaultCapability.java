package com.R3DKn16h7.kerncraft.capabilities.manual;

import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.MessageSyncTyrociniumProgress;
import com.R3DKn16h7.kerncraft.network.MessageUnlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Filippo on 19-Apr-17.
 */
public class TyrociniumProgressDefaultCapability
        implements ITyrociniumProgressCapability {

    @CapabilityInject(ITyrociniumProgressCapability.class)
    public static Capability<ITyrociniumProgressCapability> INSTANCE = null;

    Map<String, Boolean> lockingMap = new HashMap<>();

    @Override
    public void setContentLock(String content, boolean lock) {
        lockingMap.put(content, lock);
    }

    @Override
    public void unlockContent(String content) {
        lockingMap.put(content, true);
    }

    @Override
    public boolean isContentLocked(String content) {
        if (lockingMap.containsKey(content)) {
            return !lockingMap.get(content);
        }
        return true;
    }

    @Override
    public boolean isContentUnlocked(String content) {
        if (lockingMap.containsKey(content)) {
            return lockingMap.get(content);
        }
        return false;
    }

    @Override
    public Map<String, Boolean> getMap() {
        return lockingMap;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return INSTANCE != null && capability == INSTANCE;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (INSTANCE != null && capability == INSTANCE) return INSTANCE.cast(this);
        return null;
    }


    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        for (Map.Entry<String, Boolean> entry : lockingMap.entrySet()) {
            nbt.setBoolean(entry.getKey(), entry.getValue());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        for (String key : nbt.getKeySet()) {
            lockingMap.put(key, nbt.getBoolean(key));
        }
    }

    @Override
    public void clone(ITyrociniumProgressCapability clone) {
        if (clone instanceof TyrociniumProgressDefaultCapability) {
            this.lockingMap = new HashMap<>(((TyrociniumProgressDefaultCapability) clone).lockingMap);
        }
    }

    @Override
    public void unpackMessage(MessageSyncTyrociniumProgress message) {
        lockingMap = new HashMap<>(message.unlocksMap);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void sync() {
        KernCraftNetwork.networkWrapper.sendToServer(new MessageUnlock("sync"));
    }
}
