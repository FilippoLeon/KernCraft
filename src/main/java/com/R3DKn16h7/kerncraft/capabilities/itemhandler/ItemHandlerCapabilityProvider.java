package com.R3DKn16h7.kerncraft.capabilities.itemhandler;

/**
 * Created by Filippo on 18-Apr-17.
 */

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemHandlerCapabilityProvider
        implements INBTSerializable<NBTTagCompound>, ICapabilityProvider {
    public final ItemStackHandler container;

    public ItemHandlerCapabilityProvider(int maxCapacity) {
        container = new ItemStackHandler(maxCapacity);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this.container;
        }
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return this.container.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.container.deserializeNBT(nbt);
    }
}