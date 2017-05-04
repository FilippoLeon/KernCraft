package com.R3DKn16h7.kerncraft;

import com.R3DKn16h7.kerncraft.achievements.AchievementHandler;
import com.R3DKn16h7.kerncraft.blocks.KernCraftBlocks;
import com.R3DKn16h7.kerncraft.capabilities.element.ElementContainerDefaultCapability;
import com.R3DKn16h7.kerncraft.capabilities.element.ElementContainerStorage;
import com.R3DKn16h7.kerncraft.capabilities.element.IElementContainer;
import com.R3DKn16h7.kerncraft.capabilities.manual.ITyrociniumProgressCapability;
import com.R3DKn16h7.kerncraft.capabilities.manual.TyrociniumProgressFactory;
import com.R3DKn16h7.kerncraft.capabilities.manual.TyrociniumProgressStorage;
import com.R3DKn16h7.kerncraft.crafting.KernCraftRecipes;
import com.R3DKn16h7.kerncraft.elements.ElementRegistry;
import com.R3DKn16h7.kerncraft.events.EventHandlerCommon;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.ModGuiHandler;
import com.R3DKn16h7.kerncraft.potions.KernCraftPotions;
import com.R3DKn16h7.kerncraft.sounds.KernCraftSounds;
import com.R3DKn16h7.kerncraft.tileentities.KernCraftTileEntities;
import com.R3DKn16h7.kerncraft.utils.config.KernCraftConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.File;

public class CommonProxy {

    @Mod.Instance(KernCraft.MODID)
    public static KernCraft INSTANCE;

    /**
     * Config file for KernCraft
     */
    public static Configuration CONFIG;

    public void preInit(FMLPreInitializationEvent e) {
        // Setup log
        File directory = e.getModConfigurationDirectory();
        CONFIG = new Configuration(new File(directory.getPath(), "kerncraft.cfg"));
        KernCraftConfig.readConfig();

        new ElementRegistry();

        new KernCraftItems();
        KernCraftBlocks.createBlocks();
        KernCraftTileEntities.createEntities();

        new KernCraftNetwork();

        CapabilityManager.INSTANCE.register(ITyrociniumProgressCapability.class,
                new TyrociniumProgressStorage(), new TyrociniumProgressFactory());
        CapabilityManager.INSTANCE.register(IElementContainer.class,
                new ElementContainerStorage(), ElementContainerDefaultCapability.class);

        new KernCraftSounds();
    }

    public void init(FMLInitializationEvent e) {
        // Call this after we created all Objects
        new KernCraftRecipes();

        EventHandlerCommon handler = new EventHandlerCommon();
        MinecraftForge.EVENT_BUS.register(handler);

        new AchievementHandler();

        new KernCraftPotions();
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (CONFIG.hasChanged()) {
            CONFIG.save();
        }

        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new ModGuiHandler());

        KernCraft.FOUND_TESLA = Loader.isModLoaded("tesla");
    }
}
