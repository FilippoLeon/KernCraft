package com.R3DKn16h7.kerncraft.crafting;

import com.R3DKn16h7.kerncraft.blocks.KernCraftBlocks;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filippo on 20-Apr-17.
 */
public class KernCraftRecipes {
    // Static register of recipes
    static public List<ISmeltingRecipe> EXTRACTOR_RECIPES = new ArrayList<>();
    public static List<ISmeltingRecipe> CHEMICAL_FURNACE_RECIPES = new ArrayList<>();

    public KernCraftRecipes() {
        RegisterCraftingRecipes();
        RegisterExtractorRecipes();
        RegisterChemicalFurnaceRecipes();
    }

    private void RegisterCraftingRecipes() {

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

        ItemStack canister = new ItemStack(KernCraftItems.CANISTER, 1);
        NBTTagCompound comp = new NBTTagCompound();
        comp.setInteger("Element", 1);
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

    private void RegisterExtractorRecipes() {
        addExtractorRecipe(Item.getItemFromBlock(Blocks.IRON_ORE),
                Item.getItemFromBlock(Blocks.ANVIL),
                new ElementStack[]{
                        new ElementStack(1, 1),
                        new ElementStack(4, 1)
                }, 10);
        addExtractorRecipe(Item.getItemFromBlock(Blocks.DIAMOND_ORE),
                Item.getItemFromBlock(Blocks.LAPIS_BLOCK),
                new ElementStack[]{
                        new ElementStack(1, 1),
                        new ElementStack(5, 5)
                }, 10);
        addExtractorRecipe(Item.getItemFromBlock(Blocks.DIAMOND_BLOCK),
                null,
                new ElementStack[]{
                        new ElementStack("Be", 4, 0.5f),
                        new ElementStack("Ar", 5)
                },
                10);
        addExtractorRecipe(Item.getItemFromBlock(Blocks.IRON_BLOCK),
                null,
                new ElementStack[]{
                        new ElementStack("Pu", 400, 0.5f),
                        new ElementStack("Pm", 5),
                        new ElementStack("H", 1, 0.8f),
                        new ElementStack("He", 2)
                },
                10);
    }


    private void RegisterChemicalFurnaceRecipes() {
        addChemicalFurnaceRecipe(
                new ElementStack[]{
                        new ElementStack("H", 4, 0),
                        new ElementStack("O", 2, 0),
                },
                new ElementStack[]{
                    new ElementStack("Pu", 400, 0.5f),
                    new ElementStack("Pu", 400, 0.5f),
                },
                100, new FluidStack(FluidRegistry.WATER, 1000), 5
        );
    }

    /**
     * Add new recipe
     * @param item Principal item that will allow to extract elements.
     * @param catalyst Additional item (optional) that acts as Catalyst
     * @param outs LAB_BONNET list of output Elements
     * @param energy The required energy (TODO: change to "cost") to perform a smelting operation.
     * @return Return false if registration failed (TODO)
     */
    static public boolean addExtractorRecipe(Item item, Item catalyst,
                                         ElementStack[] outs, int energy) {
        EXTRACTOR_RECIPES.add(new ExtractorRecipe(item, catalyst, outs, energy));
        return true;
    }


    static public boolean addChemicalFurnaceRecipe(ElementStack[] inputs, ElementStack[] outputs,
                                                   int energy, FluidStack fluid, int cost) {
        CHEMICAL_FURNACE_RECIPES.add(new ChemicalFurnaceRecipe(inputs, outputs, energy, fluid, cost));
        return true;
    }

}
