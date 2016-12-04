package com.R3DKn16h7.quantumbase.client.gui;

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
public class AdvancedGuiContainer extends GuiContainer {

    // Margin of gui backgound
    public int borderLeft = 9;
    public int borderTop = 18;
    public int backgroundStartx;
    public int backgroundStarty;
    private ArrayList<IWidget> background_widgets = new ArrayList<IWidget>();
    private ArrayList<IWidget> foreground_widgets = new ArrayList<IWidget>();
    private IInventory playerInv;
    private TileEntity te;
    // Background offset within texture
    private int offsetX = 0;
    private int offsetY = 0;
    private ResourceLocation backgroundResource;

    public AdvancedGuiContainer(Container container,
                                IInventory playerInv,
                                TileEntity te) {
        super(container);

        this.playerInv = playerInv;
        this.te = te;

        setGuiSize(176, 166);
        backgroundStartx = guiLeft;
        backgroundStarty = guiTop;

        setBackground("quantumbase:textures/gui/container/extractor_gui.png",
                0, 0, 176, 166);

        //xSize = 176;
        /** The Y size of the inventory window in pixels. */
        //protected int ySize = 166;

        AddWidget(
                new GuiElement(this,
                        "textures/gui/container/furnace.png", 80, 80,
                        14, 14, 176, 0),
                false);
        AddWidget(
                new GuiElement(this,
                        "textures/gui/container/furnace.png", 18, 18,
                        14, 14, 176, 0),
                true);
        AddWidget(new Button(this, "textures/gui/container/furnace.png",
                0, 0, 14, 14, 176, 0), true);

        AddWidget(AnimatedGuiElement.ARROW(this, 40, 40), true);

        //Runnable r2 = () -> System.out.println("Hello world two!");
    }

    public void AddWidget(IWidget widget, boolean foreground) {
        if (foreground)
            foreground_widgets.add(widget);
        else
            background_widgets.add(widget);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks,
                                                   int mouseX, int mouseY) {
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
        for (IWidget widget : foreground_widgets) {
            if (widget.isMouseInArea(mouseX, mouseY)) {
                widget.click(mouseButton);
            }
        }
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
