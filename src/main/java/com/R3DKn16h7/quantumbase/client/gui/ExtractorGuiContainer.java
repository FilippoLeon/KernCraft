package com.R3DKn16h7.quantumbase.client.gui;

import com.R3DKn16h7.quantumbase.tileentities.ExtractorTileEntity;
import gui.R3DKn16h7.quantumbase.guicontainer.ExtractorContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class ExtractorGuiContainer extends GuiContainer {

    private IInventory playerInv;
    private ExtractorTileEntity te;

    public ExtractorGuiContainer(IInventory playerInv, ExtractorTileEntity te) {
        super(new ExtractorContainer(playerInv, te));

        this.playerInv = playerInv;
        this.te = te;

//	    this.xSize = 289;
//	    this.ySize = 265;
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
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = ""; // this.te.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, 88 - this.fontRendererObj.getStringWidth(s) / 2,
                6, 4210752);
        this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(),
                8, 72, 4210752);

        String perc = String.format("%f.2 %%", this.te.getProgressPerc() * 100);

        this.fontRendererObj.drawString(perc, 16, 66, 4210752);
    }
}