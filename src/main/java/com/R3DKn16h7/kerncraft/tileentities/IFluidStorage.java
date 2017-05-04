package com.R3DKn16h7.kerncraft.tileentities;

import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Filippo on 18-Apr-17.
 */
public interface IFluidStorage {
    int getNumberOfTanks();

    int getCapacity(int i);

    FluidStack getFluid(int i);

    int getFluidAmount(int i);
}
