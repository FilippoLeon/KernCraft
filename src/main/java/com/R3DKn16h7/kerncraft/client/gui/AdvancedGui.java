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
 *
 * An advance gui that provides some Widget functionality to a standard
 * GuiScreen.
 */
public class AdvancedGui extends GuiScreen implements IAdvancedGuiContainer {
    /**
     * Null or active widget if any.
     */
    public IWidget activeWidget;

    /**
     * The margin on the left (and right of the interface).
     */
    public int borderLeft = 9;
    /**
     * The margin from the top interface.
     */
    public int borderTop = 18;

    /**
     * Each GUI as an unique identifier.
     */
    public int id = 0;
    /**
     * A dinamic background is a standardized background that
     * can be resized according to the size of the GUI.
     */
    public boolean use_dynamic_background = false;
    /**
     * The player inventory, which is displayed then the GUI is displayed.
     * (If needed)
     * TODO: this might be removed?
     */
    protected IInventory playerInv;
    /**
     * Tile entity to which this GUI belongs to, on null if no tile entity is needed.
     */
    protected TileEntity te;
    /**
     * List of all interface widgets.
     */
    private ArrayList<IWidget> widgets = new ArrayList<IWidget>();
    /**
     * Background offset within texture
     * I.e. start coordinates of the actual background on the texture.
     */
    private int offsetX = 0;
    private int offsetY = 0;
    /**
     * Location of the background.
     */
    private ResourceLocation backgroundResource;
    private int btn_id = 0;
    private int guiLeft, guiTop;
    private int xSize, ySize;
    public AdvancedGui(IInventory playerInv,
                       TileEntity te) {
        this.playerInv = playerInv;
        this.te = te;
    }

    /**
     * Middle point of the GUI in local coordinates.
     * @return coordinate of the middle point
     * of the interface, coordinate zero is top-left corner.
     */
    public int getMiddle() {
        return this.xSize / 2;
    }

    /**
     * Get left position with the addition of the border.
     * @return
     */
    public int getBorderLeft() {
        return borderLeft;
    }

    public int getBorderTop() {
        return borderTop;
    }

    /**
     * Each GUI as one, and only one, active widget.
     * TODO: this routine does nothing.
     * @param widget unused.
     */
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

    /**
     * ???
     * @return
     */
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
            this.drawTexturedModalRect(this.guiLeft, this.guiTop,
                    0, 0,
                    this.xSize / 2, this.ySize / 2);
            this.drawTexturedModalRect(this.guiLeft, this.guiTop + this.ySize / 2,
                    0, 165 - this.ySize / 2,
                    this.xSize / 2, this.ySize / 2);
            this.drawTexturedModalRect(this.guiLeft + this.xSize / 2, this.guiTop,
                    256 - this.xSize / 2, 0,
                    this.xSize / 2, this.ySize / 2);
            this.drawTexturedModalRect(this.guiLeft + this.xSize / 2, this.guiTop + this.ySize / 2,
                    256 - this.xSize / 2, 165 - this.ySize / 2,
                    this.xSize / 2, this.ySize / 2);
        } else {
            this.drawTexturedModalRect(this.guiLeft, this.guiTop,
                    offsetX, offsetY,
                    this.xSize, this.ySize);
        }
    }
}
