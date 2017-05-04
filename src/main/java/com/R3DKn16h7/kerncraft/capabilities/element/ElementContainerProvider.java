package com.R3DKn16h7.kerncraft.capabilities.element;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Filippo on 22-Apr-17.
 */
public class ElementContainerProvider implements
        IElementContainerCapability {

    public final ElementContainerDefaultCapability elementContainer;

    public ElementContainerProvider() {
        elementContainer =
                new ElementContainerDefaultCapability();
    }

    public ElementContainerProvider(int maxNumElements, int maxCapacity) {
        elementContainer = new ElementContainerDefaultCapability(maxNumElements,
                maxCapacity);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability,
                                 @Nullable EnumFacing facing) {
        return capability == ElementCapabilities.CAPABILITY_ELEMENT_CONTAINER;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability,
                               @Nullable EnumFacing facing) {
        if (capability == ElementCapabilities.CAPABILITY_ELEMENT_CONTAINER)
            return (T) this.elementContainer;
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {

        return this.elementContainer.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

        this.elementContainer.deserializeNBT(nbt);
    }
}
