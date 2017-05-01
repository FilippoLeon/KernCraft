package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.crafting.CentrifugeRecipe;
import com.R3DKn16h7.kerncraft.crafting.ChemicalFurnaceRecipe;
import com.R3DKn16h7.kerncraft.crafting.ElectrolyzerRecipe;
import com.R3DKn16h7.kerncraft.crafting.KernCraftRecipes;
import com.R3DKn16h7.kerncraft.elements.ElementRegistry;
import com.R3DKn16h7.kerncraft.items.Canister;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import com.R3DKn16h7.kerncraft.tileentities.KernCraftTileEntities;
import com.R3DKn16h7.kerncraft.utils.config.KernCraftConfig;
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
        subtypeRegistry.registerSubtypeInterpreter(KernCraftItems.CANISTER,
                new ElementJEISubtypeInterpreter());
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


        //// REGISTER CHEMICAL FURNACE CATEGORY
        IRecipeWrapperFactory<ChemicalFurnaceRecipe> ChemicalFurnaceFactory
                = new IRecipeWrapperFactory<ChemicalFurnaceRecipe>() {
            @Override
            public IRecipeWrapper getRecipeWrapper(ChemicalFurnaceRecipe recipe) {
                return new ChemicalFurnaceJEIRecipeWrapper(recipe);
            }
        };
        ChemicalFurnaceJEIRecipeCategory ChemicalFurnaceCat = new ChemicalFurnaceJEIRecipeCategory(guih);
        registry.handleRecipes(ChemicalFurnaceRecipe.class,
                ChemicalFurnaceFactory, ChemicalFurnaceCat.getUid());
        registry.addRecipes(KernCraftRecipes.CHEMICAL_FURNACE_RECIPES,
                ChemicalFurnaceCat.getUid());
        registry.addRecipeCategoryCraftingItem(
                new ItemStack(Item.getItemFromBlock(KernCraftTileEntities.CHEMICAL_FURNACE)),
                ChemicalFurnaceCat.getUid()
        );

        //// REGISTER CENTRIFUGE CATEGORY
        IRecipeWrapperFactory<CentrifugeRecipe> CentrifugeFactory
                = new IRecipeWrapperFactory<CentrifugeRecipe>() {
            @Override
            public IRecipeWrapper getRecipeWrapper(CentrifugeRecipe recipe) {
                return new CentrifugeJEIRecipeWrapper(recipe);
            }
        };
        CentrifugeJEIRecipeCategory CentrifugeCat = new CentrifugeJEIRecipeCategory(guih);
        registry.handleRecipes(CentrifugeRecipe.class,
                CentrifugeFactory, CentrifugeCat.getUid());
        registry.addRecipes(KernCraftRecipes.CENTRIFUGE_RECIPES,
                CentrifugeCat.getUid());
        registry.addRecipeCategoryCraftingItem(
                new ItemStack(Item.getItemFromBlock(KernCraftTileEntities.CENTRIFUGE_MACHINE)),
                CentrifugeCat.getUid()
        );

        //// REGISTER ELECTROLYZER CATEGORY
        IRecipeWrapperFactory<ElectrolyzerRecipe> ElectrolyzerFactory
                = new IRecipeWrapperFactory<ElectrolyzerRecipe>() {
            @Override
            public IRecipeWrapper getRecipeWrapper(ElectrolyzerRecipe recipe) {
                return new ElectrolyzerJEIRecipeWrapper(recipe);
            }
        };
        ElectrolyzerJEIRecipeCategory ElectrolyzerCat = new ElectrolyzerJEIRecipeCategory(guih);
        registry.handleRecipes(ElectrolyzerRecipe.class,
                ElectrolyzerFactory, ElectrolyzerCat.getUid());
        registry.addRecipes(KernCraftRecipes.ELECTROLYZER_RECIPES,
                ElectrolyzerCat.getUid());
        registry.addRecipeCategoryCraftingItem(
                new ItemStack(Item.getItemFromBlock(KernCraftTileEntities.ELECTROLYZER_MACHINE)),
                ElectrolyzerCat.getUid()
        );

        registry.addRecipeCategories(ChemicalFurnaceCat, CentrifugeCat, ElectrolyzerCat);

        // TODO: add description for elements
        if (KernCraftConfig.DISPLAY_ALL_ELEMENTS) {
            for (int i = 1; i <= ElementRegistry.NUMBER_OF_ELEMENTS; ++i) {
                String descr = ElementRegistry.getElement(i).shortDescription;
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
