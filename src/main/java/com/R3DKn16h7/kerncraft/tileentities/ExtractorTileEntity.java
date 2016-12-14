package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.elements.ElementBase;
import com.R3DKn16h7.kerncraft.items.Canister;
import com.R3DKn16h7.kerncraft.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;

public class ExtractorTileEntity extends SmeltingTileEntity {

    // Slot IDs
    static final public int inputSlot = 0;
    static final public int catalystSlot = 1;
    static final public int canisterSlot = 2;
    static final public int fuelSlot = 3;
    static final public int outputSlotStart = 4;
    static final public int outputSlotSize = 4;
    static final private int NumberOfSlots = 8;
    //// Static constants
    static private final int consumedEnergyPerFuelRefill = 100;
    static private final int generatedFuelPerEnergyDrain = 100;
    static private final int consumedFuelPerTic = 20;
    // Static register of recipes
    private final IItemHandler automationInput = new ItemStackHandler(4);
    private final IItemHandler automationOutput = new ItemStackHandler(4);

    public ExtractorTileEntity() {
        super(4, 4);

        if (recipes == null) {
            recipes = new ArrayList<ISmeltingRecipe>();
            registerRecipe(Item.getItemFromBlock(Blocks.IRON_ORE),
                    Item.getItemFromBlock(Blocks.ANVIL),
                    new ElementStack[]{new ElementStack(1, 1), new ElementStack(4, 1)}, 10);
            registerRecipe(Item.getItemFromBlock(Blocks.DIAMOND_ORE),
                    Item.getItemFromBlock(Blocks.LAPIS_BLOCK),
                    new ElementStack[]{new ElementStack(1, 1), new ElementStack(5, 5)}, 10);
            registerRecipe(Item.getItemFromBlock(Blocks.DIAMOND_BLOCK),
                    null,
                    new ElementStack[]{
                            new ElementStack("Be", 4, 0.5f),
                            new ElementStack("Ar", 5)
                    },
                    10);
            registerRecipe(Item.getItemFromBlock(Blocks.IRON_BLOCK),
                    null,
                    new ElementStack[]{
                            new ElementStack("Pu", 400, 0.5f),
                            new ElementStack("Pm", 5),
                            new ElementStack("H", 1, 0.8f),
                            new ElementStack("He", 2)
                    },
                    10);
        }
    }

    /**
     * Add new recipe
     * @param item Principal item that will allow to extract elements.
     * @param catalyst Additional item (optional) that acts as Catalyst
     * @param outs LAB_BONNET list of output Elements
     * @param energy The required energy (TODO: change to "cost") to perform a smelting operation.
     * @return Return false if registration failed (TODO)
     */
    static public boolean registerRecipe(Item item, Item catalyst,
                                         ElementStack[] outs, int energy) {
        recipes.add(new ExtractorRecipe(item, catalyst, outs, energy));
        return true;
    }

    @Override
    public boolean canSmelt(ISmeltingRecipe rec) {
        ExtractorRecipe ex_rec = ((ExtractorRecipe) rec);

        return !(input == null ||
                input.getStackInSlot(inputSlot) == null ||
                // Check input
                input.getStackInSlot(inputSlot).getItem() != ex_rec.item ||
                input.getStackInSlot(inputSlot).stackSize < 1 ||
                // Check catalyst
                ex_rec.catalyst != null &&
                        (input.getStackInSlot(catalystSlot) == null ||
                                input.getStackInSlot(catalystSlot).getItem() != ex_rec.catalyst ||
                                input.getStackInSlot(catalystSlot).stackSize < 1
                        ));
    }

    /**
     * Try and consume one unit of fuel (depending on recipe)
     * if it fails, return false.
     * @return
     */
    @Override
    public boolean tryProgress() {
        ExtractorRecipe recipe = ((ExtractorRecipe) currentlySmelting);
        if (storedFuel < consumedFuelPerTic) {
            ItemStack fuelItem = input.getStackInSlot(fuelSlot);
            int fuel = TileEntityFurnace.getItemBurnTime(fuelItem); //GameRegistry.getFuelValue(fuelItem);
            if (storage.getEnergyStored() > consumedEnergyPerFuelRefill) {
                storage.extractEnergy(consumedEnergyPerFuelRefill, false);
                storedFuel += generatedFuelPerEnergyDrain;
            } else if (fuelItem != null &&
                    fuelItem.stackSize >= 1 &&
                    fuel > 0) {
                fuelItem.stackSize -= 1;
                if (fuelItem.stackSize == 0) {
                    fuelItem = null;
                    input.setStackInSlot(fuelSlot, null);
                }
                storedFuel += fuel;
            } else {
                return false;
            }
        }
        storedFuel -= consumedFuelPerTic;
        return true;
    }

    @Override
    public void doneSmelting() {
        ExtractorRecipe recipe = ((ExtractorRecipe) currentlySmelting);

        input.setStackInSlot(inputSlot, new ItemStack(input.getStackInSlot(inputSlot).getItem(),
                input.getStackInSlot(inputSlot).stackSize - 1));
        if (input.getStackInSlot(inputSlot).stackSize == 0) input.setStackInSlot(inputSlot, null);
        if (recipe.catalyst != null) {
            input.setStackInSlot(catalystSlot, new ItemStack(input.getStackInSlot(catalystSlot).getItem(),
                    input.getStackInSlot(catalystSlot).stackSize - 1));
            if (input.getStackInSlot(catalystSlot).stackSize == 0) input.setStackInSlot(catalystSlot, null);
        }

        // For each Element output, flag telling if this has already been produced
        int[] remaining = new int[recipe.outs.length];
        for (int i = 0; i < recipe.outs.length; ++i) {
            remaining[i] = recipe.outs[i].quantity;
        }

        // Place outputs in canisters, only if output can be placed (i.e. there isn't another element inside.
        // Try each output slot
        for (int rec_slot = 0; rec_slot < outputSlotSize; ++rec_slot) {
            ItemStack out = output.getStackInSlot(rec_slot);
            if (out == null) {
                continue;
            }

            NBTTagCompound nbt;
            if (out.hasTagCompound()) {
                nbt = out.getTagCompound();
            } else {
                nbt = new NBTTagCompound();
            }

            // Try each generated element
            int i = 0;
            for (ElementStack rec_out : recipe.outs) {
                // If all output has been produced break
                if (remaining[i] <= 0) {
                    ++i;
                    continue;
                }
                if (nbt.hasKey("Element")) {
                    int elemId = nbt.getInteger("Element");
                    int qty = nbt.getInteger("Quantity");
                    if (elemId == rec_out.id) {
                        if (remaining[i] + qty <= Canister.CAPACITY) {
                            nbt.setInteger("Quantity", remaining[i] + qty);
                            remaining[i] = 0;
                            out.setTagCompound(nbt);
                            break;
                        } else {
                            remaining[i] -= Canister.CAPACITY - qty;
                            nbt.setInteger("Quantity", Canister.CAPACITY);
                            out.setTagCompound(nbt);
                        }
                    }
                } else {
                    nbt.setInteger("Element", rec_out.id);
                    nbt.setInteger("Quantity", rec_out.quantity);
                    out.setTagCompound(nbt);
                    remaining[i] = 0;
                    break;
                }
                ++i;
            }

        }

        // If there is a CANISTER in CANISTER slot, pull if to the output slot and
        // fill it with element
        // Try each output slot
        for (int rec_slot = 0; rec_slot < outputSlotSize; ++rec_slot) {

            ItemStack out = output.getStackInSlot(rec_slot);
            if (out == null) {
                // Try each element
                int i = 0;
                for (ElementStack rec_out : recipe.outs) {

                    // If Element has not been produced and there is a CANISTER in
                    // the slot
                    if (remaining[i] > 0 && input.getStackInSlot(canisterSlot) != null &&
                            input.getStackInSlot(canisterSlot).stackSize > 0 &&
                            input.getStackInSlot(canisterSlot).getItem() == ModItems.CANISTER) {
                        remaining[i] = 0;
                        output.setStackInSlot(rec_slot, new ItemStack(input.getStackInSlot(canisterSlot).getItem(), 1));
                        input.setStackInSlot(canisterSlot, new ItemStack(input.getStackInSlot(canisterSlot).getItem(),
                                input.getStackInSlot(canisterSlot).stackSize - 1));
                        if (input.getStackInSlot(canisterSlot).stackSize == 0) input.setStackInSlot(canisterSlot, null);
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setInteger("Element", rec_out.id);
                        nbt.setInteger("Quantity", rec_out.quantity);
                        output.getStackInSlot(rec_slot).setTagCompound(nbt);
                        break;
                    }

                    ++i;
                }
            }
        }
        this.markDirty();
    }


    static public class ElementStack {
        public int id;
        public int quantity;
        public float prob = 1.0f;

        public ElementStack(int id_, int quantity_) {
            id = id_;
            quantity = quantity_;
        }

        public ElementStack(String name_, int quantity_) {
            id = ElementBase.symbolToId(name_);
            quantity = quantity_;
        }

        public ElementStack(int id_, int quantity_, float prob_) {
            this(id_, quantity_);
            prob = prob_;
        }

        public ElementStack(String name_, int quantity_, float prob_) {
            this(name_, quantity_);
            prob = prob_;
        }
    }

    /**
     * Represents the recipe for the extractor.
     */
    static public class ExtractorRecipe implements ISmeltingRecipe {
        public Item item;
        public Item catalyst;
        public ElementStack[] outs;
        public int energy;

        public ExtractorRecipe(Item item_, Item catalyst_,
                               ElementStack[] outs_, int energy_) {
            item = item_;
            catalyst = catalyst_;
            outs = outs_;
            energy = energy_;
        }

        @Override
        public int getCost() {
            return energy;
        }
    }
}
