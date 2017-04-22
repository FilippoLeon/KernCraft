package com.R3DKn16h7.kerncraft.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

/**
 * Created by Filippo on 22-Apr-17.
 */
public interface IElementContainer extends INBTSerializable<NBTTagCompound> {

    int getCapacity();

    int getTotalAmount();

    int getNumberOfElements();

    int[] getElements();

    int getAmountOf(int i);

    int addAmountOf(int id, int amount, boolean simulate);

    int removeAmountOf(int id, int amount, boolean simulate);

    int getMaxNumberOfElements();

    Map<Integer, Integer> getElementMap();

    void removeAllOf(int id);
}
