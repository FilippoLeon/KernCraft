package com.R3DKn16h7.kerncraft.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Filippo on 27/11/2016.
 */
public class AdvancedGuiContainer extends GuiContainer implements IAdvancedGuiContainer {

    public IWidget activeWidget;

    // Margin of gui backgound

    public int borderLeft = 9;
    public int borderTop = 18;
    public int id = 0;
    protected IInventory playerInv;
    protected TileEntity te;
    private ArrayList<IWidget> background_widgets = new ArrayList<IWidget>();
    private ArrayList<IWidget> foreground_widgets = new ArrayList<IWidget>();
    // Background offset within texture
    private int offsetX = 0;
    private int offsetY = 0;
    private ResourceLocation backgroundResource;
    private int btn_id = 0;

    public AdvancedGuiContainer(Container container,
                                IInventory playerInv,
                                TileEntity te) {
        super(container);

        this.playerInv = playerInv;
        this.te = te;
    }

    public int getGuiLeft() {
        return guiLeft;
    }

    public int getGuiTop() {
        return guiTop;
    }

    public void add(GuiButton btn) {
        func_189646_b(btn);
    }

    @Override
    public void initGui() {
        super.initGui();

        for (IWidget widget : background_widgets) {
            widget.init();
        }
        for (IWidget widget : foreground_widgets) {
            widget.init();
        }
    }

    public FontRenderer getFontRenderer() {
        return fontRendererObj;
    }

    public void AddWidget(IWidget widget, boolean foreground) {
        if (foreground)
            foreground_widgets.add(widget);
        else
            background_widgets.add(widget);
    }

    public void updateWidgets() {
    }

    public int nextId() {
        return btn_id++;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks,
                                                   int mouseX, int mouseY) {
        updateWidgets();

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        drawBackground();

        List<String> hoveringText = new ArrayList<String>();
        for (IWidget widget : background_widgets) {
            widget.draw();

            if (widget.isMouseInArea(mouseX, mouseY)) {
                widget.addHoveringText(hoveringText);
            }
        }
        if (!hoveringText.isEmpty()) {
            drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        List<String> hoveringText = new ArrayList<String>();
        for (IWidget widget : foreground_widgets) {
            widget.draw();

            if (widget.isMouseInArea(mouseX, mouseY)) {
                widget.addHoveringText(hoveringText);
            }
        }
        if (!hoveringText.isEmpty()) {
            drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (IWidget widget : foreground_widgets) {
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

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (hasActiveWidget() && activeWidget.interceptKeyPress()) {
            activeWidget.onKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }

    void setBackground(String location,
                       int offsetX, int offsetY,
                       int xSize, int ySize) {
        backgroundResource = new ResourceLocation(location);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    void drawBackground() {
        this.mc.getTextureManager().bindTexture(backgroundResource);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop,
                offsetX, offsetY,
                this.xSize, this.ySize);
    }
}
