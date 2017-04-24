package com.R3DKn16h7.kerncraft;

import com.R3DKn16h7.kerncraft.elements.ElementBase;
import com.R3DKn16h7.kerncraft.items.Canister;
import com.R3DKn16h7.kerncraft.items.EnergyContainer;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = KernCraft.MODID, name = KernCraft.NAME,
     version = KernCraft.VERSION, dependencies= "after:tesla")
public class KernCraft {
    public static final String MODID = "kerncraft";
    public static final String NAME = "kerncraft";
    public static final String VERSION = "1.0";

    public static boolean MOD_CONFIG_DISPLAY_ALL_ELEMENTS = true;
    public static boolean MOD_CONFIG_ADD_FULL_SUBITEMS = true;

    public static final CreativeTabs KERNCRAFT_CREATIVE_TAB = new CreativeTabs("KernCraft") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(KernCraftItems.TYROCINIUM_CHYMICUM);
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> p_78018_1_) {
            super.displayAllRelevantItems(p_78018_1_);

            if(MOD_CONFIG_DISPLAY_ALL_ELEMENTS) {
                for (int i = 1; i <= ElementBase.NUMBER_OF_ELEMENTS; ++i) {
                    p_78018_1_.add(Canister.getElementItemStack(i, -1));
                }
            }
            if (MOD_CONFIG_ADD_FULL_SUBITEMS) {
                ItemStack potato_battery = new ItemStack(KernCraftItems.POTATO_BATTERY, 1);
                // TODO: this must be fixed
                if (potato_battery.hasCapability(CapabilityEnergy.ENERGY, null)) {
                    IEnergyStorage cap = potato_battery.getCapability(CapabilityEnergy.ENERGY, null);
                    if (cap instanceof EnergyContainer) {
                        ((EnergyContainer) cap).fill();
                    }
                    p_78018_1_.add(potato_battery);
                }
            }
        }
    };

    @SidedProxy(clientSide = "com.R3DKn16h7.kerncraft.ClientProxy",
            serverSide = "com.R3DKn16h7.kerncraft.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance(KernCraft.MODID)
    public static KernCraft instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

}
