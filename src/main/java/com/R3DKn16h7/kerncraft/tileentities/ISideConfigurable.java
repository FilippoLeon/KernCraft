package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.tileentities.utils.SideConfiguration;

/**
 * Created by Filippo on 18-Apr-17.
 */
public interface ISideConfigurable {
    int[][] getInputCoords();

    int[][] getOutputCoords();

    int getInputSize();

    int getOutputSize();

    void setSlotSide(int slotId, int side);

    SideConfiguration getSideConfig();

    void contentChanged();
}
