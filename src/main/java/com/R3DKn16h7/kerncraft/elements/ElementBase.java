package com.R3DKn16h7.kerncraft.elements;

import com.R3DKn16h7.kerncraft.items.ModItems;
import com.R3DKn16h7.kerncraft.tileentities.ExtractorTileEntity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
    public static ElementBase instance;
    private static HashMap<String, Integer> string_to_id = new HashMap<String, Integer>();
    private static ArrayList<Element> elements;

    public ElementBase(String resource) {
        if (instance != null) return;
        instance = this;
        elements = new ArrayList<Element>(NUMBER_OF_ELEMENTS);

        Gson gson = new Gson();
        //ResourceLocation loc = new ResourceLocation("kerncraft:config/elements.json");
        InputStreamReader in = null;
        try {
            System.out.println("Reading file = .txt");
            in = new InputStreamReader(getClass().getClassLoader()
                    .getResourceAsStream("assets/kerncraft/config/elements.json"), "UTF-8");
            //in = Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //BufferedReader reader = new BufferedReader(new InputStreamReader(in))
        BufferedReader reader = new BufferedReader(in);

        JsonElement je = gson.fromJson(reader, JsonElement.class);
        JsonObject json = je.getAsJsonObject();


        for (HashMap.Entry<String, JsonElement> j : json.entrySet()) {
            String symb = j.getValue().getAsJsonObject().get("symbol").getAsString();
            Integer id = Integer.parseInt(j.getKey());

            System.out.println(symb + " (id = " + id + ") added...");

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

    static public ItemStack getItem(ExtractorTileEntity.ElementStack stack) {
        return getItem(stack.id, stack.quantity);
    }

    static public ItemStack getItem(int id, int qty) {
        ItemStack is = new ItemStack(ModItems.canister);
        NBTTagCompound nbt = is.getTagCompound();
        if (nbt == null) {
            nbt = new NBTTagCompound();
        }

        nbt.setInteger("Element", id);
        nbt.setInteger("Quantity", qty);

        is.setTagCompound(nbt);

        return is;
    }

    static public Color StringToColor(String str) {
        if (str.equals("colorless")) return new Color(255, 255, 255);
        if (str.equals("white")) return new Color(255, 255, 255);
        if (str.equals("bright white")) return new Color(255, 255, 255);
        else if (str.equals("silvery")) return new Color(192, 192, 192);
        else if (str.equals("gray")) return new Color(128, 128, 128);
        else if (str.equals("steel gray")) return new Color(100, 100, 100);
        else if (str.equals("black")) return new Color(0, 0, 0);
        else if (str.equals("gray-black")) return new Color(40, 40, 40);
        else if (str.equals("white-yellow")) return new Color(255, 255, 200);
        else if (str.equals("yellow(pale)")) return new Color(255, 255, 150);
        else if (str.equals("greenish-yellow")) return new Color(205, 255, 150);
        else if (str.equals("silvery gray")) return new Color(230, 230, 230);
        else if (str.equals("silvery-white")) return new Color(250, 250, 210);
        else if (str.equals("bluish-gray")) return new Color(150, 150, 255);
        else if (str.equals("bluish-black")) return new Color(200, 200, 255);
        else if (str.equals("golden yellow")) return new Color(255, 255, 10);
        else if (str.equals("bluish-white")) return new Color(210, 210, 255);
        else if (str.equals("orange-red")) return new Color(255, 70, 0);
        else if (str.equals("silvery-blue")) return new Color(140, 190, 255);
        else if (str.equals("red-brown")) return new Color(165, 42, 42);
        else if (str.equals("grayish-white")) return new Color(211, 211, 211);
        else if (str.equals("gray-white")) return new Color(244, 244, 244);
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
        Alkali, Alkaline, BasicMetal, Semimetal, NonMetal, Halogen, NobleGas, Transition,
        Actinides, Lanthanides, UNKNOWN;

        public static ElementFamily fromString(String s) {
            if (s.equals("Alkali")) return Alkali;
            else if (s.equals("Alkaline")) return Alkaline;
            else if (s.equals("BasicMetal")) return BasicMetal;
            else if (s.equals("Semimetal")) return Semimetal;
            else if (s.equals("NonMetal")) return NonMetal;
            else if (s.equals("Halogen")) return Halogen;
            else if (s.equals("Noble")) return NobleGas;
            else if (s.equals("Transition")) return Transition;
            else if (s.equals("L")) return Lanthanides;
            else if (s.equals("A")) return Actinides;
            return UNKNOWN;
        }

        public Color toColor() {
            switch (this) {
                case Alkali:
                    return new Color(255, 0, 0);
                case Alkaline:
                    return new Color(240, 150, 0);
                case Transition:
                    return new Color(250, 250, 0);
                case BasicMetal:
                    return new Color(100, 255, 0);
                case Semimetal:
                    return new Color(0, 155, 135);
                case NonMetal:
                    return new Color(0, 105, 135);
                case Halogen:
                    return new Color(100, 55, 235);
                case NobleGas:
                    return new Color(140, 0, 235);
                case Lanthanides:
                    return new Color(0, 205, 0);
                case Actinides:
                    return new Color(0, 100, 0);
                case UNKNOWN:
                    return new Color(14, 14, 14);
            }
            return null;
        }
    }

    static public class Element {
        public int id;
        public String symbol;
        public String register_name;
        public String name;
        public int group;
        public int period;
        public float mass;
        public float density;
        public float radius;
        public float conductivity;
        public float sp_heat;
        public float th_conductivity;
        public String color;
        public boolean toxic;
        public ElementState state;
        public ElementFamily family;
        public float half_life;
        public String description;

        Element(int id_, JsonObject js) {
            id = id_;
            symbol = js.get("symbol").getAsString();
            try {
                mass = js.get("mass").getAsFloat();
            } catch (RuntimeException e) {
                density = 0;
            }

            String gr = js.get("group").getAsString();
            if (gr.equals("A")) group = GROUP.Actinide.getValue();
            else if (gr.equals("L")) {
                group = GROUP.Lanthanide.getValue();
            } else {
                try {
                    group = js.get("group").getAsInt();
                } catch (RuntimeException e) {

                }
            }

            family = ElementFamily.fromString(js.get("family").getAsString());

            period = js.get("period").getAsInt();
            name = js.get("english").getAsString();
            try {
                density = js.get("density").getAsFloat();
            } catch (RuntimeException e) {
                density = 0;
            }
            try {
                radius = js.get("radius").getAsFloat();
            } catch (RuntimeException e) {
                radius = 0;
            }
            try {
                conductivity = js.get("conductivity").getAsFloat();
            } catch (RuntimeException e) {
                conductivity = 0;
            }

            try {
                th_conductivity = js.get("th_conductivity").getAsFloat();
            } catch (RuntimeException e) {
                th_conductivity = 0;
            }
            try {
                sp_heat = js.get("sp_heat").getAsFloat();
            } catch (RuntimeException e) {
                sp_heat = 0;
            }
            color = js.get("color").getAsString();
            try {
                toxic = js.get("toxic").getAsBoolean();
            } catch (RuntimeException e) {
                toxic = false;
            }

            state = ElementState.fromString(js.get("state").getAsString());

            try {
                half_life = js.get("half_life").getAsFloat();
            } catch (RuntimeException e) {
                half_life = 0;
            }

            try {
                description = js.get("description").getAsString();
            } catch (RuntimeException e) {
            }

        }
    }

}
