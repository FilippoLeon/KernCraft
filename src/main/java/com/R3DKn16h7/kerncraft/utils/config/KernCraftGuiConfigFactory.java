package com.R3DKn16h7.kerncraft.utils.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

/**
 * Created by Filippo on 01-May-17.
 */
public class KernCraftGuiConfigFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

//    @Override
//    public Class<? extends GuiScreen> mainConfigGuiClass() {
//        return KernCraftConfigGui.class;
//    }

    @Override
    public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new KernCraftConfigGui(parentScreen);
    }

//    @Override
//    public IModGuiFactory.RuntimeOptionGuiHandler getHandlerFor(IModGuiFactory.RuntimeOptionCategoryElement element) {
//        return null;
//    }
}
