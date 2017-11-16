package com.R3DKn16h7.kerncraft.items.molecules;

import com.R3DKn16h7.kerncraft.blocks.BasicBlock;
import com.R3DKn16h7.kerncraft.blocks.KernCraftBlocks;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public class AlloyItem {
    public static List<String> ALLOY_ORES = new ArrayList<>();

    public AlloyItem(String name, Object oreNames) {
        String[] oreNamesIngot = null;
        String[] oreNamesNugget = null;
        String[] oreNamesBlock = null;
        ALLOY_ORES.add(name);
        if (oreNames instanceof String) {
            oreNamesIngot = new String[1];
            oreNamesNugget = new String[1];
            oreNamesBlock = new String[1];
            oreNamesIngot[0] = "ingot" + oreNames;
            oreNamesNugget[0] = "nugget" + oreNames;
            oreNamesBlock[0] = "block" + oreNames;

        } else if (oreNames instanceof String[]) {
            oreNamesIngot = new String[((String[]) oreNames).length];
            oreNamesNugget = new String[((String[]) oreNames).length];
            oreNamesBlock = new String[((String[]) oreNames).length];
            for (int i = 0; i < ((String[]) oreNames).length; ++i) {
                oreNamesIngot[i] = "ingot" + ((String[]) oreNames)[i];
                oreNamesNugget[i] = "nugget" + ((String[]) oreNames)[i];
                oreNamesBlock[i] = "block" + ((String[]) oreNames)[i];
            }
        }

        MoleculeItem.create(name + "_ingot", oreNamesIngot);
        MoleculeItem.create(name + "_nugget", oreNamesNugget);
        BasicBlock.create(name + "_block", oreNamesBlock);
    }

    public static void addAlloyRecipes() {
        for (String entry : ALLOY_ORES) {
            Item nugget = KernCraftItems.MATERIALS.get(entry + "_nugget");
            Item ingot = KernCraftItems.MATERIALS.get(entry + "_ingot");
            Item block = Item.getItemFromBlock(KernCraftBlocks.BLOCKS.get(entry + "_block"));

            // TODO 1.12 move
//            GameRegistry.addShapedRecipe(
//                    new ItemStack(ingot, 1),
//                    "GGG", "GGG", "GGG", 'G', nugget
//            );
//            GameRegistry.addShapedRecipe(
//                    new ItemStack(block, 1),
//                    "GGG", "GGG", "GGG", 'G', ingot
//            );
//            GameRegistry.addShapelessRecipe(
//                    new ItemStack(ingot, 9),
//                    new ItemStack(block, 9)
//            );
//            GameRegistry.addShapelessRecipe(
//                    new ItemStack(nugget, 9),
//                    new ItemStack(ingot, 1)
//            );
        }
    }
}