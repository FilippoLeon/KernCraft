package com.R3DKn16h7.kerncraft;

import com.R3DKn16h7.kerncraft.achievements.AchievementHandler;
import com.R3DKn16h7.kerncraft.blocks.KernCraftBlocks;
import com.R3DKn16h7.kerncraft.capabilities.*;
import com.R3DKn16h7.kerncraft.crafting.KernCraftRecipes;
import com.R3DKn16h7.kerncraft.elements.ElementRegistry;
import com.R3DKn16h7.kerncraft.events.EventHandlerCommon;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.ModGuiHandler;
import com.R3DKn16h7.kerncraft.potions.KernCraftPotions;
import com.R3DKn16h7.kerncraft.sounds.KernCraftSounds;
import com.R3DKn16h7.kerncraft.tileentities.KernCraftTileEntities;
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

        new ElementRegistry();

        new KernCraftItems();
        KernCraftBlocks.createBlocks();
        KernCraftTileEntities.createEntities();
        // Call this after we created all Objects
        new KernCraftRecipes();

        new KernCraftNetwork();

        CapabilityManager.INSTANCE.register(ITyrociniumProgressCapability.class,
                new TyrociniumProgressStorage(), new TyrociniumProgressFactory());
        CapabilityManager.INSTANCE.register(IElementContainer.class,
                new ElementContainerStorage(), ElementContainerDefaultCapability.class);

        new KernCraftSounds();
    }

    public void init(FMLInitializationEvent e) {

        EventHandlerCommon handler = new EventHandlerCommon();
        MinecraftForge.EVENT_BUS.register(handler);

        new AchievementHandler();

        new KernCraftPotions();
    }

    public void postInit(FMLPostInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new ModGuiHandler());
    }
}
