package com.R3DKn16h7.kerncraft.items;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.capabilities.energy.EnergyContainer;
import com.R3DKn16h7.kerncraft.capabilities.energy.EnergyContainerItemCapabilityProvider;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Filippo on 18-Apr-17.
 */
//@Optional.Interface( iname = IntegrationType.RF, iface = "cofh.api.energy.IEnergyContainerItem" )
//@Optional.Interface( iname = IntegrationType.RF, iface = "cofh.api.energy.IEnergyReceiver" )
//@Optional.Interface( iname = IntegrationType.RF, iface = "cofh.api.energy.IEnergyReceiver" )
public abstract class EnergyContainerItem extends Item {

    public EnergyContainerItem() {

        this.setUnlocalizedName(getName());
        this.setRegistryName(getName());
        this.setCreativeTab(KernCraft.KERNCRAFT_CREATIVE_TAB);
        this.setMaxStackSize(1);
        GameRegistry.register(this);
    }

    abstract public String getName();

    protected abstract int getMaxInput();

    protected abstract int getMaxOutput();

    protected abstract int getCapacity();


    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {

        NBTTagCompound nbt = stack.getTagCompound();

        IEnergyStorage cap = stack.getCapability(CapabilityEnergy.ENERGY, null);

        NBTTagCompound nbt2 = ((EnergyContainer) cap).serializeNBT();
        if (nbt == null) {
            nbt = new NBTTagCompound();
        }
        nbt.setTag("capabilityEnergy", nbt2);

        return nbt;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (worldIn.isRemote) {
            IEnergyStorage cap = stack.getCapability(CapabilityEnergy.ENERGY, null);
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("capabilityEnergy")) {
                ((EnergyContainer) cap).deserializeNBT(stack.getTagCompound().getCompoundTag("capabilityEnergy"));
            }
        }


        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

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
    public void addInformation(ItemStack stack, EntityPlayer playerIn,
                               List<String> tooltip, boolean advanced) {

        // TODO: custom info
        TeslaUtils.createTooltip(stack, tooltip);
    }


    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack,
                                                NBTTagCompound nbt) {
        return new EnergyContainerItemCapabilityProvider(getCapacity(), getMaxInput(), getMaxOutput());

    }


}
