package com.R3DKn16h7.kerncraft.plugins.JEI;

import mezz.jei.api.ISubtypeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * Created by Filippo on 20-Apr-17.
 */
public class ElementJEISubtypeInterpreter implements ISubtypeRegistry.ISubtypeInterpreter {

    @Nullable
    @Override
    public String getSubtypeInfo(ItemStack itemStack) {
        if ( itemStack.hasTagCompound()) {
            NBTTagCompound nbt =  itemStack.getTagCompound();
            if (nbt.hasKey("Element")) {
                return String.format("%d", nbt.getInteger("Element"));
            }
        }
        return "empty";
    }
}
