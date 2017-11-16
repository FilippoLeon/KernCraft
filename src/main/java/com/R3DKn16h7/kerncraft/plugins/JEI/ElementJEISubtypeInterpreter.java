package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.capabilities.element.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.element.IElementContainer;
import mezz.jei.api.ISubtypeRegistry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Created by Filippo on 20-Apr-17.
 */
public class ElementJEISubtypeInterpreter implements ISubtypeRegistry.ISubtypeInterpreter {

    @Nullable
    @Override
    public String apply(ItemStack itemStack) {
        if (ElementCapabilities.hasCapability(itemStack)) {
            IElementContainer cap = ElementCapabilities.getCapability(itemStack);
            if (cap.getNumberOfElements() <= 0) return "empty";
            return String.format("%d",
                    ElementCapabilities.getFirstElement(cap).id
            );
        }
        return "empty";
    }
}
