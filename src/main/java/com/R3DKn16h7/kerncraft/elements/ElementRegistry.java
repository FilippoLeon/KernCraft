package com.R3DKn16h7.kerncraft.elements;

import com.R3DKn16h7.kerncraft.items.Canister;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Filippo on 22/11/2016.
 * <p>
 * A singleton reading and generating all \Elements and keeping a fast record of
 * it.
 */
public class ElementRegistry {
    /**
     * Maybe we manage to syntesize some more element?
     */
    public static final int NUMBER_OF_ELEMENTS = 118;
    /**
     * Map an element name to its id, which can also access the elements array.
     */
    private static final HashMap<String, Integer> symbolToIdMap = new HashMap<>();
    /**
     * Element list in order of atomic number.
     */
    private static final ArrayList<Element> elements = new ArrayList<>(NUMBER_OF_ELEMENTS);

    public ElementRegistry() {
        Gson gson = new Gson();
        String fileName = "assets/kerncraft/config/elements.json";
        InputStreamReader in = null;
        System.out.println(
                String.format("Reading file with element data '%s.txt'", fileName)
        );
        try {
            in = new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream(fileName),
                    "UTF-8"
            );
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }

        BufferedReader reader = new BufferedReader(in);

        JsonElement je = gson.fromJson(reader, JsonElement.class);
        JsonObject json = je.getAsJsonObject();

        for (HashMap.Entry<String, JsonElement> j : json.entrySet()) {
            String symbol = j.getValue().getAsJsonObject().get("symbol").getAsString();
            Integer id = Integer.parseInt(j.getKey());

            JsonObject jsonElement = j.getValue().getAsJsonObject();
            // Fill in Registry
            elements.add(new Element(id, jsonElement));
            symbolToIdMap.put(symbol, id);
        }
    }

    /**
     * Get list of elements.
     *
     * @return
     */
    public static List<Element> getElements() {
        return elements;
    }

    /**
     * Return id of element with symbol.
     *
     * @param symbol
     * @return
     */
    static public int symbolToId(String symbol) {
        return symbolToIdMap.get(symbol);
    }

    /**
     * Return an element from its id (not position in list).
     *
     * @param id
     * @return
     */
    static public Element getElement(int id) {
        if (id < 1 || id > NUMBER_OF_ELEMENTS) return null;
        return elements.get(id - 1);
    }

    /**
     * Generate an ItemStack from an ElementStack (probability is ignored).
     *
     * @param stack
     * @return
     */
    @Deprecated
    static public ItemStack getItem(ElementStack stack) {
        return Canister.getElementItemStack(stack.id, stack.quantity);
    }

    /**
     * Map color name (in Elements JSON) to an actual color.
     *
     * @param str
     * @return
     */
    static public Color StringToColor(String str) {
        switch (str) {
            case "colorless":
                return new Color(62, 255, 255);
            case "white":
                return new Color(142, 142, 142);
            case "bright white":
                return new Color(148, 148, 148);
            case "silvery":
                return new Color(192, 192, 192);
            case "gray":
                return new Color(128, 128, 128);
            case "steel gray":
                return new Color(100, 100, 100);
            case "black":
                return new Color(0, 0, 0);
            case "gray-black":
                return new Color(40, 40, 40);
            case "white-yellow":
                return new Color(253, 255, 148);
            case "yellow(pale)":
                return new Color(255, 255, 150);
            case "greenish-yellow":
                return new Color(205, 255, 150);
            case "silvery gray":
                return new Color(101, 101, 101);
            case "silvery-white":
                return new Color(250, 249, 169);
            case "bluish-gray":
                return new Color(150, 150, 255);
            case "bluish-black":
                return new Color(200, 200, 255);
            case "golden yellow":
                return new Color(255, 255, 10);
            case "bluish-white":
                return new Color(210, 210, 255);
            case "orange-red":
                return new Color(255, 70, 0);
            case "silvery-blue":
                return new Color(140, 190, 255);
            case "red-brown":
                return new Color(165, 42, 42);
            case "grayish-white":
                return new Color(151, 151, 151);
            case "gray-white":
                return new Color(156, 156, 156);
            case "yellow/silvery":
                return new Color(230, 230, 192);
            case "blue glow":
                return new Color(0, 0, 255);
            default:
                return new Color(255, 130, 130);
        }
    }

}
