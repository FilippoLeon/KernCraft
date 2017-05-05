package com.R3DKn16h7.kerncraft.items.energy;

import com.R3DKn16h7.kerncraft.items.EnergyContainerItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class PotatoBattery extends EnergyContainerItem {

    public PotatoBattery() {
        super();
    }

    @Override
    public String getName() {
        return "potato_battery";
    }

    @Override
    protected int getMaxInput() {
        return 100;
    }

    @Override
    protected int getMaxOutput() {
        return 100;
    }

    @Override
    protected int getCapacity() {
        return 1000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {

            IEnergyStorage cap =
                    playerIn.getHeldItem(handIn).getCapability(CapabilityEnergy.ENERGY, null);

            cap.receiveEnergy(100, false);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}