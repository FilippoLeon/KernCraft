package com.R3DKn16h7.kerncraft.guicontainer;

import com.R3DKn16h7.kerncraft.tileentities.SmeltingTileEntity;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Filippo on 18-Apr-17.
 */
public class SmeltingContainer extends MachineContainer<SmeltingTileEntity> {
    public static final int FIELDS = 2;
    public static final int FUEL_ID = 0;
    public static final int PROGRESS_ID = 1;
    int[] params = new int[FIELDS];

    public SmeltingContainer(IInventory playerInv, SmeltingTileEntity te) {
        super(playerInv, te);

    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i) {
            IContainerListener icontainerlistener = this.listeners.get(i);

            for (int j = 0; j < FIELDS; ++j) {
                if (this.params[j] != this.te.getField(j)) {
                    icontainerlistener.sendProgressBarUpdate(this, j, this.te.getField(j));
                }
            }
//
//            if (this.furnaceBurnTime != this.tileFurnace.getField(0))
//            {
//                icontainerlistener.sendProgressBarUpdate(this, 0, this.tileFurnace.getField(0));
//            }
//
//            if (this.currentItemBurnTime != this.tileFurnace.getField(1))
//            {
//                icontainerlistener.sendProgressBarUpdate(this, 1, this.tileFurnace.getField(1));
//            }
//
//            if (this.totalCookTime != this.tileFurnace.getField(3))
//            {
//                icontainerlistener.sendProgressBarUpdate(this, 3, this.tileFurnace.getField(3));
//            }
        }

//        this.cookTime = this.tileFurnace.getField(2);
        for (int i = 0; i < FIELDS; ++i) {
            this.params[i] = this.te.getField(i);
        }
//        this.currentItemBurnTime = this.tileFurnace.getField(1);
//        this.totalCookTime = this.tileFurnace.getField(3);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        this.te.setField(id, data);
    }

}
