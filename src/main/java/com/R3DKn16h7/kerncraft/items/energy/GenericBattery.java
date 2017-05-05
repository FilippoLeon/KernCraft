package com.R3DKn16h7.kerncraft.items.energy;

import com.R3DKn16h7.kerncraft.capabilities.element.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.element.ElementContainerProvider;
import com.R3DKn16h7.kerncraft.capabilities.element.IElementContainer;
import com.R3DKn16h7.kerncraft.items.EnergyContainerItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public class GenericBattery extends EnergyContainerItem {
    public GenericBattery() {
        super();
    }

    @Override
    public String getName() {
        return "generic_battery";
    }

    @Override
    protected int getMaxInput() {
        return 100;
    }

    @Override
    protected int getMaxOutput() {
        return 100;
    }

    @Override
    protected int getCapacity() {
        return 1000;
    }

    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
        NBTTagCompound nbt = super.getNBTShareTag(stack);

        IElementContainer cap = ElementCapabilities.getCapability(stack);

        NBTTagCompound nbt2 = cap.serializeNBT();
        if (nbt == null) {
            nbt = new NBTTagCompound();
        }
        nbt.setTag("capabilityElementContainer", nbt2);

        return nbt;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (worldIn.isRemote && ElementCapabilities.hasCapability(stack)) {
            IElementContainer cap = ElementCapabilities.getCapability(stack);
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("capabilityElementContainer")) {
                cap.deserializeNBT(stack.getTagCompound().getCompoundTag("capabilityElementContainer"));
            }
        }

        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {

            IEnergyStorage cap =
                    playerIn.getHeldItem(handIn).getCapability(CapabilityEnergy.ENERGY, null);

            cap.receiveEnergy(100, false);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn,
                               List<String> tooltip, boolean advanced) {
        ElementCapabilities.addTooltip(stack, tooltip);

        super.addInformation(stack, playerIn, tooltip, advanced);
    }


    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        ElementContainerProvider cap = new ElementContainerProvider(2, 1000);
        return cap;
    }
}