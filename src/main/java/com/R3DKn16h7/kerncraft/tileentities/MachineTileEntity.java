package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.tileentities.utils.SideConfiguration;
import com.R3DKn16h7.kerncraft.tileentities.utils.Upgrade;
import com.R3DKn16h7.kerncraft.tileentities.utils.UpgradeHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * Created by Filippo on 14/12/2016.
 */
public abstract class MachineTileEntity extends TileEntity
        implements ITickable, ISideConfigurable, IUpgradeable {

    // Capability slot container
    public final ItemStackHandler input;
    public final ItemStackHandler output;
    // Capability side configuration
    public final SideConfiguration sideConfig;

    // Has the input changed since last check?
    public boolean inputChanged = false;
    /**
     * Generic manager for machine upgrades.
     */
    private UpgradeHandler upgradeHandler = new UpgradeHandler();

    public MachineTileEntity() {
        super();

        input = new ItemStackHandler(getInputSize());
        output = new ItemStackHandler(getOutputSize());

        sideConfig = new SideConfiguration(input, output, this);
    }

    @Override
    public UpgradeHandler getUpgrade() {
        return upgradeHandler;
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return new
                TextComponentString(I18n.format(
                this.blockType.getUnlocalizedName() + ".name")
        );
    }

    @Override
    public void setSlotSide(int slotId, int side) {
        sideConfig.setSlotSide(slotId, side);
    }

    public abstract void stop();

    public int getTotalSlots() {

        return getInput().getSlots() + getOutput().getSlots();
    }

    public ItemStackHandler getInput() {
        return input;
    }

    public ItemStackHandler getOutput() {
        return output;
    }

    @Override
    public void contentChanged() {
        inputChanged = true;
    }

    @Override
    public boolean hasCapability(Capability<?> capability,
                                 EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability,
                               EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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
    public boolean AddUpgrade(Upgrade upgrade) {
        if (maxUpgradeCount(upgrade) > getUpgrade().getCount(upgrade)) {
            getUpgrade().add(upgrade);
            resizeSlots();
            return true;
        }
        return false;
    }

    private int maxUpgradeCount(Upgrade upgrade) {
        switch (upgrade) {
            case SPEED:
                return 3;
            case EXTRA_SLOT:
                return 3;
            case ENERGY_EFFICIENTY:
                return 3;
            case PRODUCTION:
                return 3;
            default:
                return 0;
        }
    }

    private void resizeSlots() {
        // TODO: pop off items if not enough space
        input.setSize(getInputSize());
        output.setSize(getOutputSize());
        sideConfig.recompute(input, output, this);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        nbt.setTag("Input", input.serializeNBT());
        nbt.setTag("Output", output.serializeNBT());
        nbt.setTag("sideConfiguration", sideConfig.serializeNBT());
        nbt.setTag("Upgrades", getUpgrade().serializeNBT());
        return nbt;
    }

    public void restoreFromNBT(NBTTagCompound nbt) {
        if (nbt != null) {
            if (nbt.hasKey("Input")) {
                input.deserializeNBT(nbt.getCompoundTag("Input"));
            }
            if (nbt.hasKey("Output")) {
                output.deserializeNBT(nbt.getCompoundTag("Output"));
            }
            if (nbt.hasKey("sideConfiguration")) {
                sideConfig.deserializeNBT(nbt.getCompoundTag("sideConfiguration"));
            }
            if (nbt.hasKey("Upgrades")) {
                getUpgrade().deserializeNBT(nbt.getCompoundTag("Upgrades"));
            }
        }
    }

    @Nullable
    @Override
//    @SideOnly(Side.CLIENT)
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
}
