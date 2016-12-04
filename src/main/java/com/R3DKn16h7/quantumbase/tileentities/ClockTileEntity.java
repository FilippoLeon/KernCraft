package com.R3DKn16h7.quantumbase.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class ClockTileEntity extends TileEntity
        implements ITickable {

    public ClockTileEntity() {

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

        System.out.print(worldObj.getWorldTime() + " " + (int) worldObj.getWorldTime() * 15 / 22500 + " ---");
    }
}