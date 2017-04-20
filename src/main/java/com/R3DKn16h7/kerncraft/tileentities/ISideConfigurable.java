package com.R3DKn16h7.kerncraft.tileentities;

/**
 * Created by Filippo on 18-Apr-17.
 */
public interface ISideConfigurable {
    int[][] getInputCoords();

    int[][] getOutputCoords();

    void setSlotSide(int slotId, int side);
}
