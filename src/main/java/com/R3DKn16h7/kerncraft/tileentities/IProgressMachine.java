package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.client.gui.MachineGuiContainer;
import net.minecraft.util.Tuple;

/**
 * Created by Filippo on 18-Apr-17.
 */
public interface IProgressMachine {
    float getProgressPercent();

    Tuple<Integer[], MachineGuiContainer.ProgressIcon> getProgressIconCoordinate();

    int[] getProgressTextCoordinate();
}
