package com.R3DKn16h7.kerncraft.elements;

import com.R3DKn16h7.kerncraft.items.Canister;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Filippo on 22/11/2016.
 */
public class ElementBase {
    public static final int NUMBER_OF_ELEMENTS = 118;
    public static ElementBase INSTANCE;
    private static HashMap<String, Integer> string_to_id = new HashMap<String, Integer>();
    private static ArrayList<Element> elements;

    public ElementBase() {
        if (INSTANCE != null) return;
        INSTANCE = this;

        elements = new ArrayList<>(NUMBER_OF_ELEMENTS);

        Gson gson = new Gson();

        InputStreamReader in = null;
        try {
            System.out.println("Reading file = .txt");
            in = new InputStreamReader(getClass().getClassLoader()
                    .getResourceAsStream("assets/kerncraft/config/elements.json"),
                    "UTF-8");
            //in = Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(in);

        JsonElement je = gson.fromJson(reader, JsonElement.class);
        JsonObject json = je.getAsJsonObject();


        for (HashMap.Entry<String, JsonElement> j : json.entrySet()) {
            String symb = j.getValue().getAsJsonObject().get("symbol").getAsString();
            Integer id = Integer.parseInt(j.getKey());

            JsonObject js = j.getValue().getAsJsonObject();
            elements.add(new Element(id, js));

            string_to_id.put(symb, id);
        }
    }

    public static List<Element> getElements() {
        return elements;
    }

    static public int symbolToId(String s) {
        return string_to_id.get(s);
    }

    static public Element getElement(int id) {
        if (id < 1) return null;
        return elements.get(id - 1);
    }

    static public ItemStack getItem(ElementStack stack) {
        return Canister.getElementItemStack(stack.id, stack.quantity);
    }

    static public Color StringToColor(String str) {
        if (str.equals("colorless")) return new Color(62, 255, 255);
        if (str.equals("white")) return new Color(142, 142, 142);
        if (str.equals("bright white")) return new Color(148, 148, 148);
        else if (str.equals("silvery")) return new Color(192, 192, 192);
        else if (str.equals("gray")) return new Color(128, 128, 128);
        else if (str.equals("steel gray")) return new Color(100, 100, 100);
        else if (str.equals("black")) return new Color(0, 0, 0);
        else if (str.equals("gray-black")) return new Color(40, 40, 40);
        else if (str.equals("white-yellow")) return new Color(253, 255, 148);
        else if (str.equals("yellow(pale)")) return new Color(255, 255, 150);
        else if (str.equals("greenish-yellow")) return new Color(205, 255, 150);
        else if (str.equals("silvery gray")) return new Color(101, 101, 101);
        else if (str.equals("silvery-white")) return new Color(250, 249, 169);
        else if (str.equals("bluish-gray")) return new Color(150, 150, 255);
        else if (str.equals("bluish-black")) return new Color(200, 200, 255);
        else if (str.equals("golden yellow")) return new Color(255, 255, 10);
        else if (str.equals("bluish-white")) return new Color(210, 210, 255);
        else if (str.equals("orange-red")) return new Color(255, 70, 0);
        else if (str.equals("silvery-blue")) return new Color(140, 190, 255);
        else if (str.equals("red-brown")) return new Color(165, 42, 42);
        else if (str.equals("grayish-white")) return new Color(151, 151, 151);
        else if (str.equals("gray-white")) return new Color(156, 156, 156);
        else if (str.equals("yellow/silvery")) return new Color(230, 230, 192);
        else if (str.equals("blue glow")) return new Color(0, 0, 255);
        return new Color(255, 130, 130);
    }

    public enum GROUP {
        Actinide(-2),
        Lanthanide(-3);
        private int value;

        GROUP(int value) {
            this.value = value;
        }

        public static String toString(int value) {
            if (value == -2) {
                return "A";
            } else if (value == -3) {
                return "L";
            }
            return Integer.toString(value);
        }

        public String toString() {
            return toString(value);
        }

        public int getValue() {
            return value;
        }
    }

    public enum ElementState {
        GAS, LIQUID, SOLID, UNKNOWN;

        public static ElementState fromString(String s) {
            if (s.equals("g")) return GAS;
            else if (s.equals("l")) return LIQUID;
            else if (s.equals("s")) return SOLID;
            else return UNKNOWN;
        }

        public String toColor() {
            switch (this) {
                case GAS:
                    return TextFormatting.AQUA.toString();
                case LIQUID:
                    return TextFormatting.BLUE.toString();
                case SOLID:
                    return TextFormatting.RESET.toString();
                case UNKNOWN:
                    return TextFormatting.GRAY.toString();
            }
            return "";
        }
    }

    public enum ElementFamily {
        ALKALI, ALKALINE, BASIC_METAL, SEMIMETAL, NON_METAL, HALOGEN, NOBLE_GAS, TRANSITION,
        ACTINIDES, LANTHANIDES, UNKNOWN;

        public static ElementFamily fromString(String s) {
            if (s.equals("Alkali")) return ALKALI;
            else if (s.equals("Alkaline")) return ALKALINE;
            else if (s.equals("BasicMetal")) return BASIC_METAL;
            else if (s.equals("Semimetal")) return SEMIMETAL;
            else if (s.equals("NonMetal")) return NON_METAL;
            else if (s.equals("Halogen")) return HALOGEN;
            else if (s.equals("Noble")) return NOBLE_GAS;
            else if (s.equals("Transition")) return TRANSITION;
            else if (s.equals("L")) return LANTHANIDES;
            else if (s.equals("A")) return ACTINIDES;
            return UNKNOWN;
        }

        public String I18n() {
            return I18n.format("element.family." + this + ".name");
        }

        public Color toColor() {
            switch (this) {
                case ALKALI:
                    return new Color(255, 0, 0);
                case ALKALINE:
                    return new Color(240, 150, 0);
                case TRANSITION:
                    return new Color(250, 203, 40);
                case BASIC_METAL:
                    return new Color(100, 255, 0);
                case SEMIMETAL:
                    return new Color(0, 155, 135);
                case NON_METAL:
                    return new Color(0, 105, 135);
                case HALOGEN:
                    return new Color(100, 55, 235);
                case NOBLE_GAS:
                    return new Color(140, 0, 235);
                case LANTHANIDES:
                    return new Color(0, 205, 0);
                case ACTINIDES:
                    return new Color(0, 100, 0);
                case UNKNOWN:
                    return new Color(14, 14, 14);
            }
            return null;
        }
    }
}
