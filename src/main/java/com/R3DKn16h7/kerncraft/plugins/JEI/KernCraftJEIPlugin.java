package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.crafting.ChemicalFurnaceRecipe;
import com.R3DKn16h7.kerncraft.crafting.KernCraftRecipes;
import com.R3DKn16h7.kerncraft.elements.ElementBase;
import com.R3DKn16h7.kerncraft.items.Canister;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import com.R3DKn16h7.kerncraft.tileentities.KernCraftTileEntities;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by filippo on 23/11/16.
 */
@JEIPlugin
public class KernCraftJEIPlugin implements IModPlugin {
    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.registerSubtypeInterpreter(KernCraftItems.CANISTER, new ElementJEISubtypeInterpreter());
//        subtypeRegistry.useNbtForSubtypes();
//        subtypeRegistry.useNbtForSubtypes(KernCraftItems.CANISTER);
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {
    }

    @Override
    public void register(IModRegistry registry) {
        IJeiHelpers jeih = registry.getJeiHelpers();
        IGuiHelper guih = jeih.getGuiHelper();

        registry.addRecipeCategories(new ExtractorJEIRecipeCategory(guih));
        registry.addRecipeHandlers(new ExtractorJEIRecipeHandler());
        registry.addRecipeCategoryCraftingItem(
                new ItemStack(Item.getItemFromBlock(KernCraftTileEntities.EXTRACTOR)),
                ExtractorJEIRecipeCategory.CATEGORY_UID
        );


        IRecipeWrapperFactory<ChemicalFurnaceRecipe> factory
                = new IRecipeWrapperFactory<ChemicalFurnaceRecipe>() {
            @Override
            public IRecipeWrapper getRecipeWrapper(ChemicalFurnaceRecipe recipe) {
                return new ChemicalFurnaceJEIRecipeWrapper(recipe);
            }
        };

        registry.addRecipeCategories(new ChemicalFurnaceJEIRecipeCategory(guih));
        registry.handleRecipes(ChemicalFurnaceRecipe.class,
                factory, ChemicalFurnaceJEIRecipeCategory.CATEGORY_UID);
        registry.addRecipes(KernCraftRecipes.CHEMICAL_FURNACE_RECIPES,
                ChemicalFurnaceJEIRecipeCategory.CATEGORY_UID);
        registry.addRecipeCategoryCraftingItem(
                new ItemStack(Item.getItemFromBlock(KernCraftTileEntities.CHEMICAL_FURNACE)),
                ChemicalFurnaceJEIRecipeCategory.CATEGORY_UID
        );

        // TODO: add decription for elements
        if(KernCraft.MOD_CONFIG_DISPLAY_ALL_ELEMENTS) {
            for (int i = 1; i <= ElementBase.NUMBER_OF_ELEMENTS; ++i) {
                String descr = ElementBase.getElement(i).description;
                if(descr != null) {
                    registry.addDescription(Canister.getElementItemStack(i), descr);
                }
            }
        }

        registry.addRecipes(KernCraftRecipes.EXTRACTOR_RECIPES,
                ExtractorJEIRecipeCategory.CATEGORY_UID);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    }
}
