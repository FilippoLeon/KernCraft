package com.R3DKn16h7.kerncraft.guicontainer;

import com.R3DKn16h7.kerncraft.tileentities.ElectrolyzerTileEntity;
import net.minecraft.inventory.IInventory;

/**
 *
 */
public class ElectrolyzerContainer extends SmeltingContainer {

    /***
     *
     * @param playerInv
     * @param te
     */
    public ElectrolyzerContainer(IInventory playerInv,
                                 ElectrolyzerTileEntity te) {
        super(playerInv, te);

        addMachineSlots();
        addPlayerSlots(playerInv);
    }
}
