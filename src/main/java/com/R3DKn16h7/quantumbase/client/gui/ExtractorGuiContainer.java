package com.R3DKn16h7.quantumbase.client.gui;

import com.R3DKn16h7.quantumbase.guicontainer.ExtractorContainer;
import com.R3DKn16h7.quantumbase.tileentities.ExtractorTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExtractorGuiContainer extends GuiContainer {

    int i = 0;
    private IInventory playerInv;
    private ExtractorTileEntity te;
    private int borderLeft = 9;
    private int borderTop = 18;

    public ExtractorGuiContainer(IInventory playerInv, ExtractorTileEntity te) {
        super(new ExtractorContainer(playerInv, te));

        this.playerInv = playerInv;
        this.te = te;

//	    this.xSize = 289;
//	    this.ySize = 265;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        for (int k = 0; k < 3; ++k)
        {
            int l = mouseX - (i + 60);
            int i1 = mouseY - (j + 14 + 19 * k);

            if (l >= 0 && i1 >= 0 && l < 108 && i1 < 19)
            {
                System.out.println("asdsdadasdsadsdasd");
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

//    	Minecraft.getMinecraft().thePlayer.sendChatMessage("Lol what a noob3");
        this.mc.getTextureManager().bindTexture(
                new ResourceLocation("quantumbase:textures/gui/container/extractor_gui.png")
        );
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
//	    this.drawModalRectWithCustomSizedTexture(this.guiLeft, this.guiTop, 0, 0,
//	            this.xSize,  this.ySize, 320, 320);


        ResourceLocation backgroundLocation = new ResourceLocation("minecraft",
                "textures/gui/container/furnace.png");

        this.mc.getTextureManager().bindTexture(backgroundLocation);

        int offset = (int) Math.ceil(14.0 * (1. - te.getFuelStoredPercentage()));
        this.drawTexturedModalRect(this.guiLeft + borderLeft + 18 * 1,
                this.guiTop + borderTop + 18 * 1 + offset,
                176, 0 + offset, 14, 14 - offset);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        //String s = ""; // this.te.getDisplayName().getUnformattedText();
        //this.fontRendererObj.drawString(s, 88 - this.fontRendererObj.getStringWidth(s) / 2,
        //        6, 4210752);
        String s = this.playerInv.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s,
                (xSize - this.fontRendererObj.getStringWidth(s)) / 2, 6, 4210752);

        String perc = String.format("%.2f%%", this.te.getProgressPerc() * 100);

        this.fontRendererObj.drawString(perc, 18*5+2, 18 + 5, 4210752);

        this.mc.getTextureManager().bindTexture(
                new ResourceLocation("textures/blocks/water_flow.png")
        );
        int maxH = 18*3;
        int w = 4;
        int h = Math.min(maxH, i);
        this.drawTexturedModalRect(8 + 8, 8 + 0 + maxH - h, 0, 0, w,  h);
        ++i;

        List<String> hoveringText = new ArrayList<String>();
        hoveringText.add("Progress:");
        if (hoveringText.isEmpty()) {
            return;
        }
        drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);

    }
}