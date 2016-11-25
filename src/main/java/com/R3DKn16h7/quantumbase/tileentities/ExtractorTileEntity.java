package com.R3DKn16h7.quantumbase.tileentities;

import com.R3DKn16h7.quantumbase.elements.ElementBase;
import com.R3DKn16h7.quantumbase.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class ExtractorTileEntity extends TileEntity
        implements ITickable, IInventory,
                   IFluidHandler, ICapabilityProvider {

    // Slot IDs
    static final public int inputSlot = 0;
    static final public int catalystSlot = 1;
    static final public int canisterSlot = 2;
    static final public int fuelSlot = 3;
    static final public int outputSlotStart = 4;
    static final public int outputSlotSize = 4;
    static final private int NumberOfSlots = 8;
    // Static register of recipes
    static public ArrayList<ExtractorRecipe> recipes;

    // Capabilities
    @CapabilityInject(IEnergyStorage.class)
    static Capability<IEnergyStorage> ENERGY_CAPABILITY = null;
    @CapabilityInject(IFluidHandler.class)
    static Capability<IFluidHandler> FLUID_CAPABILITY = null;

    // Internal storages
    public EnergyStorage storage = new EnergyStorage(1000);
    public FluidTank tank = new FluidTank(1000);
    private ItemStack[] inventory;

    //// Static constants
    private final static String name = "Extractor";
    static private final float ticTime = 5f;
    private final int consumedFuelPerTic = 1;
    static private final int consumedEnergyPerFuelRefill = 100;
    static private final int generatedFuelPerEnergyDrain = 100;

    //// Status variables
    // Are we currently smelting
    private boolean smelting = false;
    // Recipe Id currently smelting
    private int smeltingId = -1;
    // Has the input changed since last check?
    private boolean inputChanged = false;
    // Current progress oin smelting
    private int progress;
    private int storedFuel = 0;
    //
    private float elapsed = 0f;

    ExtractorTileEntity() {
        this.inventory = new ItemStack[this.getSizeInventory()];

        if (recipes == null) {
            recipes = new ArrayList<ExtractorRecipe>();
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
                            new ElementStack("Pu", 4, 0.5f),
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
     * @param outs A list of output Elements
     * @param energy The required energy (TODO: change to "cost") to perform a smelting operation.
     * @return Return false if registration failed (TODO)
     */
    static public boolean registerRecipe(Item item, Item catalyst,
                                         ElementStack[] outs, int energy) {
        recipes.add(new ExtractorRecipe(item, catalyst, outs, energy));
        return true;
    }

    @Override
    public boolean hasCapability(Capability<?> capability,
                                 EnumFacing facing) {
        if (capability == ENERGY_CAPABILITY
                || capability == FLUID_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability,
                               EnumFacing facing) {
        if (capability == ENERGY_CAPABILITY) {
            return (T) storage;
        } else if (capability == FLUID_CAPABILITY) {
            return (T) tank;
        }
        return super.getCapability(capability, facing);
    }

    public float getProgressPerc() {
        if(smeltingId == -1) return 0f;
        return progress / (float) recipes.get(smeltingId).energy;
    }

    public boolean canSmelt() {
        return canSmelt(smeltingId);
    }

    /**
     * Return true if recipe idx can be smelted.
     * @param idx
     * @return
     */
    public boolean canSmelt(int idx) {
        if (idx == -1) return false;
        ExtractorRecipe recipe = recipes.get(idx);
        if (inventory == null ||
                // Check input
                inventory[inputSlot] == null ||
                inventory[inputSlot].getItem() != recipe.item ||
                inventory[inputSlot].stackSize < 1 ||
                // Check catalyst
                recipe.catalyst != null && (inventory[catalystSlot] == null ||
                        inventory[catalystSlot].getItem() != recipe.catalyst ||
                        inventory[catalystSlot].stackSize < 1
                    )
                ) {
            return false;
        }

        return true;
    }

    /**
     * Try and consume one unit of fuel (depending on recipe)
     * if it fails, return false.
     * @return
     */
    public boolean tryConsumeFuel() {

        ExtractorRecipe recipe = recipes.get(smeltingId);
        if (storedFuel < consumedFuelPerTic) {
            ItemStack fuelItem = inventory[fuelSlot];
            int fuel = TileEntityFurnace.getItemBurnTime(fuelItem); //GameRegistry.getFuelValue(fuelItem);
            if (storage.getEnergyStored() > consumedEnergyPerFuelRefill) {
                storage.extractEnergy(consumedEnergyPerFuelRefill, false);
                storedFuel += generatedFuelPerEnergyDrain;
            } else if (fuelItem != null &&
                    fuelItem.stackSize >= 1 &&
                    fuel > 0) {
                fuelItem.stackSize -= 1;
                if(fuelItem.stackSize == 0) fuelItem = null;
                storedFuel += fuel;
            } else {
                return false;
            }
        }
        storedFuel -= consumedFuelPerTic;
        return true;
    }

    /**
     * Do "one tick" of smelting, check if recipe changed, and if it
     * is possible to continue smelting.
     */
    public void progressSmelting() {
        if (inputChanged) {
            findRecipe();
            inputChanged = false;
        }
        if (canSmelt()) {
            if( !tryConsumeFuel() ) return;
            ++progress;
            if (progress >= recipes.get(smeltingId).energy) {
                progress = 0;
                doneSmelting();
            }
        } else {
            progress = 0;
        }
    }

    public void doneSmelting() {
        inventory[inputSlot] = new ItemStack(inventory[inputSlot].getItem(),
                inventory[inputSlot].stackSize - 1);
        if (recipes.get(smeltingId).catalyst != null) {
            inventory[catalystSlot] = new ItemStack(inventory[catalystSlot].getItem(),
                    inventory[catalystSlot].stackSize - 1);
        }

        // For each Element output, flag telling if this has already been produced
        boolean[] done_out = new boolean[recipes.get(smeltingId).outs.length];

        // Place outputs in canisters, only if output can be placed (i.e. there isn't another element inside.
        // Try each output slot
        for (int rec_slot = outputSlotStart; rec_slot < outputSlotStart + outputSlotSize; ++rec_slot) {

            if (inventory[rec_slot] == null) {
                continue;
            }

            NBTTagCompound nbt;
            if (inventory[rec_slot].hasTagCompound()) {
                nbt = inventory[rec_slot].getTagCompound();
            } else {
                nbt = new NBTTagCompound();
            }

            // Try each generated element
            int i = 0;
            for (ElementStack rec_out : recipes.get(smeltingId).outs) {
                if (done_out[i]) {
                    ++i;
                    continue;
                }
                Minecraft.getMinecraft().thePlayer.sendChatMessage("enti" + i);
                if (nbt.hasKey("Element")) {
                    int elemId = nbt.getInteger("Element");
                    int qty = nbt.getInteger("Quantity");
                    if (elemId == rec_out.id) {
                        nbt.setInteger("Quantity", rec_out.quantity + qty);
                        inventory[rec_slot].setTagCompound(nbt);
                        done_out[i] = true;
                        break;
                    }
                } else {
                    nbt.setInteger("Element", rec_out.id);
                    nbt.setInteger("Quantity", rec_out.quantity);
                    inventory[rec_slot].setTagCompound(nbt);
                    done_out[i] = true;
                    break;
                }
                ++i;
            }
        }

        // If there is a canister in canister slot, pull if to the output slot and
        // fill it with element
        // Try each output slot
        for (int rec_slot = outputSlotStart; rec_slot < outputSlotStart + outputSlotSize; ++rec_slot) {

            if (inventory[rec_slot] == null) {
                // Try each element
                int i = 0;
                for (ElementStack rec_out : recipes.get(smeltingId).outs) {

                    // If Element has not been produced and there is a canister in
                    // the slot
                    if (done_out[i] == false && inventory[canisterSlot] != null &&
                            inventory[canisterSlot].stackSize > 0 &&
                            inventory[canisterSlot].getItem() == ModItems.canister) {
                        done_out[i] = true;
                        inventory[canisterSlot] = new ItemStack(inventory[canisterSlot].getItem(),
                                inventory[canisterSlot].stackSize - 1);
                        inventory[rec_slot] = new ItemStack(inventory[canisterSlot].getItem(), 1);
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setInteger("Element", rec_out.id);
                        nbt.setInteger("Quantity", rec_out.quantity);
                        inventory[rec_slot].setTagCompound(nbt);
                        break;
                    }

                    ++i;
                }
            }
        }
        this.markDirty();
    }

    /**
     *
     */
    public void abortSmelting() {
        smelting = false;
        smeltingId = -1;
    }

    /**
     * Find recipe that matches the input/catalyst slots
     */
    public void findRecipe() {
        int idx = 0;
        for (ExtractorRecipe recipe : recipes) {
            if (canSmelt(idx)) {
                smeltingId = idx;
                smelting = true;
                return;
            }

            ++idx;
        }
        abortSmelting();
    }

    /**
     * Update entity: check if needs to smelt item
     */
    @Override
    public void update() {
        if (elapsed > ticTime) {
            progressSmelting();
            elapsed = 0;
        } else {
            elapsed += 1;
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return NumberOfSlots;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index < 0 || index >= this.getSizeInventory())
            return null;
        return this.inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.getStackInSlot(index) != null) {
            ItemStack itemstack;

            if (this.getStackInSlot(index).stackSize <= count) {
                itemstack = this.getStackInSlot(index);
                this.setInventorySlotContents(index, null);
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.getStackInSlot(index).splitStack(count);

                if (this.getStackInSlot(index).stackSize <= 0) {
                    this.setInventorySlotContents(index, null);
                } else {
                    //Just to show that changes happened
                    this.setInventorySlotContents(index, this.getStackInSlot(index));
                }

                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = this.getStackInSlot(index);
        this.setInventorySlotContents(index, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {


        if (index < 0 || index >= this.getSizeInventory())
            return;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
            stack.stackSize = this.getInventoryStackLimit();
        if (stack != null && (index == inputSlot || index == catalystSlot)) {
            inputChanged = true;
        }

        if (stack != null && stack.stackSize == 0)
            stack = null;

        this.inventory[index] = stack;
        this.markDirty();
    }


    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.getPos()) == this &&
                player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == canisterSlot || index >= outputSlotStart || index < outputSlotStart + outputSlotSize) {
            if (stack.getItem() != ModItems.canister) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.getSizeInventory(); i++)
            this.setInventorySlotContents(i, null);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (this.getStackInSlot(i) != null) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                this.getStackInSlot(i).writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        nbt.setTag("Items", list);

        if (this.hasCustomName()) {
            nbt.setString("CustomName", "Name");
        }

        return nbt;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        NBTTagList list = nbt.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 255;
            this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
        }

        if (nbt.hasKey("CustomName", 8)) {
        }
    }


    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[0];
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
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
    static public class ExtractorRecipe {
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
    }
}
