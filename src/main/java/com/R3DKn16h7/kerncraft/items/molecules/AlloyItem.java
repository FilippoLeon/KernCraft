package com.R3DKn16h7.kerncraft.items.molecules;

import com.R3DKn16h7.kerncraft.blocks.BasicBlock;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.Map;

public class AlloyItem {
    public static ListMultimap<String, String> ALLOY_ORES = ArrayListMultimap.create();

    public AlloyItem(String name, Object oreNames) {
        String[] oreNamesIngot = null;
        String[] oreNamesNugget = null;
        String[] oreNamesBlock = null;
        if (oreNames instanceof String) {
            oreNamesIngot = new String[1];
            oreNamesNugget = new String[1];
            oreNamesBlock = new String[1];
            oreNamesIngot[0] = "ingot" + oreNames;
            oreNamesNugget[0] = "nugget" + oreNames;
            oreNamesBlock[0] = "block" + oreNames;

            ALLOY_ORES.put(name, ((String) oreNames));
        } else if (oreNames instanceof String[]) {
            oreNamesIngot = new String[((String[]) oreNames).length];
            oreNamesNugget = new String[((String[]) oreNames).length];
            oreNamesBlock = new String[((String[]) oreNames).length];
            for (int i = 0; i < ((String[]) oreNames).length; ++i) {
                oreNamesIngot[i] = "ingot" + ((String[]) oreNames)[i];
                oreNamesNugget[i] = "nugget" + ((String[]) oreNames)[i];
                oreNamesBlock[i] = "block" + ((String[]) oreNames)[i];
                ALLOY_ORES.put(name, ((String[]) oreNames)[i]);
            }
        }

        MoleculeItem.create(name + "_ingot", oreNamesIngot);
        MoleculeItem.create(name + "_nugget", oreNamesNugget);
        BasicBlock.create(name + "_block", oreNamesBlock);
    }

    public static void addAlloyRecipes() {
        for (Map.Entry<String, String> entry : ALLOY_ORES.entries()) {
            String nugget = "nugget" + entry.getValue();
            String ingot = "ingot" + entry.getValue();
            String block = "block" + entry.getValue();
            String nuggetName = entry.getKey() + "_nugget";
            String ingotName = entry.getKey() + "_ingot";
            String blockName = entry.getKey() + "_block";
            GameRegistry.addRecipe(new ShapelessOreRecipe(Block.getBlockFromItem(KernCraftItems.MATERIALS.get(blockName)),
                    new Object[]{ingot, ingot, ingot, ingot, ingot, ingot, ingot, ingot, ingot}
            ));
            GameRegistry.addRecipe(new ShapelessOreRecipe(KernCraftItems.MATERIALS.get(ingotName),
                    new Object[]{nugget, nugget, nugget, nugget, nugget, nugget, nugget, nugget, nugget}
            ));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(KernCraftItems.MATERIALS.get(ingotName), 9),
                    new Object[]{block}
            ));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(KernCraftItems.MATERIALS.get(nuggetName), 9),
                    new Object[]{ingot}
            ));
        }
    }
}