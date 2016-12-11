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

    public AdvancedGui(IInventory playerInv,
                       TileEntity te) {
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
        super.drawScreen(mouseX, mouseY, partialTicks);
        updateWidgets();

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        drawBackground();

        List<String> hoveringText = new ArrayList<String>();
        for (IWidget widget : widgets) {
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

    void drawBackground() {
        this.mc.getTextureManager().bindTexture(backgroundResource);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop,
                offsetX, offsetY,
                this.xSize, this.ySize);
    }
}
