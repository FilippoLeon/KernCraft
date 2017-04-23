package com.R3DKn16h7.kerncraft.guicontainer;

import com.R3DKn16h7.kerncraft.tileentities.ChemicalFurnaceTileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.IItemHandler;


public class ChemicalFurnaceContainer extends SmeltingContainer {

    /***
     *
     * @param playerInv
     * @param te
     * ###########
     * # I L    R#
     * #         #
     * # F   OOOO#
     * ___________
     * #PPPPPPPPP#
     * #PPPPPPPPP#
     * #PPPPPPPPP#
     * -----------
     * #HHHHHHHHH#
     * ###########
     */
    public ChemicalFurnaceContainer(IInventory playerInv,
                                    ChemicalFurnaceTileEntity te) {
        super(playerInv, te);

        IItemHandler input = te.getInput();
        IItemHandler output = te.getOutput();

        int numInputs = 2;
        for (int i = 0; i < numInputs; ++i) {
            this.addSlotToContainer(new AdvancedSlotItemHandler(this,
                    input, i,
                    bdLeft + (4 + i) * xSlotSize,
                    bdTop + 0 * ySlotSize, 64));
        }
        int numOutputs = 2;
        for (int i = 0; i < numOutputs; ++i) {
            this.addSlotToContainer(new AdvancedSlotItemHandler(this,
                    output, i,
                    bdLeft + (4 + i) * xSlotSize,
                    bdTop + 2 * ySlotSize, 64));
        }

        // Player Inventory Slot 9-35, ID 9-35
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9,
                        bdLeft + x * xSlotSize, 84 + y * xSlotSize));
            }
        }

        // Player Hotbar Slot 0-8, ID 36-44
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(playerInv, x,
                    bdLeft + x * xSlotSize, 142));
        }
    }

}
