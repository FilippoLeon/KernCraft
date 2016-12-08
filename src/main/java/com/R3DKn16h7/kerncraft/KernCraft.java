package com.R3DKn16h7.kerncraft;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = KernCraft.MODID, version = KernCraft.VERSION)
public class KernCraft {
    public static final String MODID = "kerncraft";
    public static final String VERSION = "0.0.1";

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
