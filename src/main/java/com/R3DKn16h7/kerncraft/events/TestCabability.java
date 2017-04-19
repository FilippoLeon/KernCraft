package com.R3DKn16h7.kerncraft.events;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Filippo on 19-Apr-17.
 */
public class TestCabability implements IExampleCapability, ICapabilityProvider  { //}, ICapabilitySerializable<NBTBase> {

    @CapabilityInject(IExampleCapability.class)
    public static Capability<IExampleCapability> INSTANCE = null;

    boolean unlocked = false;

    @Override
    public void unlockContent(int content, boolean unlock) {
        unlocked = unlock;
    }
    @Override
    public void unlockContent(int content) {
        unlocked = true;
    }

    @Override
    public boolean isContentLocked(int content) {
        return unlocked;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return INSTANCE != null && capability == INSTANCE;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(INSTANCE != null && capability == INSTANCE) return INSTANCE.cast(this);
        return null;
    }

//
//    @Override
//    public NBTBase serializeNBT() {
//        return null;
//    }
//
//    @Override
//    public void deserializeNBT(NBTBase nbt) {
//
//    }
}
