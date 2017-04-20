package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.crafting.ISmeltingRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;

abstract public class SmeltingTileEntity
        extends MachineTileEntity
        implements IRedstoneSettable, IFuelUser,
                   IEnergyContainer, IProgressMachine, IFluidStorage {

    //// Status variables
    // Are we currently smelting
    private static final float ticTime = 5f;
    //// Static constants
    // Internal energy storage
    EnergyStorage storage;
    FluidTank tank;
    // Is the machine currently smelting
    private boolean smelting = false;
    // Recipe Id currently smelting
    ISmeltingRecipe currentlySmelting;
    // Current progress oin smelting
    private int progress;
    int storedFuel = 0;
    //
    private float elapsed = 0f;
    private int redstoneMode = 0;

    public SmeltingTileEntity(int inputSize, int outputSize) {
        super(inputSize, outputSize);

        // Internal storages
        storage = new EnergyStorage(100000);
        tank = new FluidTank(16000);
    }

    @Override
    public float getProgressPercent() {
        if (currentlySmelting == null) return 0f;
        return progress / (float) currentlySmelting.getCost();
    }
    @Override
    public float getFuelStoredPercent() {
        return Math.min(storedFuel / (float)
                        TileEntityFurnace.getItemBurnTime(new ItemStack(Items.COAL)),
                1.0f
        );
    }

    @Override
    public int getEnergyStored() {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return storage.getMaxEnergyStored();
    }

    @Override
    public int getCapacity() {
        return tank.getCapacity();
    }

    @Override
    public FluidStack getFluid() {
        return tank.getFluid();
    }

    @Override
    public int getFluidAmount() {
        return tank.getFluidAmount();
    }

    @Override
    public int getRedstoneMode() {
        return redstoneMode;
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
        }
        return super.getCapability(capability, facing);
    }

    private boolean canSmelt() {
        return canSmelt(currentlySmelting);
    }

    abstract public boolean canSmelt(ISmeltingRecipe rec);

    abstract public boolean tryProgress();

    abstract public void doneSmelting();

    public boolean isSmelting() {
        return smelting;
    }

    private void setSmelting(boolean isSmelting) {
        if(smelting != isSmelting) {
            IBlockState state = world.getBlockState(this.pos);
            world.setBlockState(this.pos, state.withProperty(MachineBlock.POWERED, smelting));
        }
        smelting = isSmelting;
    }

    public void stop() {
        smelting = false;
        abortSmelting();
    }

    /**
     * Do "one tick" of smelting, check if recipe changed, and if it
     * is possible to continue smelting.
     */
    private void progressSmelting() {
        // Machine cannot porocess due to redstone
        if ((redstoneMode == 0 && !this.world.isBlockPowered(pos)) || (redstoneMode == 1 && this.world.isBlockPowered(pos))) {
            setSmelting(false);
            return;
        }

        if (inputChanged) {
            findRecipe();
            inputChanged = false;
        }
        if (currentlySmelting == null) {
            setSmelting(false);
            return;
        }

        if (canSmelt()) {
            if (!tryProgress()) {
                setSmelting(false);
                return;
            } else {
                setSmelting(true);
            }
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
        setSmelting(false);
        currentlySmelting = null;
    }

    public abstract List<ISmeltingRecipe> getRecipes();

    /**
     * Find recipe that matches the input/catalyst slots
     */
    public void findRecipe() {
        if (getRecipes() == null) return;
        for (ISmeltingRecipe recipe : getRecipes()) {
            if (canSmelt(recipe)) {
                currentlySmelting = recipe;
                setSmelting(true);
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

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        restoreFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);

        nbt.setInteger("redstoneMode", redstoneMode);

        NBTTagCompound nbt_tank = new NBTTagCompound();
        tank.writeToNBT(nbt_tank);
        nbt.setTag("tank", nbt_tank);

        nbt.setInteger("energyStored", storage.getEnergyStored());
        nbt.setInteger("maxEnergyStored", storage.getMaxEnergyStored());
        return nbt;
    }

    @Override
    public void restoreFromNBT(NBTTagCompound nbt) {
        super.restoreFromNBT(nbt);

        if (nbt != null) {
            if (nbt.hasKey("redstoneMode")) {
                redstoneMode = nbt.getInteger("redstoneMode");
            }
            if (nbt.hasKey("tank")) {
                tank.readFromNBT(nbt.getCompoundTag("tank"));
            }
            if (nbt.hasKey("maxEnergyStored") && nbt.getInteger("maxEnergyStored") != storage.getMaxEnergyStored()) {
                int temp = storage.getEnergyStored();
                storage = new EnergyStorage( nbt.getInteger("maxEnergyStored"));
                storage.receiveEnergy(temp,false);
            }
            if (nbt.hasKey("energyStored")) {
                storage.extractEnergy(1000000000, false);
                storage.receiveEnergy(nbt.getInteger("energyStored"),false);
            }
        }
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        SPacketUpdateTileEntity packet = super.getUpdatePacket();
        NBTTagCompound tag = packet != null ? packet.getNbtCompound() : new NBTTagCompound();

        writeToNBT(tag);

        return new SPacketUpdateTileEntity(pos, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt.getNbtCompound();
        readFromNBT(tag);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
    }

    @Override
    public void setRedstoneMode(int mode) {
        world.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
        this.markDirty();
        this.redstoneMode = mode;
    }
}
