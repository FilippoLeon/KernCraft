package com.R3DKn16h7.kerncraft.capabilities;

import com.R3DKn16h7.kerncraft.elements.Element;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

/**
 * Created by Filippo on 22-Apr-17.
 *
 * The basic interface for containers that are able to store elements.
 */
public interface IElementContainer extends INBTSerializable<NBTTagCompound> {

    int getCapacity();

    int getTotalAmount();

    int getNumberOfElements();

    int[] getElements();

    int getAmountOf(int i);

    /**
     * Does the container accept elements that have this state?
     *
     * @param state
     * @return
     */
    boolean acceptsElementWithState(Element.State state);

    /**
     * Adds a new state to be accepted
     *
     * @param state
     * @return
     */
    void addAcceptedState(Element.State state);

    /**
     * Adds a new state to be accepted
     *
     * @param state
     * @return
     */
    void removeAcceptedState(Element.State state);

    /**
     * Is the container isolated?  Is true, then the container prevents
     * elements from "escaping" through itselt, causing nasty effects such as
     * poison, ecc...
     *
     * @return
     */
    boolean doesIsolate();

    /**
     * Does the container confine its content and prefent it from exploting,
     * decaying, etc...
     *
     * @return
     */
    boolean doesConfine();

    /**
     * Whenever somebody adds an element to the container, we must notify the container of the
     * entity that performed the action (if any).
     *
     * @param id
     * @param amount
     * @param simulate
     * @param adder    Whoever is in charge of adding the element to the canister.
     * @return
     */
    int addAmountOf(int id, int amount, boolean simulate, Entity adder);

    /**
     * Only indended to create a full stack without it exploding in face.
     * <p>
     * Pass null to addAmountOf for Entities instead.
     *
     * @param id The id of the element.
     * @param amount The amount to add.
     * @param simulate Do not acutally perform the transfer.
     * @return
     */
    @Deprecated
    int addAmountOf(int id, int amount, boolean simulate);

    int removeAmountOf(int id, int amount, boolean simulate);

    int getMaxNumberOfElements();

    Map<Integer, Integer> getElementMap();

    void removeAllOf(int id);
}
