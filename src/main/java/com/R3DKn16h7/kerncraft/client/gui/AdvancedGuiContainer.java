package com.R3DKn16h7.kerncraft.client.gui;

import com.R3DKn16h7.kerncraft.client.gui.widgets.IWidget;
import com.R3DKn16h7.kerncraft.client.gui.widgets.Widget;
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
public class AdvancedGuiContainer extends GuiContainer implements IAdvancedGui {

    public IWidget activeWidget;

    // Margin of gui backgound
    public int borderLeft = 9;
    public int borderTop = 18;
    // Gui Id
    public int id = 0;
    // Is the GUI using a dynamic background
    public boolean use_dynamic_background = false;

    protected IInventory playerInv;
    protected TileEntity te;

    private ArrayList<IWidget> background_widgets = new ArrayList<IWidget>();
    private ArrayList<IWidget> foreground_widgets = new ArrayList<IWidget>();

    //// Background info
    // Background offset within texture
    private int offsetX = 0;
    private int offsetY = 0;
    // Background texture
    private ResourceLocation backgroundResource;
    // Size of backfreound texture
    private int xBackgroundSize = 250;
    private int yBackgroundSize = 176;

    private int btn_id = 0;

    public AdvancedGuiContainer(Container container,
                                IInventory playerInv,
                                TileEntity te) {
        super(container);

        this.playerInv = playerInv;
        this.te = te;
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

    public int getBorderLeft() {
        return borderLeft;
    }

    public int getBorderTop() {
        return borderTop;
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


    void RemoveWidget(Widget wid) {
        foreground_widgets.remove(wid);
        background_widgets.remove(wid);
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

        GlStateManager.pushMatrix();
        GlStateManager.translate(guiLeft - borderLeft, guiTop - borderTop, 0);
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
        GlStateManager.popMatrix();
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


    void setDynamicBackground() {
        use_dynamic_background = true;
        backgroundResource = new ResourceLocation("textures/gui/demo_background.png");
        this.xBackgroundSize = 250;
        this.yBackgroundSize = 167;
    }

    void setBackground(String location,
                       int offsetX, int offsetY,
                       int xSize, int ySize) {
        use_dynamic_background = false;
        backgroundResource = new ResourceLocation(location);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.xBackgroundSize = xSize;
        this.yBackgroundSize = ySize;
    }

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
