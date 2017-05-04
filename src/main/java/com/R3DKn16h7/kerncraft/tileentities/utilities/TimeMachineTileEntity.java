package com.R3DKn16h7.kerncraft.tileentities.utilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;

public class TimeMachineTileEntity extends TileEntity
        implements ITickable {

    public TimeMachineTileEntity() {

    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {

        return new TextComponentString("Time Machine");
    }

    @Override
    public void update() {
        NBTTagCompound nbt = this.getTileData();
        if (nbt.hasKey("active") && nbt.getBoolean("active")) {
            world.setWorldTime(world.getStrongPower(pos) * 1500);
        }
    }
}