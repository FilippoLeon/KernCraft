package com.R3DKn16h7.kerncraft.capabilities.element;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.capabilities.energy.EnergyContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.CapabilityEnergy;

/**
 * Created by filippo on 06-May-17.
 */
public class ElementContainerAndEnergyProvider implements
        IElementContainerCapability,
        INBTSerializable<NBTTagCompound>,
        ICapabilityProvider {

    public final ElementContainerDefaultCapability elementContainer;
    public final EnergyContainer container;

    public ElementContainerAndEnergyProvider(int maxNumElements, int maxCapacity,
                                             long maxEnergyCapacity,
                                             long input, long output) {
        elementContainer = new ElementContainerDefaultCapability(maxNumElements,
                maxCapacity);
        container = new EnergyContainer(maxEnergyCapacity, input, output);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setTag("elementCapability", this.elementContainer.serializeNBT());
        nbt.setTag("energyCapability", this.container.serializeNBT());

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.elementContainer.deserializeNBT(nbt.getCompoundTag("elementCapability"));
        this.container.deserializeNBT(nbt.getCompoundTag("energyCapability"));
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

        if (capability == ElementCapabilities.CAPABILITY_ELEMENT_CONTAINER)
            return true;
        if (KernCraft.FOUND_TESLA) {
            if (capability == TeslaCapabilities.CAPABILITY_PRODUCER
                    && this.container.isTeslaProducer())
                return true;
            else if (capability == TeslaCapabilities.CAPABILITY_HOLDER)
                return true;
            else if (capability == TeslaCapabilities.CAPABILITY_CONSUMER
                    && this.container.isTeslaConsumer())
                return true;
        } else if (capability == CapabilityEnergy.ENERGY) return true;

        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (
                (KernCraft.FOUND_TESLA
                        && (capability == TeslaCapabilities.CAPABILITY_CONSUMER
                        || capability == TeslaCapabilities.CAPABILITY_HOLDER
                        || capability == TeslaCapabilities.CAPABILITY_PRODUCER))
                        || capability == CapabilityEnergy.ENERGY) {
            return (T) this.container;
        } else if (capability == ElementCapabilities.CAPABILITY_ELEMENT_CONTAINER) {
            return (T) this.elementContainer;
        }

        return null;
    }
}
