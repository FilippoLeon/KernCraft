package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.client.gui.widgets.IWidget;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filippo on 27/11/2016.
 */
public class AdvancedGui extends GuiScreen implements IAdvancedGuiContainer {
    public IWidget activeWidget;

    // Margin of gui backgound
    public int borderLeft = 9;
    public int borderTop = 18;

    public int id = 0;
    public boolean use_dynamic_background = false;
    protected IInventory playerInv;
    protected TileEntity te;
    private ArrayList<IWidget> widgets = new ArrayList<IWidget>();
    // Background offset within texture
    private int offsetX = 0;
    private int offsetY = 0;
    private ResourceLocation backgroundResource;
    private int btn_id = 0;
    private int guiLeft, guiTop;
    private int xSize, ySize;

    private int xBackgroundSize = 250;
    private int yBackgroundSize = 176;

    public AdvancedGui(IInventory playerInv,
                       TileEntity te) {
        this.playerInv = playerInv;
        this.te = te;
    }

    public int getMiddle() {
        return this.xSize / 2;
    }

    public int getBorderLeft() {
        return borderLeft;
    }

    public int getBorderTop() {
        return borderTop;
    }

    public void setActiveWidget(IWidget widget) {

    }

    public int getGuiLeft() {
        return guiLeft;
    }

    public int getGuiTop() {
        return guiTop;
    }

    public void add(GuiButton btn) {
        addButton(btn);
    }

    @Override
    public void initGui() {
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
        super.initGui();

        for (IWidget widget : widgets) {
            widget.init();
        }
    }

    public FontRenderer getFontRenderer() {

        return fontRendererObj;
    }

    public void AddWidget(IWidget widget) {
        widgets.add(widget);
    }

    public void updateWidgets() {
    }

    public int nextId() {
        return btn_id++;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        drawBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);
        updateWidgets();

        GlStateManager.pushMatrix();
        GlStateManager.translate(guiLeft, guiTop, 0);

        List<String> hoveringText = new ArrayList<String>();
        for (IWidget widget : widgets) {
            widget.draw();

            if (widget.isMouseInArea(mouseX, mouseY)) {
                widget.addHoveringText(hoveringText);
            }
        }
        GlStateManager.popMatrix();
        if (!hoveringText.isEmpty()) {
            drawHoveringText(hoveringText, mouseX, mouseY, fontRendererObj);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (IWidget widget : widgets) {
            if (widget.isMouseInArea(mouseX, mouseY)) {
                widget.onClicked(mouseX - widget.getPositionX(),
                        mouseY - widget.getPositionY(), mouseButton);
                return;
            }
        }
    }

    public boolean hasActiveWidget() {
        return activeWidget != null;
    }

    void setDynamicBackground(int xSize, int ySize) {

        use_dynamic_background = true;
        backgroundResource = new ResourceLocation("textures/gui/demo_background.png");
        this.xSize = xSize;
        this.ySize = ySize;
    }


    void setBackground(String location,
                       int offsetX, int offsetY,
                       int xSize, int ySize) {
        use_dynamic_background = false;
        backgroundResource = new ResourceLocation(location);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.xSize = xSize;
        this.ySize = ySize;
        this.xBackgroundSize = xSize;
        this.yBackgroundSize = ySize;
    }

//    @Override
//    public void onResize(Minecraft mcIn, int w, int h) {
//        super.onResize(mcIn, w, h);
//
//        initGui();
//    }
    void drawBackground() {
        this.mc.getTextureManager().bindTexture(backgroundResource);
        if (use_dynamic_background) {
            // Top-left corner
            this.drawTexturedModalRect(this.guiLeft, this.guiTop,
                    offsetX, offsetY,
                    this.xSize / 2, this.ySize / 2);
            // Bottom-left
            this.drawTexturedModalRect(this.guiLeft, this.guiTop + this.ySize / 2,
                    offsetX, offsetY + this.yBackgroundSize - this.ySize / 2,
                    this.xSize / 2, this.ySize / 2);
            // Top-right
            this.drawTexturedModalRect(this.guiLeft + this.xSize / 2, this.guiTop,
                    offsetX + this.xBackgroundSize - this.xSize / 2, offsetY,
                    this.xSize / 2, this.ySize / 2);
            // Bottom-right
            this.drawTexturedModalRect(this.guiLeft + this.xSize / 2, this.guiTop + this.ySize / 2,
                    offsetX + this.xBackgroundSize - this.xSize / 2, offsetY + this.yBackgroundSize - this.ySize / 2,
                    this.xSize / 2, this.ySize / 2);
        } else {
            this.drawTexturedModalRect(this.guiLeft, this.guiTop,
                    offsetX, offsetY,
                    this.xSize, this.ySize);
        }
    }
}
