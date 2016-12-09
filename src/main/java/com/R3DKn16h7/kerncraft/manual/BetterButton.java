package com.R3DKn16h7.kerncraft.manual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Filippo on 09/12/2016.
 */
@SideOnly(Side.CLIENT)
public class BetterButton extends GuiButton {

    protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("kerncraft:textures/gui/widgets.png");

    int red;
    int blue;
    int green;

    public BetterButton(int buttonId, int x, int y, String buttonText, int red, int green, int blue) {
        this(buttonId, x, y, 200, 20, buttonText, red, green, blue);
    }

    public BetterButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, int red, int green, int blue) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            this.hovered = mouseX >= this.xPosition &&
                    mouseY >= this.yPosition &&
                    mouseX < this.xPosition + this.width &&
                    mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(red / 255.f, green / 255.f, blue / 255.f, 1.0F);
            this.drawTexturedModalRect(this.xPosition, this.yPosition,
                    0, 46 + i * 20,
                    this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.xPosition, this.yPosition + this.height / 2,
                    0, 66 + i * 20 - this.height / 2,
                    this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition,
                    200 - this.width / 2, 46 + i * 20,
                    this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition + this.height / 2,
                    200 - this.width / 2, 66 + i * 20 - this.height / 2,
                    this.width / 2, this.height / 2);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (packedFGColour != 0) {
                j = packedFGColour;
            } else if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
        }
    }

}
