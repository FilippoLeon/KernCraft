package com.R3DKn16h7.kerncraft.plugins.JEI;

import com.R3DKn16h7.kerncraft.tileentities.ExtractorTileEntity;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

/**
 * Created by filippo on 23/11/16.
 */
public class ExtractorJEIRecipeHandler implements IRecipeHandler {
    @Override
    public Class getRecipeClass() {
        return ExtractorTileEntity.ExtractorRecipe.class;
    }


    @Override
    public String getRecipeCategoryUid(Object recipe) {
        return "kerncraft.extractor";
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(Object recipe) {
        ExtractorJEIRecipeWrapper ret = new ExtractorJEIRecipeWrapper((ExtractorTileEntity.ExtractorRecipe) recipe);

        return ret;
    }

    @Override
    public boolean isRecipeValid(Object recipe) {
        return true;
    }
}
