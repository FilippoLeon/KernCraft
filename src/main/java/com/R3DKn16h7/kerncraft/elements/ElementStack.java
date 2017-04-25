package com.R3DKn16h7.kerncraft.elements;

/**
 * Created by Filippo on 20-Apr-17.
 *
 * This is used to specify custom recipes.
 */
public class ElementStack {
    public int id;
    public int quantity;
    public float prob = 1.0f;

    public ElementStack(int id_, int quantity_) {
        id = id_;
        quantity = quantity_;
    }

    public ElementStack(String name_, int quantity_) {
        id = ElementRegistry.symbolToId(name_);
        quantity = quantity_;
    }

    public ElementStack(int id_, int quantity_, float prob_) {
        this(id_, quantity_);
        prob = prob_;
    }

    public ElementStack(String name_, int quantity_, float prob_) {
        this(name_, quantity_);
        prob = prob_;
    }

}
