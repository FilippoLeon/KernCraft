package com.R3DKn16h7.kerncraft.utils.config;

import com.R3DKn16h7.kerncraft.CommonProxy;
import com.R3DKn16h7.kerncraft.KernCraft;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

/**
 * Modified From: http://wiki.mcjty.eu/modding/index.php/Configuration-1.9
 */
public class KernCraftConfig {

    static final String CATEGORY_GENERAL = "general";
    static final String CATEGORY_ELEMENTS = "elements";

    public static boolean DISPLAY_ALL_ELEMENTS = true;
    public static boolean ADD_FULL_SUBITEMS = true;

    public static void readConfig() {
        Configuration cfg = CommonProxy.CONFIG;
        try {
            cfg.load();
            initGeneralConfig(cfg);
            initDimensionConfig(cfg);
        } catch (Exception e1) {
            KernCraft.LOGGER.log(Level.ERROR,
                    "Problem loading configuration file!", e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
    }

    private static void initDimensionConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_ELEMENTS, "Elements configuration");
        DISPLAY_ALL_ELEMENTS = cfg.getBoolean("displayAllElements",
                CATEGORY_GENERAL, DISPLAY_ALL_ELEMENTS,
                "Add all elements to creative tab/JEI."
        );
        ADD_FULL_SUBITEMS = cfg.getBoolean("addFullSubitems",
                CATEGORY_GENERAL, ADD_FULL_SUBITEMS,
                "Add all subitems to the creative tab."
        );

    }
}
