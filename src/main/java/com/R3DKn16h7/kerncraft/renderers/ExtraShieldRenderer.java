package com.R3DKn16h7.kerncraft.renderers;

import com.R3DKn16h7.kerncraft.items.ModItems;
import com.R3DKn16h7.kerncraft.models.ExtraShieldModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ExtraShieldRenderer {
    public static ExtraShieldRenderer instance = new ExtraShieldRenderer();
    private final ExtraShieldModel modelShield = new ExtraShieldModel();

    public void renderByItem(ItemStack itemStackIn) {
        if (itemStackIn.getItem() == ModItems.EXTRA_SHIELD) {
            if (itemStackIn.getSubCompound("BlockEntityTag", false) != null) {
//                this.banner.setItemValues(itemStackIn);
//                Minecraft.getMinecraft().getTextureManager().bindTexture(BannerTextures.SHIELD_DESIGNS.getResourceLocation(this.banner.getPatternResourceLocation(), this.banner.getPatternList(), this.banner.getColorList()));
            } else {
                Minecraft.getMinecraft().getTextureManager().bindTexture(BannerTextures.SHIELD_BASE_TEXTURE);
            }

            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            this.modelShield.render();
            GlStateManager.popMatrix();
        }
    }
}