package com.R3DKn16h7.kerncraft.items;

/**
 * Created by Filippo on 18-Apr-17.
 */
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public class TeslaContainerItem implements INBTSerializable<NBTTagCompound>, ICapabilityProvider
{
    private final BaseTeslaContainer container;
    private long maxCapacity;
    private long output;
    private long input;

    public TeslaContainerItem(BaseTeslaContainer container, long maxCapacity, long input, long output)
    {
        this.container = container;
        this.maxCapacity = maxCapacity;
        this.output = output;
        this.input = input;
        container.setCapacity(maxCapacity);
        container.setOutputRate(output);
        container.setInputRate(input);
    }

    @Override
    public boolean hasCapability (Capability<?> capability, EnumFacing facing)
    {
        return capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == TeslaCapabilities.CAPABILITY_HOLDER;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability (Capability<T> capability, EnumFacing facing)
    {
        if (capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == TeslaCapabilities.CAPABILITY_HOLDER)
            return (T) this.container;

        return null;
    }

    @Override
    public NBTTagCompound serializeNBT ()
    {
        return this.container.serializeNBT();
    }

    @Override
    public void deserializeNBT (NBTTagCompound nbt)
    {
        this.container.deserializeNBT(nbt);
    }
}