package com.R3DKn16h7.kerncraft.items;

import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

import java.util.List;

/**
 * Created by Filippo on 18-Apr-17.
 */
//@Optional.Interface( iname = IntegrationType.RF, iface = "cofh.api.energy.IEnergyContainerItem" )
//@Optional.Interface( iname = IntegrationType.RF, iface = "cofh.api.energy.IEnergyReceiver" )
//@Optional.Interface( iname = IntegrationType.RF, iface = "cofh.api.energy.IEnergyReceiver" )
public abstract class EnergyContainerItem extends Item {

    protected abstract int getMaxInput();

    protected abstract int getMaxOutput();

    protected abstract int getCapacity();

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
            IEnergyStorage cap = stack.getCapability(CapabilityEnergy.ENERGY, null);
            return (1. - (double) cap.getEnergyStored() / (double) cap.getMaxEnergyStored());
        }
        return 0;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    @Optional.Method(modid = "tesla")
    public void addInformation (ItemStack stack, EntityPlayer playerIn,
                                List<String> tooltip, boolean advanced) {
        // TODO: custom info
        TeslaUtils.createTooltip(stack, tooltip);
    }


    @Override
    @Optional.Method(modid = "tesla")
    public ICapabilityProvider initCapabilities(ItemStack stack,
                                                NBTTagCompound nbt) {
        return new EnergyContainerItemCapabilityProvider(getCapacity(), getMaxInput(), getMaxOutput());
    }


}
