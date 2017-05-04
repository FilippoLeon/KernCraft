package com.R3DKn16h7.kerncraft.guicontainer;

import com.R3DKn16h7.kerncraft.tileentities.machines.ExtractorTileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandler;

public class ExtractorContainer extends SmeltingContainer {

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
    public ExtractorContainer(IInventory playerInv, ExtractorTileEntity te) {
        super(playerInv, te);

        IItemHandler input = te.getInput();
        IItemHandler output = te.getOutput();

        int id = 0;
        // Input (I) ID 0
        this.addSlotToContainer(new AdvancedSlotItemHandler(this,
                input, id++,
                bdLeft + xSlotSize, bdTop, 64));
        // Catalyst (LAB_BOOTS) ID 1
        this.addSlotToContainer(new AdvancedSlotItemHandler(this,
                input, id++, bdLeft + 3 * xSlotSize, bdTop, 64));
        // Container (R) ID 2
        this.addSlotToContainer(new AdvancedSlotItemHandler(this,
                input, id++, bdLeft + 8 * xSlotSize, bdTop, 64));
        // Fuel slot (F) ID 3
        this.addSlotToContainer(new AdvancedSlotItemHandler(this,
                input, id++,
                bdLeft + xSlotSize, bdTop + 2 * ySlotSize, 64));
        // Output (O) ID 4-7
        for (int i = 0; i < te.getOutputCoords().length; ++i) {
            this.addSlotToContainer(new AdvancedSlotItemHandler(this, output, i,
                    bdLeft + (5 + i) * xSlotSize, bdTop + 2 * ySlotSize));
        }

        addPlayerSlots(playerInv);
    }


}
