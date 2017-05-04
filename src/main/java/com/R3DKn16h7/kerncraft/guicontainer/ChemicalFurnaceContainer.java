package com.R3DKn16h7.kerncraft.guicontainer;

import com.R3DKn16h7.kerncraft.tileentities.machines.ChemicalFurnaceTileEntity;
import net.minecraft.inventory.IInventory;

/**
 *
 */
public class ChemicalFurnaceContainer extends SmeltingContainer {

    /***
     *
     * @param playerInv
     * @param te
     */
    public ChemicalFurnaceContainer(IInventory playerInv,
                                    ChemicalFurnaceTileEntity te) {
        super(playerInv, te);

        addMachineSlots();
        addPlayerSlots(playerInv);
    }

}
