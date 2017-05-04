package com.R3DKn16h7.kerncraft.capabilities.energy;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

/**
 * Created by Filippo on 24-Apr-17.
 */
@Optional.InterfaceList({
        @Optional.Interface(modid = "tesla", iface = "net.darkhax.tesla.api.ITeslaHolder"),
        @Optional.Interface(modid = "tesla", iface = "net.darkhax.tesla.api.ITeslaConsumer"),
        @Optional.Interface(modid = "tesla", iface = "net.darkhax.tesla.api.ITeslaProducer")
})
public class EnergyContainer
        implements IEnergyStorage,
        ITeslaProducer, ITeslaConsumer, ITeslaHolder,
        INBTSerializable<NBTTagCompound> {

    long amount = 0;
    private long maxCapacity;
    private long input;
    private long output;

    public EnergyContainer(long maxCapacity, long input, long output) {

        this.maxCapacity = maxCapacity;
        this.input = input;
        this.output = output;
    }

    public EnergyContainer(NBTTagCompound nbt) {
        this.deserializeNBT(nbt);
    }

    public boolean isTeslaConsumer() {
        return input > 0;
    }

    public boolean isTeslaProducer() {
        return output > 0;
    }

    public long getInputRate() {

        return this.input;
    }

    public EnergyContainer setInputRate(long rate) {
        this.input = rate;
        return this;
    }

    public long getOutputRate() {
        return this.output;
    }

    public EnergyContainer setOutputRate(long rate) {
        this.output = rate;
        return this;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return (int) givePower(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return ((int) takePower(maxExtract, simulate));
    }

    @Override
    public int getEnergyStored() {
        return ((int) getStoredPower());
    }

    @Override
    public int getMaxEnergyStored() {
        return ((int) getCapacity());
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    @Override
    public long givePower(long power, boolean simulated) {
        // From Tesla API
        final long acceptedTesla = Math.min(this.getCapacity() -
                this.amount, this.getInputRate());

        if (!simulated) {
            this.amount += acceptedTesla;
        }

        return acceptedTesla;
    }

    @Override
    public long getStoredPower() {
        return amount;
    }

    @Override
    public long getCapacity() {
        return this.maxCapacity;
    }

    @Override
    public long takePower(long power, boolean simulated) {
        // From Tesla API
        final long removedPower = Math.min(this.amount, this.getOutputRate());

        if (!simulated) {
            this.amount -= removedPower;
        }

        return removedPower;
    }

    @Override
    public NBTTagCompound serializeNBT() {

        final NBTTagCompound nbt = new NBTTagCompound();

        nbt.setLong("power_amount", this.amount);
        nbt.setLong("power_max_capacity", this.maxCapacity);
        nbt.setLong("power_max_input", this.input);
        nbt.setLong("power_max_output", this.output);

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

        if (nbt.hasKey("power_amount")) {
            this.amount = nbt.getLong("power_amount");
        }

        if (nbt.hasKey("power_max_capacity")) {
            this.maxCapacity = nbt.getLong("power_max_capacity");
        }

        if (nbt.hasKey("power_max_input")) {
            this.input = nbt.getLong("power_max_input");
        }

        if (nbt.hasKey("power_max_output")) {
            this.output = nbt.getLong("power_max_output");
        }

        if (this.amount > this.getCapacity()) {
            this.amount = this.getCapacity();
        }
    }

    public void fill() {
        this.amount = getCapacity();
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
