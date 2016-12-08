package com.R3DKn16h7.kerncraft.crafting;

import com.R3DKn16h7.kerncraft.blocks.ModBlocks;
import com.R3DKn16h7.kerncraft.items.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModCrafting {
    public static void initCrafting() {
        GameRegistry.addRecipe(new ItemStack(ModBlocks.testBlock),
                "##", "##", '#', ModItems.portableBeacon);

        ModItems.canister.addRecipes();
//		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.tutorialItem), new Object[] {Items.redstone, new ItemStack(Items.dye, 1, 4)});
//		GameRegistry.addSmelting(Items.diamond, new ItemStack(ModItems.tutorialItem), 1.0F);
    }

}
