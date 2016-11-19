package com.R3DKn16h7.quantumbase;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = QuantumBase.MODID, version = QuantumBase.VERSION)
public class QuantumBase
{
    public static final String MODID = "quantumbase";
    public static final String VERSION = "0.0.1";
    
    @SidedProxy(clientSide="com.R3DKn16h7.quantumbase.ClientProxy", 
    		serverSide="com.R3DKn16h7.quantumbase.ServerProxy")
    public static CommonProxy proxy;

	@Mod.Instance(QuantumBase.MODID) public static QuantumBase instance;

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
