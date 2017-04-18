package com.R3DKn16h7.kerncraft.items;

import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaConsumer;

import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Optional;

import java.util.List;

/**
 * Created by Filippo on 18-Apr-17.
 */
//@Optional.Interface( iname = IntegrationType.RF, iface = "cofh.api.energy.IEnergyContainerItem" )
//@Optional.Interface( iname = IntegrationType.RF, iface = "cofh.api.energy.IEnergyReceiver" )
@Optional.InterfaceList({
        @Optional.Interface(modid = "Tesla", iface = "net.darkhax.tesla.api.ITeslaHolder"),
        @Optional.Interface(modid = "Tesla", iface = "net.darkhax.tesla.api.ITeslaConsumer")
})
public abstract class EnergyContainerItem extends Item implements
        ITeslaHolder, ITeslaConsumer {
//        IEnergyContainerItem, IEnergyReceiver {
    int storedPower = 200;
    int maxPower = 200;

    public EnergyContainerItem() {

    }

    @Override
    @Optional.Method(modid = "Tesla")
    public long getCapacity() {
        return maxPower;
    }

    @Override
    @Optional.Method(modid = "Tesla")
    public long getStoredPower() {
        return storedPower;
    }

    @Override
    @Optional.Method(modid = "Tesla")
    public long givePower(long power, boolean simulated) {
        if(storedPower + power > maxPower) {
            if(!simulated) storedPower = maxPower;
            return maxPower - storedPower;
        } else if (simulated) {
            return power;
        } else {
            storedPower += power;
            return power;
        }
    }

    @Override
    @Optional.Method(modid = "Tesla")
    public double getDurabilityForDisplay(ItemStack stack) {
        return (1. - (double) storedPower / (double) maxPower);
    }

    @Override
    @Optional.Method(modid = "Tesla")
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    @Optional.Method(modid = "Tesla")
    public void addInformation (ItemStack stack, EntityPlayer playerIn,
                                List<String> tooltip, boolean advanced) {

        tooltip.add("Energy: " + storedPower + "/" + maxPower);
    }


    @Override
    @Optional.Method(modid = "Tesla")
    public ICapabilityProvider initCapabilities(ItemStack stack,
                                                NBTTagCompound nbt)
    {
        return new TeslaContainerItem(new BaseTeslaContainer(),
                maxPower, 100, 100);
    }
}
