package com.R3DKn16h7.kerncraft.guicontainer;

import com.R3DKn16h7.kerncraft.tileentities.machines.RockAnalyzerTileEntity;
import net.minecraft.inventory.IInventory;

/**
 *
 */
public class RockAnalyzerContainer extends SmeltingContainer {

    /***
     *
     * @param playerInv
     * @param te
     */
    public RockAnalyzerContainer(IInventory playerInv,
                                 RockAnalyzerTileEntity te) {
        super(playerInv, te);

        addMachineSlots();
        addPlayerSlots(playerInv);
    }
}
