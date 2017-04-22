package com.R3DKn16h7.kerncraft.capabilities;

import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Filippo on 22-Apr-17.
 */
public class ElementContainerDefaultCapability
        implements IElementContainer {

    Map<Integer, Integer> containedElements;
    int maxNumElements;
    private int maxCapacity;

    public ElementContainerDefaultCapability() {
        this(1, 1000);
    }

    public ElementContainerDefaultCapability(int maxNumElements,
                                             int maxCapacity) {
        containedElements = new HashMap<>();
        this.maxNumElements = maxNumElements;
        this.maxCapacity = maxCapacity;
    }

    public ElementContainerDefaultCapability(NBTTagCompound dataTag) {

        this.deserializeNBT(dataTag);
    }

    @Override
    public NBTTagCompound serializeNBT() {

        final NBTTagCompound nbt = new NBTTagCompound();

        NBTTagCompound nbt2 = new NBTTagCompound();
        for (Map.Entry key : containedElements.entrySet()) {
            nbt2.setInteger(key.getKey().toString(), ((Integer) key.getValue()));
        }
        nbt.setTag("elements", nbt2);

        nbt.setInteger("maxElementQuantity", this.maxNumElements);
        nbt.setInteger("maxElementCapacity", this.maxCapacity);

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.maxCapacity = nbt.getInteger("maxElementCapacity");
        this.maxNumElements = nbt.getInteger("maxElementQuantity");
        NBTTagCompound nbt2 = nbt.getCompoundTag("elements");
        for (String key : nbt2.getKeySet()) {
            containedElements.put(Integer.parseInt(key), nbt2.getInteger(key));
        }
    }

    @Override
    public int getCapacity() {
        return maxCapacity;
    }

    @Override
    public int getTotalAmount() {
        int ret = 0;
        for (Map.Entry entry : containedElements.entrySet()) {
            ret += ((Integer) entry.getValue());
        }
        return ret;
    }

    @Override
    public int getNumberOfElements() {
        return containedElements.size();
    }

    @Override
    public int[] getElements() {
        int[] elem = new int[containedElements.size()];
        int i = 0;
        for (Map.Entry entry : containedElements.entrySet()) {
            elem[i++] = ((Integer) entry.getValue());
        }
        return elem;
    }

    @Override
    public int getAmountOf(int i) {
        return containedElements.get(i);
    }

    @Override
    public int addAmountOf(int id, int amount, boolean simulate) {
        if (getNumberOfElements() >= maxCapacity && containedElements.containsKey(id)) {
            return 0;
        }
        int contained = containedElements.getOrDefault(id, 0);
        int canBeAdded = Math.min(maxCapacity - getTotalAmount(), amount);
        if (!simulate) {
            containedElements.put(id, contained + canBeAdded);
        }
        return canBeAdded;
    }

    @Override
    public int removeAmountOf(int id, int amount, boolean simulate) {
        int contained;
        contained = containedElements.getOrDefault(id, 0);
        if (!simulate) {
            containedElements.put(id, contained - Math.min(contained, amount));
        }
        return Math.min(amount, contained);
    }

    @Override
    public int getMaxNumberOfElements() {
        return maxNumElements;
    }

    @Override
    public Map<Integer, Integer> getElementMap() {
        return containedElements;
    }
}
