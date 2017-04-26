package com.R3DKn16h7.kerncraft.capabilities;

import com.R3DKn16h7.kerncraft.elements.Element;
import com.R3DKn16h7.kerncraft.elements.ElementRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;
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
    private ItemStack itemStack;
    private EnumSet<Element.State> acceptedStates = EnumSet.allOf(Element.State.class);
    // TODO: handle strong containers that don't explode etc...


    public ElementContainerDefaultCapability() {
        this(1, 1000, null);
    }

    public ElementContainerDefaultCapability(int maxNumElements,
                                             int maxCapacity) {
        this(maxNumElements, maxCapacity, null);
    }

    public ElementContainerDefaultCapability(int maxNumElements,
                                             int maxCapacity, ItemStack itemStack) {
        this.itemStack = itemStack;
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

        nbt.setBoolean("acceptsGas", acceptedStates.contains(Element.State.GAS));
        nbt.setBoolean("acceptsLiquid", acceptedStates.contains(Element.State.SOLID));
        nbt.setBoolean("acceptsSolid", acceptedStates.contains(Element.State.LIQUID));
        nbt.setBoolean("acceptsUnknown", acceptedStates.contains(Element.State.UNKNOWN));

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
        if (nbt.getBoolean("acceptsGas")) acceptedStates.add(Element.State.GAS);
        if (nbt.getBoolean("acceptsLiquid")) acceptedStates.add(Element.State.SOLID);
        if (nbt.getBoolean("acceptsSolid")) acceptedStates.add(Element.State.LIQUID);
        if (nbt.getBoolean("acceptsUnknown")) acceptedStates.add(Element.State.UNKNOWN);
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
            elem[i++] = ((Integer) entry.getKey());
        }
        return elem;
    }

    @Override
    public void removeAllOf(int id) {
        containedElements.remove(id);
    }

    @Override
    public int getAmountOf(int i) {
        return containedElements.getOrDefault(i, 0);
    }

    public int addAmountOf(int id, int amount, boolean simulate, Entity ent) {
        int add = addAmountOf(id, amount, simulate);
        if (!simulate && ent != null) {

            // Move to "ExplodeIfNeeded"
            Element elem = ElementRegistry.getElement(id);
            if (elem.reachedCriticalMass(getAmountOf(id))) {
                containedElements.put(id, 0);

                BlockPos pos = ent.getPosition();
                ent.world.createExplosion(ent, pos.getX(), pos.getY(), pos.getZ(),
                        10, true);
                return 0;
            }
        }
        return add;
    }

    @Override
    @Deprecated
    public int addAmountOf(int id, int amount, boolean simulate) {
        if (id <= 0 || id > ElementRegistry.NUMBER_OF_ELEMENTS) return 0;
        if (getNumberOfElements() >= maxNumElements && !containedElements.containsKey(id)) {
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
            if (contained - Math.min(contained, amount) == 0) {
                containedElements.remove(id);
            } else {
                containedElements.put(id, contained - Math.min(contained, amount));
            }
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

    @Override
    public boolean acceptsElementWithState(Element.State state) {
        return acceptedStates.contains(state);
    }

    @Override
    public void addAcceptedState(Element.State state) {
        acceptedStates.add(state);
    }

    @Override
    public void removeAcceptedState(Element.State state) {
        acceptedStates.remove(state);
    }

    @Override
    public boolean doesIsolate() {
        return false;
    }

    @Override
    public boolean doesConfine() {
        return false;
    }
}
