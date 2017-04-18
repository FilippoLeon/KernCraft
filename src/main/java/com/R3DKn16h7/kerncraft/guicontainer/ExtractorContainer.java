package com.R3DKn16h7.kerncraft.guicontainer;

import com.R3DKn16h7.kerncraft.tileentities.ExtractorTileEntity;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.IItemHandler;

public class ExtractorContainer extends MachineContainer {

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

        int id = -1;
        // Input (I) ID 0
        this.addSlotToContainer(new AdvancedSlotItemHandler(this,
                input, ++id,
                bdLeft + xSlotSize, bdTop, 64));
        // Catalyst (LAB_BOOTS) ID 1
        this.addSlotToContainer(new AdvancedSlotItemHandler(this,
                input, ++id, bdLeft + 3 * xSlotSize, bdTop, 64));
        // Container (R) ID 2
        this.addSlotToContainer(new AdvancedSlotItemHandler(this,
                input, ++id, bdLeft + 8 * xSlotSize, bdTop, 64));
        // Fuel slot (F) ID 3
        this.addSlotToContainer(new AdvancedSlotItemHandler(this,
                input, ++id,
                bdLeft + xSlotSize, bdTop + 2 * ySlotSize, 64));
        // Output (O) ID 4-7
        int numOutputs = 4;
        for (int i = 0; i < numOutputs; ++i) {
            this.addSlotToContainer(new AdvancedSlotItemHandler(this, output, i,
                    bdLeft + (5 + i) * xSlotSize, bdTop + 2 * ySlotSize));
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
