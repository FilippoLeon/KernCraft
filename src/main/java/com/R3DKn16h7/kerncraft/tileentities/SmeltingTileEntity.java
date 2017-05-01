package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.crafting.ISmeltingRecipe;
import com.R3DKn16h7.kerncraft.guicontainer.SmeltingContainer;
import com.R3DKn16h7.kerncraft.tileentities.utils.SideConfiguration;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

abstract public class SmeltingTileEntity<SmeltingRecipe extends ISmeltingRecipe>
        extends MachineTileEntity
        implements IRedstoneSettable, IFuelUser,
                   IEnergyContainer, IProgressMachine, IFluidStorage {

    //// Status variables
    // Are we currently smelting
    static final float ticTime = 5f;
    public FluidTank tank;
    // Is the machine currently smelting
    protected boolean smelting = false;
    // Current progress oin smelting
    protected float progress;
    //
    protected float elapsed = 0f;
    protected int redstoneMode = 0;
    //// Static constants
    // Internal energy storage
    EnergyStorage storage;
    int storedFuel = 0;
    // Recipe Id currently smelting
    SmeltingRecipe currentlySmelting;

    /// TODO FIXME ARGH THIS SUCKS BIG TIME; HOW TO DO THIS PROPERLY?
    int elapsedT = 0;
    boolean lastSmeltig = false;
    private boolean must_spin_down = false;

    public SmeltingTileEntity() {
        super();

        // Internal storages
        storage = new EnergyStorage(100000);
        tank = new FluidTank(16000);
    }

    @Override
    public float getProgressPercent() {
        return progress;
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
    public void setRedstoneMode(int mode) {
        world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
        this.markDirty();
        this.redstoneMode = mode;
    }

    @Override
    public boolean hasCapability(Capability<?> capability,
                                 EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY ||
                capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
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

    abstract public boolean canSmelt(SmeltingRecipe rec);

    abstract public boolean tryProgress();

    abstract public void doneSmelting();

    public boolean isSmelting() {
        return smelting;
    }

    private void setSmelting(boolean isSmelting) {
        if(smelting != isSmelting) {
            IBlockState state = world.getBlockState(this.pos);
            world.setBlockState(this.pos, state.withProperty(MachineBlock.POWERED, isSmelting));
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
        if (getRecipes() != null && currentlySmelting == null) {
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
            progress += progressPerOperation();
            if (progress >= 1.) {
                progress = 0;
                doneSmelting();
            }
        } else {
            progress = 0;
        }
    }

    protected double progressPerOperation() {
        return 1. / (double) currentlySmelting.getCost();
    }

    /**
     *
     */
    public void abortSmelting() {
        setSmelting(false);
        currentlySmelting = null;
    }

    public abstract List<SmeltingRecipe> getRecipes();

    /**
     * Find recipe that matches the input/catalyst slots
     */
    public void findRecipe() {
        if (getRecipes() == null) return;
        for (SmeltingRecipe recipe : getRecipes()) {
            if (canSmelt(recipe)) {
                currentlySmelting = recipe;
                setSmelting(true);
                return;
            }
        }
        abortSmelting();
    }

    @Override
    public SideConfiguration getSideConfig() {
        return sideConfig;
    }

    /**
     * Update entity: check if needs to smelt item
     */
    @Override
    public void update() {
        if (!world.isRemote) {
            if (elapsed > ticTime) {
                progressSmelting();
//                world.scheduleBlockUpdate(getPos(), blockType, 0, 1000);
                elapsed = 0;
            } else {
                elapsed += 1;
            }
        }


        /// TODO FIXME ARGH THIS SUCKS BIG TIME; HOW TO DO THIS PROPERLY?
        /// IS THIS LAGGY? I BETCHA
        if (!world.isRemote) {
//            if (this.blockType == null) return;
            int maxElapsed = 0;
            int spinUpTime = 93;

            boolean smeltig2;
            try {
                smeltig2 = world.getBlockState(getPos()).getValue(MachineBlock.POWERED);
            } catch (Exception e) {
                return;
            }

            if (smeltig2 != lastSmeltig) {
                if (smeltig2) {
                    must_spin_down = false;
                    elapsedT = 0;
                } else {
                    must_spin_down = true;
                }
                lastSmeltig = smeltig2;
            }

            // FIXME
//
//            if (smeltig2) {
//                must_spin_down = false;
//                if (elapsedT == 0) {
//                    world.playSound(null, getPos(),
//                            KernCraftSounds.CENTRIFUGE_SPIN_UP, SoundCategory.BLOCKS,
//                            0.5F, 1.0F
//                    );
//                } else if (elapsedT >= spinUpTime) {
//                    world.playSound(null, getPos(),
//                            KernCraftSounds.CENTRIFUGE, SoundCategory.BLOCKS,
//                            0.5F, 1.0F
//                    );
//                    elapsedT = spinUpTime;
//                }
//            } else if (elapsedT >= spinUpTime) {
//                if (must_spin_down) {
//                    world.playSound(null, getPos(),
//                            KernCraftSounds.CENTRIFUGE_SPIN_DOWN, SoundCategory.BLOCKS,
//                            0.5F, 1.0F
//                    );
//                    must_spin_down = false;
//                }
//                elapsedT = 0;
//            }
            elapsedT++;
        }

    }

    public int getField(int id) {
        switch (id) {
            case SmeltingContainer.FUEL_ID:
                return storedFuel;
            case SmeltingContainer.PROGRESS_ID:
                return (int) Math.floor(progress * 100);
            case SmeltingContainer.FLUID_AMOUNT:
                return this.tank.getFluidAmount();
            case SmeltingContainer.ENERGY:
                return this.getEnergyStored();
            case SmeltingContainer.REDSTONE_MODE:
                return this.getRedstoneMode();
        }
        if (id < 0) {
            return sideConfig.getSlotSide(-id).getValue();
        }
        return 0;
    }

    public void setField(int id, int value) {
        switch (id) {
            case SmeltingContainer.FUEL_ID:
                this.storedFuel = value;
                break;
            case SmeltingContainer.PROGRESS_ID:
                this.progress = value / 100.f;
                break;
            case SmeltingContainer.FLUID_AMOUNT:
//                this.tank.setFluid(new FluidStack(FluidRegistry.LAVA, value));
                break;
            case SmeltingContainer.ENERGY:
                this.storage.receiveEnergy(value - this.storage.getEnergyStored(), false);
                break;
            case SmeltingContainer.REDSTONE_MODE:
                this.setRedstoneMode(value);
                break;
        }
        if (id < 0) {
            sideConfig.setSlotSide(-id, value);
        }
    }

    public int getFieldCount() {
        return SmeltingContainer.FIELDS;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        inputChanged = true;
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
        nbt.setInteger("fuel", storedFuel);
        nbt.setFloat("progress", progress);
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
            if (nbt.hasKey("fuel")) {
                storedFuel = nbt.getInteger("fuel");
            }
            if (nbt.hasKey("progress")) {
                progress = nbt.getFloat("progress");
            }
        }
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public SPacketUpdateTileEntity getUpdatePacket() {
        SPacketUpdateTileEntity packet = super.getUpdatePacket();
        NBTTagCompound tag = packet != null ? packet.getNbtCompound() : new NBTTagCompound();

        writeToNBT(tag);

        return new SPacketUpdateTileEntity(pos, 1, tag);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt.getNbtCompound();
        readFromNBT(tag);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {

        return false;
    }


}
