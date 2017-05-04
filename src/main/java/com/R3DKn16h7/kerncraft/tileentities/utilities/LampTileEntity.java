package com.R3DKn16h7.kerncraft.tileentities.utilities;

import com.R3DKn16h7.kerncraft.tileentities.IMessageIntReceiver;
import com.R3DKn16h7.kerncraft.tileentities.IRedstoneSettable;
import com.R3DKn16h7.kerncraft.tileentities.KernCraftTileEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class LampTileEntity extends TileEntity implements IRedstoneSettable, IMessageIntReceiver {

    public int redstoneMode = 0;
    public int lightLevel = 0;

    public LampTileEntity() {

    }

    public int getRedstoneMode() {
        return redstoneMode;
    }

    public void setRedstoneMode(int i) {
        redstoneMode = i;
        updateState();
    }

    @Override
    public BlockPos getPos() {
        return super.getPos();
    }

    public void receiveMessage(int i) {
//        System.out.println("Received ll:" + lightLevel);
        if (i == -1 && lightLevel < 15) ++lightLevel;
        else if (i == -2 && lightLevel > 0) --lightLevel;
        else if(i >= 0 && i <= 15 && lightLevel != i) lightLevel = i;
        else return;
        world.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
        this.markDirty();
        updateState();
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt != null && nbt.hasKey("lightLevel")) {
            lightLevel = nbt.getInteger("lightLevel");
            redstoneMode = nbt.getInteger("redstoneMode");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        nbt.setInteger("lightLevel", lightLevel);
        nbt.setInteger("redstoneMode", redstoneMode);
//        System.out.println("Load nbt");
        return nbt;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos,
                                 IBlockState oldState, IBlockState newSate) {
        return false;
    }

    public void updateState() {
        if (redstoneMode == 0 && !this.world.isBlockPowered(pos)) {
            world.setBlockState(this.pos, KernCraftTileEntities.LAMP.getDefaultState()
                    .withProperty(LampBlockEntity.POWERED, 0));
        } else if (redstoneMode == 1 && this.world.isBlockPowered(pos)) {
            world.setBlockState(this.pos, KernCraftTileEntities.LAMP.getDefaultState()
                    .withProperty(LampBlockEntity.POWERED, 0));
        } else {
            world.setBlockState(this.pos, KernCraftTileEntities.LAMP
                    .getDefaultState().withProperty(LampBlockEntity.POWERED, lightLevel));
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
}