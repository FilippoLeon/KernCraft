package com.R3DKn16h7.kerncraft.manual;

import com.R3DKn16h7.kerncraft.client.gui.IWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

/**
 * Created by Filippo on 09/12/2016.
 */
@SideOnly(Side.CLIENT)
public class BetterButton extends GuiButton implements IWidget {

    protected ResourceLocation BUTTON_TEXTURES;
    int green = 0;
    int red = 0;
    int blue = 0;
    String texture;
    boolean use_texture = false;
    Alignment alignment = Alignment.LEFT;
    private int xStart;
    private int yStart;
    private int xEnd;
    private int yEnd;
    private int xMargin = 0;
    private int yMargin = 0;
    private Color textColor;

    public BetterButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText,
                        Color color, String texture, int xStart, int yStart, int xEnd, int yEnd,
                        Alignment alignment, int xMargin, int yMargin, Color textColor) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.textColor = textColor;
        if (color != null) {
            this.red = color.getRed();
            this.green = color.getGreen();
            this.blue = color.getBlue();
        }
        this.xMargin = xMargin;
        this.yMargin = yMargin;
        if (texture != null) {
            this.xStart = xStart;
            this.yStart = yStart;
            this.xEnd = xEnd;
            this.yEnd = yEnd;
            this.texture = texture;
            use_texture = true;
            BUTTON_TEXTURES = new ResourceLocation(texture);
        }
        this.alignment = alignment;
    }

    public BetterButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText,
                        Color color, String texture, int xStart, int yStart, int xEnd, int yEnd,
                        Alignment alignment, int xMargin, int yMargin) {
        this(buttonId, x, y, widthIn, heightIn, buttonText,
                color, texture, xStart, yStart, xEnd, yEnd,
                alignment, xMargin, yMargin, Color.black);

    }

    public BetterButton(int buttonId, int x, int y, String buttonText, int red, int green, int blue) {
        this(buttonId, x, y, 200, 20, buttonText, red, green, blue);
    }

    public BetterButton(int buttonId, int x, int y, int widthIn,
                        int heightIn, String buttonText, int red, int green, int blue) {
        this(buttonId, x, y, widthIn, heightIn, buttonText,
                new Color(red, green, blue), "kerncraft:textures/gui/widgets.png",
                0, 46, 200, 66, Alignment.CENTER, 0, 0);
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            if (use_texture) {
                mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            }
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
            if (use_texture) {
                this.drawTexturedModalRect(this.xPosition, this.yPosition,
                        xStart, yStart + i * 20,
                        this.width / 2, this.height / 2);
                this.drawTexturedModalRect(this.xPosition, this.yPosition + this.height / 2,
                        xStart, yEnd + i * 20 - this.height / 2,
                        this.width / 2, this.height / 2);
                this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition,
                        xEnd - this.width / 2, yStart + i * 20,
                        this.width / 2, this.height / 2);
                this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition + this.height / 2,
                        xEnd - this.width / 2, yEnd + i * 20 - this.height / 2,
                        this.width / 2, this.height / 2);
            }
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;
            if (textColor != null) j = textColor.getRGB();

            if (packedFGColour != 0) {
                j = packedFGColour;
            } else if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = 16777120;
            }
            switch (alignment) {
                case LEFT:
                    fontrenderer.drawString(this.displayString,
                            this.xPosition + this.xMargin, this.yPosition + (this.height - 8) / 2, j);
                    break;
                case CENTER:
                    int w = fontrenderer.getStringWidth(this.displayString);
                    fontrenderer.drawString(this.displayString,
                            this.xPosition + (this.width - w) / 2, this.yPosition + (this.height - 8) / 2, j, true);
                    break;
                case RIGHT:
                    break;
            }
        }
    }

    @Override
    public int getPositionX() {
        return 0;
    }

    @Override
    public int getPositionY() {
        return 0;
    }

    @Override
    public void init() {

    }

    @Override
    public void draw() {

    }

    @Override
    public java.util.List<String> addHoveringText(java.util.List<String> hoveringText) {
        return null;
    }

    @Override
    public void onHover(int mouseX, int mouseY) {

    }

    @Override
    public void onClicked(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {

    }

    @Override
    public boolean isMouseInArea(int mouseX, int mouseY) {
        return false;
    }

    @Override
    public boolean interceptKeyPress() {
        return false;
    }

    @Override
    public boolean canBecomeActive() {
        return false;
    }

    enum Alignment {
        LEFT, CENTER, RIGHT
    }

}
