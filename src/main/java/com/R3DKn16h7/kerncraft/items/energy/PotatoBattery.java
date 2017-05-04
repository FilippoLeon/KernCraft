package com.R3DKn16h7.kerncraft.items.energy;

import com.R3DKn16h7.kerncraft.items.EnergyContainerItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class PotatoBattery extends EnergyContainerItem {
    public static String base_name = "potato_battery";

    public PotatoBattery() {
        super();

        this.setUnlocalizedName(base_name);
        this.setRegistryName(base_name);
        this.setCreativeTab(CreativeTabs.MISC);
        this.setMaxStackSize(1);
        GameRegistry.register(this);
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
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
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