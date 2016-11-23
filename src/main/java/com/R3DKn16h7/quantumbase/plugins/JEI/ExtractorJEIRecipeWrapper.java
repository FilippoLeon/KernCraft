package com.R3DKn16h7.quantumbase.plugins.JEI;

import com.R3DKn16h7.quantumbase.items.ModItems;
import com.R3DKn16h7.quantumbase.tileentities.ExtractorTileEntity;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippo on 23/11/16.
 */
public class ExtractorJEIRecipeWrapper extends BlankRecipeWrapper {

    ExtractorTileEntity.ExtractorRecipe recipe;

    ExtractorJEIRecipeWrapper(ExtractorTileEntity.ExtractorRecipe recipe_) {
        recipe = recipe_;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ArrayList<ItemStack> inputs = new ArrayList<ItemStack>();

        inputs.add(new ItemStack(recipe.item));
        if(recipe.catalyst != null) inputs.add(new ItemStack(recipe.catalyst));

        ItemStack is = new ItemStack(ModItems.canister);
        NBTTagCompound nbt = is.getTagCompound();
        if(nbt == null) {
            nbt = new NBTTagCompound();
        }

        nbt.setInteger("Element", recipe.outs[0].id);
        is.setTagCompound(nbt);

        inputs.add(is);
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, is);
    }

    @Override
    public void drawInfo(Minecraft minecraft,
                         int recipeWidth, int recipeHeight,
                         int mouseX, int mouseY) {

    }

    @Override
    public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight) {

    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return null;
    }

    @Override
    public boolean handleClick(Minecraft minecraft,
                               int mouseX, int mouseY,
                               int mouseButton) {
        return false;
    }
}
