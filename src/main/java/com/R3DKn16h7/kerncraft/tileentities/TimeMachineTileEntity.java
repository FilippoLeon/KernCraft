package com.R3DKn16h7.kerncraft.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TimeMachineTileEntity extends TileEntity
        implements ITickable {

    public TimeMachineTileEntity() {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        return nbt;
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

    }

    @Override
    public void update() {
        ClockBlockEntity _block = (ClockBlockEntity)
                this.getWorld().getBlockState(this.getPos()).getBlock();

        this.worldObj.setBlockState(pos,
                _block.getDefaultState().withProperty(ClockBlockEntity.TIME,
                        Math.min((int) worldObj.getWorldTime() * 15 / 22500, 15)));

        //System.out.print(worldObj.getWorldTime() + " " + (int) worldObj.getWorldTime() * 15 / 22500 + " ---");
    }
}