package com.R3DKn16h7.kerncraft.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TimeMachineTileEntity extends TileEntity
        implements ITickable {

    public TimeMachineTileEntity() {

    }

    @Override
    public void update() {
        world.setWorldTime(world.getStrongPower(pos) * 1500);
    }
}