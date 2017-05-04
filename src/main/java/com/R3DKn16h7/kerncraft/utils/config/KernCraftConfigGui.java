package com.R3DKn16h7.kerncraft.utils.config;

import com.R3DKn16h7.kerncraft.CommonProxy;
import com.R3DKn16h7.kerncraft.KernCraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filippo on 01-May-17.
 */
public class KernCraftConfigGui extends GuiConfig {
    public KernCraftConfigGui(GuiScreen parent) {
        super(parent, getConfigElements(), KernCraft.MODID,
                false, false,
                "KernCraft configuration");
    }

    /**
     * Compiles a list of configuration elements
     **/
    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        //Add categories to config GUI
        list.add(categoryElement(KernCraftConfig.CATEGORY_GENERAL,
                "General", "kerncraft.config.category.general"));
        list.add(categoryElement(KernCraftConfig.CATEGORY_ELEMENTS,
                "Elements", "kerncraft.config.category.elements"));

        return list;
    }

    /**
     * Creates a button linking to another screen where all options of the category are available
     */
    private static IConfigElement categoryElement(String category, String name, String tooltip_key) {
        return new DummyConfigElement.DummyCategoryElement(name, tooltip_key,
                new ConfigElement(CommonProxy.CONFIG.getCategory(category)).getChildElements());
    }
}
