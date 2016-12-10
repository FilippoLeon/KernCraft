package com.R3DKn16h7.kerncraft.crafting;

import com.R3DKn16h7.kerncraft.blocks.ModBlocks;
import com.R3DKn16h7.kerncraft.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModCrafting {
    public static void initCrafting() {
        GameRegistry.addRecipe(new ItemStack(ModItems.PORTABLE_BEACON),
                "##", "##", '#', ModBlocks.TEST_BLOCK);

        GameRegistry.addShapedRecipe(new ItemStack(ModItems.CANISTER),
                "#N#", "N N", "#N#", '#', Items.IRON_INGOT, 'N', Items.IRON_INGOT);
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.POTATO_BATTERY),
                "i  ",
                "P- ",
                "   ",
                'P', Items.POISONOUS_POTATO, 'i', Items.GOLD_INGOT, '-', Items.IRON_INGOT);
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.ELECTROLYTIC_CELL),
                "WiW",
                " iB",
                "ppp",
                'W', Items.GLASS_BOTTLE, 'i', Blocks.IRON_BARS,
                'p', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'B', ModItems.POTATO_BATTERY);

        ItemStack canister = new ItemStack(ModItems.CANISTER, 1, 1);
        NBTTagCompound comp = new NBTTagCompound();
        comp.setInteger("element", 1);
        canister.writeToNBT(comp);
        //IRecipe
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.TYROCINIUM_CHYMICUM), Items.BOOK, canister);

        int[] elements = {ElementRecipe.ANY_ELEMENT};
        int[] quantities = {0};
        ItemStack[] ing = {new ItemStack(Items.BOOK)};
        CraftingManager.getInstance().addRecipe(new ElementRecipe(ModItems.TYROCINIUM_CHYMICUM, ing, elements, quantities));
    }

}
