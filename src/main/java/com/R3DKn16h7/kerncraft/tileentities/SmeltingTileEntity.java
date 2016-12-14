package com.R3DKn16h7.kerncraft.tileentities;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
    static protected final float ticTime = 5f;
    //// Static constants
    // Static register of recipes
    static public ArrayList<ISmeltingRecipe> recipes;
    protected final IItemHandler automationInput;
    protected final IItemHandler automationOutput;
    // Internal storages
    public EnergyStorage storage;
    public FluidTank tank;
    // Has the input changed since last check?
    public boolean inputChanged = false;
    protected boolean smelting = false;
    // Recipe Id currently smelting
    protected ISmeltingRecipe currentlySmelting;
    // Current progress oin smelting
    protected int progress;
    protected int storedFuel = 0;
    //
    protected float elapsed = 0f;
    int mode = 0;

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
        if (currentlySmelting == null) return 0f;
        return progress / (float) currentlySmelting.getCost();
    }

    public boolean canSmelt() {
        return canSmelt(currentlySmelting);
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
        if (currentlySmelting == null) return;
        if (canSmelt()) {
            if (!tryProgress()) return;
            ++progress;
            if (progress >= currentlySmelting.getCost()) {
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
        currentlySmelting = null;
    }

    /**
     * Find recipe that matches the input/catalyst slots
     */
    public void findRecipe() {
        if (recipes == null) return;
        for (ISmeltingRecipe recipe : recipes) {
            if (canSmelt(recipe)) {
                currentlySmelting = recipe;
                smelting = true;
                return;
            }
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

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("RedstoneMode"))
            mode = nbt.getInteger("RedstoneMode");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        nbt.setInteger("RedstoneMode", mode);
        return nbt;
    }

    @Override
    public void restoreFromNBT(NBTTagCompound nbt) {
        super.restoreFromNBT(nbt);

        if (nbt != null && nbt.hasKey("RedstoneMode"))
            mode = nbt.getInteger("RedstoneMode");
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
