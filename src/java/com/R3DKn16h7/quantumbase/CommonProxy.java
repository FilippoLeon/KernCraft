package com.R3DKn16h7.quantumbase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.R3DKn16h7.quantumbase.blocks.ModBlocks;
import com.R3DKn16h7.quantumbase.crafting.ModCrafting;
import com.R3DKn16h7.quantumbase.entities.ModEntities;
import com.R3DKn16h7.quantumbase.items.ModItems;
import com.R3DKn16h7.quantumbase.network.ModGuiHandler;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
	
	@Mod.Instance(QuantumBase.MODID) public static QuantumBase instance;

    public void preInit(FMLPreInitializationEvent e) {
    	ModItems.createItems();
    	ModBlocks.createBlocks();
    	ModEntities.createEntities();
    }

    public void init(FMLInitializationEvent e) {

	    ModCrafting.initCrafting();
	    
	    NetworkRegistry.INSTANCE.registerGuiHandler(instance, new ModGuiHandler());
	    
	    Gson gson = new Gson();
	    ResourceLocation loc = new ResourceLocation("quantumbase:config/elements.json");
	    InputStream in = null;
        try {
            in = Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    JsonElement je = gson.fromJson(reader, JsonElement.class);
	    JsonObject json = je.getAsJsonObject();

	    System.out.print(json.getAsJsonObject("H").get("id").getAsString());
 }  

    public void postInit(FMLPostInitializationEvent e) {

    }
}
