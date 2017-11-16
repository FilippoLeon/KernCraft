package com.R3DKn16h7.kerncraft.crafting;

import com.R3DKn16h7.kerncraft.capabilities.element.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.element.IElementContainer;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Filippo on 10/12/2016.
 */
public class ElementRecipe implements IRecipe {
    //Added in for future ease of change, but hard coded for now.
    public static final int MAX_CRAFT_GRID_WIDTH = 3;
    public static final int MAX_CRAFT_GRID_HEIGHT = 3;
    public int width = 0;
    public int height = 0;
    @Nonnull
    protected ItemStack output = ItemStack.EMPTY;
    protected Object[] input = null;
    protected boolean mirrored = true;

    public ElementRecipe(Block result, Object... recipe) {
        this(new ItemStack(result), recipe);
    }

    public ElementRecipe(Item result, Object... recipe) {
        this(new ItemStack(result), recipe);
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        return null;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public IRecipe setRegistryName(ResourceLocation name) {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return null;
    }

    public ElementRecipe(@Nonnull ItemStack result, Object... recipe) {
        output = result.copy();

        String shape = "";
        int idx = 0;

        if (recipe[idx] instanceof Boolean) {
            mirrored = (Boolean) recipe[idx];
            if (recipe[idx + 1] instanceof Object[]) {
                recipe = (Object[]) recipe[idx + 1];
            } else {
                idx = 1;
            }
        }

        if (recipe[idx] instanceof String[]) {
            String[] parts = ((String[]) recipe[idx++]);

            for (String s : parts) {
                width = s.length();
                shape += s;
            }

            height = parts.length;
        } else {
            while (recipe[idx] instanceof String) {
                String s = (String) recipe[idx++];
                shape += s;
                width = s.length();
                height++;
            }
        }

        if (width * height != shape.length()) {
            String ret = "Invalid shaped ore recipe: ";
            for (Object tmp : recipe) {
                ret += tmp + ", ";
            }
            ret += output;
            throw new RuntimeException(ret);
        }

        HashMap<Character, Object> itemMap = new HashMap<Character, Object>();

        for (; idx < recipe.length; idx += 2) {
            Character chr = (Character) recipe[idx];
            Object in = recipe[idx + 1];

            if (in instanceof ItemStack) {
                itemMap.put(chr, ((ItemStack) in).copy());
            } else if (in instanceof Item) {
                itemMap.put(chr, new ItemStack((Item) in));
            } else if (in instanceof Block) {
                itemMap.put(chr, new ItemStack((Block) in, 1, OreDictionary.WILDCARD_VALUE));
            } else if (in instanceof String) {
                itemMap.put(chr, OreDictionary.getOres((String) in));
            } else if (in instanceof ElementStack) {
                //// KERNCRAFT EDIT: include element stacks
                itemMap.put(chr, in);
            } else {
                String ret = "Invalid shaped ore recipe: ";
                for (Object tmp : recipe) {
                    ret += tmp + ", ";
                }
                ret += output;
                throw new RuntimeException(ret);
            }
        }

        input = new Object[width * height];
        int x = 0;
        for (char chr : shape.toCharArray()) {
            input[x++] = itemMap.get(chr);
        }
    }

    ElementRecipe(ShapedRecipes recipe, Map<ItemStack, String> replacements) {
        output = recipe.getRecipeOutput();
        width = recipe.recipeWidth;
        height = recipe.recipeHeight;

        input = new Object[recipe.recipeItems.size()];

        for (int i = 0; i < input.length; i++) {
            Ingredient ingredient = recipe.recipeItems.get(i);

            if (ingredient == null) continue;

            input[i] = recipe.recipeItems.get(i);

            for (Map.Entry<ItemStack, String> replace : replacements.entrySet()) {
                if (ingredient.test(replace.getKey())) {
                    input[i] = OreDictionary.getOres(replace.getValue());
                    break;
                }
            }
        }
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
        return output.copy();
    }

    /**
     * Returns the size of the recipe area
     */
//    @Override
    public int getRecipeSize() {
        return input.length;
    }



    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return output;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        for (int x = 0; x <= MAX_CRAFT_GRID_WIDTH - width; x++) {
            for (int y = 0; y <= MAX_CRAFT_GRID_HEIGHT - height; ++y) {
                if (checkMatch(inv, x, y, false)) {
                    return true;
                }

                if (mirrored && checkMatch(inv, x, y, true)) {
                    return true;
                }
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    protected boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++) {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++) {
                int subX = x - startX;
                int subY = y - startY;
                Object target = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
                    if (mirror) {
                        target = input[width - subX - 1 + subY * width];
                    } else {
                        target = input[subX + subY * width];
                    }
                }

                ItemStack slot = inv.getStackInRowAndColumn(x, y);

                if (target instanceof ItemStack) {
                    if (!OreDictionary.itemMatches((ItemStack) target, slot, false)) {
                        return false;
                    }
                } else if (target instanceof List) {
                    boolean matched = false;

                    Iterator<ItemStack> itr = ((List<ItemStack>) target).iterator();
                    while (itr.hasNext() && !matched) {
                        matched = OreDictionary.itemMatches(itr.next(), slot, false);
                    }

                    if (!matched) {
                        return false;
                    }
                } else if (target instanceof ElementStack) {
                    // KERNCRAFT EDIT: MATCH ELEMENTS STACKS
                    // target: the value in the recipe
                    // slot: the item currently in the grid

                    // If item has not capability forget about it
                    if (!ElementCapabilities.hasCapability(slot)) return false;
                    IElementContainer cap = ElementCapabilities.getCapability(slot);
                    int amount = ElementCapabilities.remove(slot, ((ElementStack) target), true);
                    if (amount != ((ElementStack) target).quantity) {
                        return false;
                    }
                } else if (target == null && !slot.isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    // KERNCRAFT EDIT: RETURN VALUE MATCHING THIS
    public ElementRecipe setMirrored(boolean mirror) {
        mirrored = mirror;
        return this;
    }

    /**
     * Returns the input for this recipe, any mod accessing this value should never
     * manipulate the values in this array as it will effect the recipe itself.
     *
     * @return The recipes input vales.
     */
    public Object[] getInput() {
        return this.input;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) //getRecipeLeftovers
    {
        /// KERNCRAFT CHANGE
        // KERNCRAFT EDIT: EXTRA HOOKS
        NonNullList<ItemStack> lst = NonNullList.create();
        ItemStack[][] returnGrid = new ItemStack[MAX_CRAFT_GRID_WIDTH][MAX_CRAFT_GRID_HEIGHT];

        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++) {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++) {
                ItemStack slot = inv.getStackInRowAndColumn(x, y);
                if (slot.isEmpty() || !ElementCapabilities.hasCapability(slot)) continue;

                returnGrid[x][y] = slot.copy();

            }
        }

        for (Object target : input) {
            if (target instanceof ElementStack) {
                int to_remove = ((ElementStack) target).quantity;

                outerloop:
                for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++) {
                    for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++) {
                        ItemStack slot = returnGrid[x][y];
                        if (slot == null || slot.isEmpty() || !ElementCapabilities.hasCapability(slot)) continue;

                        IElementContainer cap = ElementCapabilities.getCapability(slot);
                        if (to_remove < 0) {
                            to_remove += cap.addAmountOf(((ElementStack) target).id, -to_remove, false);
                        } else {
                            to_remove -= cap.removeAmountOf(((ElementStack) target).id, to_remove, false);
                        }

                        if (to_remove == 0) {
                            break outerloop;
                        }
                    }
                }
            }
        }

        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++) {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++) {
                ItemStack slot = returnGrid[x][y];
                if (slot != null && !slot.isEmpty()) {
                    lst.add(returnGrid[x][y]);
                }
            }
        }

        lst.addAll(ForgeHooks.defaultRecipeGetRemainingItems(inv));

        return lst;
    }

}