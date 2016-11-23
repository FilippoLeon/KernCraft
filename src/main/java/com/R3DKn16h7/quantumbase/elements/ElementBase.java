package com.R3DKn16h7.quantumbase.elements;

import com.R3DKn16h7.quantumbase.items.ModItems;
import com.R3DKn16h7.quantumbase.tileentities.ExtractorTileEntity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Filippo on 22/11/2016.
 */
public class ElementBase {
    public static ElementBase instance;
    private static HashMap<String, Integer> string_to_id = new HashMap<String, Integer>();
    private static ArrayList<Element> elements;

    public ElementBase(String resource) {
        if (instance != null) return;
        instance = this;
        elements = new ArrayList<Element>(118);

        Gson gson = new Gson();
        //ResourceLocation loc = new ResourceLocation("quantumbase:config/elements.json");
        InputStreamReader in = null;
        try {
            System.out.println("Reading file = .txt");
            in = new InputStreamReader(getClass().getClassLoader()
                    .getResourceAsStream("assets/quantumbase/config/elements.json"), "UTF-8");
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

    static public int symbolToId(String s) {
        return string_to_id.get(s);
    }

    static public Element getElement(int id) {
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

    public enum ElementState {
        GAS, LIQUID, SOLID;

        public static ElementState fromString(String s) {
            if (s == "GAS") return GAS;
            if (s == "LUQUID") return LIQUID;
            if (s == "SOLID") return SOLID;
            else return GAS;
        }

        public String toColor() {
            switch (this) {
                case GAS:
                    return TextFormatting.RED.toString();
                case LIQUID:
                    return TextFormatting.YELLOW.toString();
                case SOLID:
                    return TextFormatting.GREEN.toString();
            }
            return "";
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
        public float half_life;

        Element(int id_, JsonObject js) {
            id = id_;
            symbol = js.get("symbol").getAsString();
            try {
                mass = js.get("mass").getAsFloat();
            } catch (RuntimeException e) {
                density = 0;
            }

            try {
                group = js.get("group").getAsInt();
            } catch (RuntimeException e) {
                try {
                    String gr = js.get("group").getAsString();
                    if (gr == "A") group = -2;
                    else if (gr == "L") group = -3;
                    else group = -1;
                } catch (RuntimeException e2) {

                }
            }
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
            color = js.get("conductivity").getAsString();
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
        }
    }

}
