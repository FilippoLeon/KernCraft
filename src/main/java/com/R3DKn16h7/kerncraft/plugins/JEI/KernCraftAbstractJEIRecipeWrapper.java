package com.R3DKn16h7.kerncraft.plugins.JEI;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;

/**
 * Created by Filippo on 07-May-17.
 */
public abstract class KernCraftAbstractJEIRecipeWrapper extends BlankRecipeWrapper {
    protected boolean isInBox(int mouseX, int mouseY,
                              int startX, int startY,
                              int sizeX, int sizeY) {
        return mouseX > startX && mouseX < startX + sizeX
                && mouseY > startY && mouseY < startY + sizeY;
    }

    @Override
    public boolean handleClick(Minecraft minecraft,
                               int mouseX, int mouseY,
                               int mouseButton) {
        return false;
    }
}
