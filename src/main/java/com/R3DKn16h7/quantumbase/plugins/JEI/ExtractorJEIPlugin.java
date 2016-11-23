package com.R3DKn16h7.quantumbase.plugins.JEI;

import com.R3DKn16h7.quantumbase.tileentities.ExtractorTileEntity;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import scala.tools.nsc.transform.patmat.PatternExpander;

/**
 * Created by filippo on 23/11/16.
 */
@JEIPlugin
public class ExtractorJEIPlugin implements IModPlugin {
    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {

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

        registry.addRecipes(ExtractorTileEntity.recipes);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    }
}
