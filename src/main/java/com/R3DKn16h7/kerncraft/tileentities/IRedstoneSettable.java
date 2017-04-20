package com.R3DKn16h7.kerncraft.tileentities;

import net.minecraft.util.math.BlockPos;

/**
 * Created by Filippo on 11/12/2016.
 */
public interface IRedstoneSettable {
    BlockPos getPos();

    void setRedstoneMode(int i);

    int getRedstoneMode();
}
