package com.R3DKn16h7.kerncraft.capabilities;

import net.minecraft.entity.Entity;
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

    /**
     * Whenever somebody add an element to the canister, we must notify the canister of the
     *
     * @param id
     * @param amount
     * @param simulate
     * @param adder    Whoever is in charge of adding the element to the canister.
     * @return
     */
    int addAmountOf(int id, int amount, boolean simulate, Entity adder);

    /**
     * Only indended to create full stack without exploding in face.
     * <p>
     * Pass null to addAmountOf for Entities instead.
     *
     * @param id
     * @param amount
     * @param simulate
     * @return
     */
    @Deprecated
    int addAmountOf(int id, int amount, boolean simulate);

    int removeAmountOf(int id, int amount, boolean simulate);

    int getMaxNumberOfElements();

    Map<Integer, Integer> getElementMap();

    void removeAllOf(int id);
}
