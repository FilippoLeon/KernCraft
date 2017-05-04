package com.R3DKn16h7.kerncraft.capabilities.energy;

/**
 * Created by Filippo on 18-Apr-17.
 */

import com.R3DKn16h7.kerncraft.KernCraft;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.CapabilityEnergy;

public class EnergyContainerItemCapabilityProvider
        implements INBTSerializable<NBTTagCompound>, ICapabilityProvider {
    public final EnergyContainer container;

    public EnergyContainerItemCapabilityProvider(long maxCapacity, long input, long output) {
        container = new EnergyContainer(maxCapacity, input, output);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return (KernCraft.FOUND_TESLA && ((capability == TeslaCapabilities.CAPABILITY_PRODUCER && this.container.isTeslaProducer())
                || capability == TeslaCapabilities.CAPABILITY_HOLDER
                || (capability == TeslaCapabilities.CAPABILITY_CONSUMER && this.container.isTeslaConsumer())
        ) || capability == CapabilityEnergy.ENERGY);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (
                (KernCraft.FOUND_TESLA && (capability == TeslaCapabilities.CAPABILITY_CONSUMER
                        || capability == TeslaCapabilities.CAPABILITY_HOLDER
                        || capability == TeslaCapabilities.CAPABILITY_PRODUCER))
                        || capability == CapabilityEnergy.ENERGY) {
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