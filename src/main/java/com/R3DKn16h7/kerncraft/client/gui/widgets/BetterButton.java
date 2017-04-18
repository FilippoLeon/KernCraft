package com.R3DKn16h7.kerncraft.client.gui.widgets;

import com.R3DKn16h7.kerncraft.client.gui.IAdvancedGuiContainer;
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

    protected ResourceLocation ICON_TEXTURES;
    int green = 255;
    int red = 255;
    int blue = 255;
    String texture = "kerncraft:textures/gui/widgets.png";
    protected ResourceLocation BUTTON_TEXTURES = new ResourceLocation(texture);
    boolean use_texture = true;
    Widget.Alignment alignment = Widget.Alignment.LEFT;
    private int yRelPosition;
    private int xRelPosition;
    private IAdvancedGuiContainer container;
    private int xIcon;
    private int yIcon;
    private int xIconSize;
    private int yIconSize;
    private int xStart = 0;
    private int yStart = 46;
    private int xEnd = 200;
    private int yEnd = 66;
    private int xMargin = 0;
    private int yMargin = 0;
    private Color textColor = new Color(16777215);
    private Color textHoverColor = Color.CYAN;
    private boolean has_icon = false;
    private String tooltip;
    private Runnable runnable;

    public BetterButton(IAdvancedGuiContainer container, int id, int x, int y,
                        int widthIn, int heightIn) {
        super(id, x, y, widthIn, heightIn, "");
        this.container = container;
        this.xRelPosition = x;
        this.yRelPosition = y;
    }

    public BetterButton(IAdvancedGuiContainer container, int x, int y,
                        int widthIn, int heightIn) {
        super(container.nextId(), x, y, widthIn, heightIn, "");
        this.container = container;
        this.xRelPosition = x;
        this.yRelPosition = y;
    }

    public BetterButton(IAdvancedGuiContainer container, int x, int y) {
        this(container, x, y, 200, 20);
    }

    public BetterButton setIcon(String texture, int xIcon, int yIcon, int xIconSize, int yIconSize) {
        this.xIcon = xIcon;
        this.yIcon = yIcon;
        this.xIconSize = xIconSize;
        this.yIconSize = yIconSize;
        has_icon = true;
        ICON_TEXTURES = new ResourceLocation(texture);

        return this;
    }

    public BetterButton setSize(int xSize, int ySize) {

        return this;
    }

    public BetterButton setText(String text) {
        this.displayString = text;
        return this;
    }

    public BetterButton setTooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public BetterButton setBackground(String texture, int xStart, int yStart, int xEnd, int yEnd) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
        this.texture = texture;
        use_texture = true;
        BUTTON_TEXTURES = new ResourceLocation(texture);
        return this;
    }

    public BetterButton setAlignment(Widget.Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public BetterButton setMargin(int xMargin, int yMargin) {

        this.xMargin = xMargin;
        this.yMargin = yMargin;
        return this;
    }

    public BetterButton setTint(Color tint) {

        this.red = tint.getRed();
        this.green = tint.getGreen();
        this.blue = tint.getBlue();
        return this;
    }

    public BetterButton setTextColor(Color textColor, boolean hover) {
        if (hover) {
            this.textHoverColor = textColor;
        } else {

            this.textColor = textColor;
        }
        return this;
    }

    public BetterButton setOnClicked(Runnable r) {
        runnable = r;
        return this;
    }

    public BetterButton setTransparent() {
        use_texture = false;

        return this;
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {

        int xAbsPosition = this.xPosition;
        int yAbsPosition = this.yPosition;

        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            if (use_texture) {
                mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            }
            this.hovered = mouseX >= xAbsPosition &&
                    mouseY >= yAbsPosition &&
                    mouseX < xAbsPosition + this.width &&
                    mouseY < yAbsPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(red / 255.f, green / 255.f, blue / 255.f, 1.0F);
            if (use_texture) {
                this.drawTexturedModalRect(xAbsPosition, yAbsPosition,
                        xStart, yStart + i * 20,
                        this.width / 2, this.height / 2);
                this.drawTexturedModalRect(xAbsPosition, yAbsPosition + this.height / 2,
                        xStart, yEnd + i * 20 - this.height / 2,
                        this.width / 2, this.height / 2);
                this.drawTexturedModalRect(xAbsPosition + this.width / 2, yAbsPosition,
                        xEnd - this.width / 2, yStart + i * 20,
                        this.width / 2, this.height / 2);
                this.drawTexturedModalRect(xAbsPosition + this.width / 2, yAbsPosition + this.height / 2,
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
            int offset = 0;
            if (has_icon) {
                offset += 16;
            }
            switch (alignment) {
                case LEFT:
                    fontrenderer.drawString(this.displayString,
                            xAbsPosition + this.xMargin + offset, yAbsPosition + (this.height - 8) / 2, j);
                    break;
                case MIDDLE:
                    int w = fontrenderer.getStringWidth(this.displayString);
                    fontrenderer.drawString(this.displayString,
                            xAbsPosition + (this.width - w) / 2, yAbsPosition + (this.height - 8) / 2, j, true);
                    break;
                case RIGHT:
                    break;
            }

            if (has_icon) {
                mc.getTextureManager().bindTexture(ICON_TEXTURES);
                if (this.displayString.equals("") && Text.Alignment.MIDDLE == alignment) {
                    this.drawTexturedModalRect(xAbsPosition + (this.width - 16) / 2, yAbsPosition + (this.height - 16) / 2,
                            xIcon, yIcon,
                            xIconSize, yIconSize);
                } else {
                    this.drawTexturedModalRect(xAbsPosition + this.xMargin, yAbsPosition + (this.height - 16) / 2,
                            xIcon, yIcon,
                            xIconSize, yIconSize);
                }
            }
            GlStateManager.resetColor();
        }
    }

    @Override
    public int getPositionX() {
        return xPosition;
    }

    @Override
    public int getPositionY() {
        return yPosition;
    }

    @Override
    public void init() {
        container.add(this);

        this.xPosition = this.xRelPosition + container.getGuiLeft();
        this.yPosition = this.yRelPosition + container.getGuiTop();
    }

    @Override
    public void draw() {
    }

    @Override
    public java.util.List<String> addHoveringText(java.util.List<String> hoveringText) {
        if (tooltip != null) hoveringText.add(tooltip);
        return hoveringText;
    }

    @Override
    public void onHover(int mouseX, int mouseY) {

    }

    @Override
    public void onClicked(int mouseX, int mouseY, int mouseButton) {
        if (runnable != null) runnable.run();
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {

    }

    @Override
    public boolean isMouseInArea(int mouseX, int mouseY) {

        return (mouseX >= xPosition && mouseX <= xPosition + getButtonWidth() &&
                mouseY >= yPosition && mouseY <= yPosition + height);
    }

    @Override
    public boolean interceptKeyPress() {
        return false;
    }

    @Override
    public boolean canBecomeActive() {
        return false;
    }
}
