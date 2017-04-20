package com.R3DKn16h7.kerncraft.crafting;

import com.R3DKn16h7.kerncraft.blocks.KernCraftBlocks;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModCrafting {
    public static void initCrafting() {

        GameRegistry.addRecipe(new ItemStack(KernCraftItems.PORTABLE_BEACON),
                "##", "##", '#', KernCraftBlocks.TEST_BLOCK);

        GameRegistry.addShapedRecipe(new ItemStack(KernCraftItems.CANISTER),
                "#N#", "N N", "#N#", '#', Items.IRON_INGOT, 'N', Items.IRON_INGOT);
        GameRegistry.addShapedRecipe(new ItemStack(KernCraftItems.POTATO_BATTERY),
                "i  ",
                "P- ",
                "   ",
                'P', Items.POISONOUS_POTATO, 'i', Items.GOLD_INGOT, '-', Items.IRON_INGOT);
        GameRegistry.addShapedRecipe(new ItemStack(KernCraftItems.ELECTROLYTIC_CELL),
                "WiW",
                " iB",
                "ppp",
                'W', Items.GLASS_BOTTLE, 'i', Blocks.IRON_BARS,
                'p', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'B', KernCraftItems.POTATO_BATTERY);

        ItemStack canister = new ItemStack(KernCraftItems.CANISTER, 1, 1);
        NBTTagCompound comp = new NBTTagCompound();
        comp.setInteger("element", 1);
        canister.writeToNBT(comp);
        //IRecipe
        GameRegistry.addShapelessRecipe(new ItemStack(KernCraftItems.TYROCINIUM_CHYMICUM), Items.BOOK, canister);

        int[] elements = {ElementRecipe.ANY_ELEMENT};
        int[] quantities = {0};
        NonNullList<ItemStack> ing = NonNullList.create();
        ing.add(new ItemStack(Items.BOOK));
//        CraftingManager.getInstance().addRecipe(new ElementRecipe(KernCraftItems.TYROCINIUM_CHYMICUM,
//                ing, elements, quantities));
    }

}
