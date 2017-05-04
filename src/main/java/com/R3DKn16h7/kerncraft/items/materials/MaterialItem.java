package com.R3DKn16h7.kerncraft.items.materials;

import com.R3DKn16h7.kerncraft.items.BasicItem;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import net.minecraftforge.oredict.OreDictionary;

public class MaterialItem extends BasicItem {

    public MaterialItem(String name) {
        this(name, null);
    }

    public MaterialItem(String name, String oreDictName) {
        super(name);

        if (oreDictName != null) {
            OreDictionary.registerOre(oreDictName, this);
        }
    }

    public static void create(String name) {
        KernCraftItems.MATERIALS.put(name, new MaterialItem(name));
    }
}