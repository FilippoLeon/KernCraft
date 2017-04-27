package com.R3DKn16h7.kerncraft.guicontainer;

import com.R3DKn16h7.kerncraft.tileentities.CentrifugeTileEntity;
import net.minecraft.inventory.IInventory;

/**
 *
 */
public class CentrifugeContainer extends SmeltingContainer {

    /***
     *
     * @param playerInv
     * @param te
     */
    public CentrifugeContainer(IInventory playerInv,
                               CentrifugeTileEntity te) {
        super(playerInv, te);

        addMachineSlots();
        addPlayerSlots(playerInv);
    }
}
