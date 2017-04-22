package com.R3DKn16h7.kerncraft.items;

import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.TeslaUtils;
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
        @Optional.Interface(modid = "tesla", iface = "net.darkhax.tesla.api.ITeslaHolder"),
        @Optional.Interface(modid = "tesla", iface = "net.darkhax.tesla.api.ITeslaConsumer")
})
public abstract class EnergyContainerItem extends Item {
    static final int CAPACITY = 1000;

    @Override
    @Optional.Method(modid = "tesla")
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, null)) {
            ITeslaHolder cap = stack.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null);
            return (1. - (double) cap.getStoredPower() / (double) cap.getCapacity());
        }
        return 0;
    }

    @Override
    @Optional.Method(modid = "tesla")
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    @Optional.Method(modid = "tesla")
    public void addInformation (ItemStack stack, EntityPlayer playerIn,
                                List<String> tooltip, boolean advanced) {
        TeslaUtils.createTooltip(stack, tooltip);
    }


    @Override
    @Optional.Method(modid = "tesla")
    public ICapabilityProvider initCapabilities(ItemStack stack,
                                                NBTTagCompound nbt) {
        return new TeslaContainerItem(new BaseTeslaContainer(),
                CAPACITY, 100, 100);
    }
}
