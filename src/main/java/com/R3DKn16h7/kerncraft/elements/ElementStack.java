package com.R3DKn16h7.kerncraft.elements;

/**
 * Created by Filippo on 20-Apr-17.
 *
 * This is used to specify custom recipes that use Elements inside
 * containers that have the appropriate capability. Probability is used
 * only to specify outputs.
 *
 * TODO: maybe use some "Ingredient" wrapper class?
 */
public class ElementStack {
    public int id;
    public int quantity;
    public float probability = 1.0f;

    public ElementStack(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public ElementStack(String name, int quantity) {
        id = ElementRegistry.symbolToId(name);
        this.quantity = quantity;
    }

    public ElementStack(int id, int quantity, float probability) {
        this(id, quantity);
        this.probability = probability;
    }

    public ElementStack(String name, int quantity, float probability) {
        this(name, quantity);
        this.probability = probability;
    }
}
