package com.R3DKn16h7.kerncraft.crafting;

import com.R3DKn16h7.kerncraft.items.ModItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Filippo on 10/12/2016.
 */
public class ElementRecipe implements IRecipe {

    public static final int ANY_ELEMENT = -1;
    Item result;
    NonNullList<ItemStack> ingredients;
    int[] id;
    int[] qty;

    public ElementRecipe(Item result, NonNullList<ItemStack> ingredients, int[] id, int[] qty) {

        this.result = result;
        this.ingredients = ingredients;
        this.id = id;
        this.qty = qty;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        boolean[] id_ok = new boolean[id.length];
        int[] remaining = qty.clone();
        boolean[] ingredients_ok = new boolean[ingredients.size()];
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack is = inv.getStackInSlot(i);
            if (is == ItemStack.EMPTY) continue;
            if (is.getItem() == ModItems.CANISTER) {
                NBTTagCompound comp = is.getTagCompound();
                if (comp != null && comp.hasKey("Element")) {
                    for (int j = 0; j < id.length; ++j) {
                        int elem = id[j];
                        if (elem == ANY_ELEMENT || elem == comp.getInteger("Element")) {
                            int q = comp.getInteger("Quantity");
                            remaining[j] -= q;
                            if (remaining[j] <= 0) {
                                id_ok[j] = true;
                                break;
                            }
                        }
                    }
                }
            } else {
                for (int j = 0; j < ingredients.size(); ++j) {
                    if (ingredients.get(j).getItem() == inv.getStackInSlot(i).getItem()) {
                        ingredients_ok[j] = true;
                    }
                }
            }
        }

        for (int i = 0; i < id_ok.length; ++i) {
            if (!id_ok[i]) return false;
        }
        for (int i = 0; i < ingredients_ok.length; ++i) {
            if (!ingredients_ok[i]) return false;
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {

        return new ItemStack(result, 1);
    }

    @Override
    public int getRecipeSize() {
        return 3;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack is = inv.getStackInSlot(i);
            if (is == ItemStack.EMPTY) continue;
            if (is.getItem() == ModItems.CANISTER) {

            } else {
                for (int j = 0; j < ingredients.size(); ++j) {
                    if (ingredients.get(j).getItem() == inv.getStackInSlot(i).getItem()) {
                        inv.decrStackSize(i, 1);
                    }
                }
            }
        }

        NonNullList<ItemStack> a = NonNullList.<ItemStack>create();
        a.add(ItemStack.EMPTY);
        return a;


    }
}
