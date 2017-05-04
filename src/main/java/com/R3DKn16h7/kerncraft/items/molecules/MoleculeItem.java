package com.R3DKn16h7.kerncraft.items.molecules;

import com.R3DKn16h7.kerncraft.items.BasicItem;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import net.minecraftforge.oredict.OreDictionary;

public class MoleculeItem extends BasicItem {

    public MoleculeItem(String name) {
        this(name, null);
    }

    public MoleculeItem(String name, Object oreDictName) {
        super(name);

        if (oreDictName != null) {
            if (oreDictName instanceof String) {
                OreDictionary.registerOre(((String) oreDictName), this);
            } else if (oreDictName instanceof String[]) {
                for (String str : ((String[]) oreDictName)) {
                    OreDictionary.registerOre(str, this);
                }
            }
        }
    }

    public static void create(String name, Object oreDictName) {
        KernCraftItems.MATERIALS.put(name, new MoleculeItem(name, oreDictName));
    }
}