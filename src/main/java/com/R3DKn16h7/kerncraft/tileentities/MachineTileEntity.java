package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.tileentities.utils.ConfigurableItemHandler;
import com.R3DKn16h7.kerncraft.tileentities.utils.SideConfiguration;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.awt.*;

/**
 * Created by Filippo on 14/12/2016.
 */
public abstract class MachineTileEntity extends TileEntity
        implements ITickable, ISideConfigurable {

    public final ItemStackHandler input;
    public final ItemStackHandler output;
    private final int inputSize;
    private final int outputSize;
    // Has the input changed since last check?
    public boolean inputChanged = false;

    public final SideConfiguration sideConfig;

    public MachineTileEntity(int inputSize, int outputSize) {
        super();

        this.inputSize = inputSize;
        this.outputSize = outputSize;

        input = new ItemStackHandler(inputSize);
        output = new ItemStackHandler(outputSize);

        sideConfig = new SideConfiguration(input, output);
    }

    @Override
    public void setSlotSide(int slotId, int side) {
        sideConfig.setSlotSide(slotId, side);
    }

    public abstract int getTotalSlots();

    public ItemStackHandler getInput() {
        return input;
    }

    public ItemStackHandler getOutput() {
        return output;
    }

    @Override
    public boolean hasCapability(Capability<?> capability,
                                 EnumFacing facing) {
        if (//capability == CapabilityEnergy.ENERGY ||
            //capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ||
                capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability,
                               EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            //return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(automationInput);
            EnumFacing machineFacing = world.getBlockState(getPos()).getValue(MachineBlock.FACING);
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(sideConfig.get(facing, machineFacing));
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        restoreFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        nbt.setTag("Input", input.serializeNBT());
        nbt.setTag("Output", output.serializeNBT());
        nbt.setTag("sideConfiguration", sideConfig.serializeNBT());
        return nbt;
    }

    public void restoreFromNBT(NBTTagCompound nbt) {
        if(nbt != null) {
            if ( nbt.hasKey("Input")) {
                input.deserializeNBT(nbt.getCompoundTag("Input"));
            }
            if ( nbt.hasKey("Output")) {
                output.deserializeNBT(nbt.getCompoundTag("Output"));
            }
            if ( nbt.hasKey("sideConfiguration")) {
                sideConfig.deserializeNBT(nbt.getCompoundTag("sideConfiguration"));
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
}
