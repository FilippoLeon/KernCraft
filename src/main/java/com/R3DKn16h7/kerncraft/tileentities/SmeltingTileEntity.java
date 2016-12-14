package com.R3DKn16h7.kerncraft.tileentities;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;

abstract public class SmeltingTileEntity
        extends MachineTileEntity
        implements IRedstoneSettable {

    //// Status variables
    // Are we currently smelting
    static private final float ticTime = 5f;
    //// Static constants
    // Static register of recipes
    static public ArrayList<ISmeltingRecipe> recipes;
    private final IItemHandler automationInput;
    private final IItemHandler automationOutput;
    // Internal storages
    public EnergyStorage storage;
    public FluidTank tank;
    // Has the input changed since last check?
    public boolean inputChanged = false;
    int mode = 0;
    private boolean smelting = false;
    // Recipe Id currently smelting
    private int smeltingId = -1;
    // Current progress oin smelting
    private int progress;
    private int storedFuel = 0;
    //
    private float elapsed = 0f;

    public SmeltingTileEntity(int inputSize, int outputSize) {
        super(inputSize, outputSize);
        automationInput = new ItemStackHandler(4);
        automationOutput = new ItemStackHandler(4);
        // Internal storages
        storage = new EnergyStorage(1000);
        tank = new FluidTank(1000);
    }

    /**
     * Add new recipe
     *
     * @return Return false if registration failed (TODO)
     */
    static public boolean registerRecipe(ISmeltingRecipe rec) {
        recipes.add(rec);
        return true;
    }

    public int getRedstoneMode() {
        return mode;
    }

    @Override
    public boolean hasCapability(Capability<?> capability,
                                 EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY ||
                capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ||
                capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability,
                               EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(storage);
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        } else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            //return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(automationInput);
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(input);
        }
        return super.getCapability(capability, facing);
    }

    public float getProgressPerc() {
        if (smeltingId == -1) return 0f;
        return progress / (float) recipes.get(smeltingId).getCost();
    }

    public boolean canSmelt() {
        return canSmelt(smeltingId);
    }

    /**
     * Return true if recipe idx can be smelted.
     *
     * @param idx
     * @return
     */
    public boolean canSmelt(int idx) {
        if (idx == -1) return false;
        ISmeltingRecipe recipe = recipes.get(idx);
        return canSmelt(recipe);
    }

    abstract public boolean canSmelt(ISmeltingRecipe rec);

    abstract public boolean tryProgress();

    abstract public void doneSmelting();

    /**
     * Do "one tick" of smelting, check if recipe changed, and if it
     * is possible to continue smelting.
     */
    public void progressSmelting() {

        if (mode == 0 && !this.worldObj.isBlockPowered(pos)) return;
        if (mode == 1 && this.worldObj.isBlockPowered(pos)) return;

        if (inputChanged) {
            findRecipe();
            inputChanged = false;
        }
        if (canSmelt()) {
            if (!tryProgress()) return;
            ++progress;
            if (progress >= recipes.get(smeltingId).getCost()) {
                progress = 0;
                doneSmelting();
            }
        } else {
            progress = 0;
        }
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
        for (ISmeltingRecipe recipe : recipes) {
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

    public float getFuelStoredPercentage() {
        return Math.min(storedFuel / (float)
                        TileEntityFurnace.getItemBurnTime(new ItemStack(Items.COAL)),
                1.0f
        );
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
