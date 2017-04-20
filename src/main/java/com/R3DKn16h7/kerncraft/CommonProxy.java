package com.R3DKn16h7.kerncraft;

import com.R3DKn16h7.kerncraft.achievements.AchievementHandler;
import com.R3DKn16h7.kerncraft.blocks.ModBlocks;
import com.R3DKn16h7.kerncraft.crafting.ModCrafting;
import com.R3DKn16h7.kerncraft.elements.ElementBase;
import com.R3DKn16h7.kerncraft.events.EventHandlerCommon;
import com.R3DKn16h7.kerncraft.capabilities.ITyrociniumProgressCapability;
import com.R3DKn16h7.kerncraft.capabilities.TyrociniumProgressFactory;
import com.R3DKn16h7.kerncraft.capabilities.TyrociniumProgressStorage;
import com.R3DKn16h7.kerncraft.items.ModItems;
import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.ModGuiHandler;
import com.R3DKn16h7.kerncraft.tileentities.ModTileEntities;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    @Mod.Instance(KernCraft.MODID)
    public static KernCraft instance;

    public void preInit(FMLPreInitializationEvent e) {

        new ElementBase("");

        ModItems.createItems();
        ModBlocks.createBlocks();
        ModTileEntities.createEntities();

        new KernCraftNetwork();

        CapabilityManager.INSTANCE.register(ITyrociniumProgressCapability.class, new TyrociniumProgressStorage(), new TyrociniumProgressFactory());
    }

    public void init(FMLInitializationEvent e) {

        ModCrafting.initCrafting();


        EventHandlerCommon handler = new EventHandlerCommon();
        MinecraftForge.EVENT_BUS.register(handler);

        new AchievementHandler();
    }

    public void postInit(FMLPostInitializationEvent e) {

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new ModGuiHandler());
    }
}
