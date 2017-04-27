package com.R3DKn16h7.kerncraft.guicontainer;

import com.R3DKn16h7.kerncraft.tileentities.FillerTileEntity;
import net.minecraft.inventory.IInventory;

/**
 *
 */
public class FillerContainer extends SmeltingContainer {

    /***
     *
     * @param playerInv
     * @param te
     */
    public FillerContainer(IInventory playerInv,
                           FillerTileEntity te) {
        super(playerInv, te);

        addMachineSlots();
        addPlayerSlots(playerInv);
    }

}
