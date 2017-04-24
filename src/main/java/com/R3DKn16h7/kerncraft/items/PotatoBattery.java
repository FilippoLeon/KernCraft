package com.R3DKn16h7.kerncraft.items;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ITeslaConsumer cap =
                playerIn.getHeldItem(handIn).getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null);

        cap.givePower(100, false);

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}