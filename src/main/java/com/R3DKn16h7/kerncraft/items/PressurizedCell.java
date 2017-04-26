package com.R3DKn16h7.kerncraft.items;

import com.R3DKn16h7.kerncraft.capabilities.ElementContainerProvider;
import com.R3DKn16h7.kerncraft.elements.Element;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class PressurizedCell extends AbstractElementContainerItem {
    public PressurizedCell() {
        super();
    }

    public String getName() {
        return "cell";
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        ElementContainerProvider cap = new ElementContainerProvider(1, 10000);
        cap.elementContainer.removeAcceptedState(Element.State.SOLID);
        cap.elementContainer.removeAcceptedState(Element.State.UNKNOWN);
        cap.elementContainer.removeAcceptedState(Element.State.LIQUID);
        return cap;
    }
}