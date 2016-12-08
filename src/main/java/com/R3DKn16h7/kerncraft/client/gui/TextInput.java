package com.R3DKn16h7.kerncraft.client.gui;

import net.minecraft.client.gui.GuiTextField;

import java.util.List;

/**
 * Created by Filippo on 27/11/2016.
 */
public class TextInput extends Widget {

    private static final String TEXTURE = "";
    private static final int TI_CLEAR_X_POS = 0;
    private static final int TI_CLEAR_Y_POS = 0;
    private static final int TI_BKGRD_X_POS = 0;
    private static final int TI_BKGRD_Y_POS = 0;
    boolean hasClearButton = true;
    GuiTextField textf;

    TextInput(AdvancedGuiContainer container, int xPosition, int yPosition,
              int xSize, int ySize) {
        super(container, xPosition, yPosition, xSize, ySize);

    }

    @Override
    public void init() {

        this.textf = new GuiTextField(container.id++, container.getFontRenderer(),
                this.xSize / 2 - 68,
                this.ySize / 2 - 46, 137, 20);
        textf.setMaxStringLength(23);
        textf.setText("sample text");
        this.textf.setFocused(true);
        super.init();
    }


    @Override
    public boolean interceptKeyPress() {
        return false;
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        //if(typedChar = KeyCodeMape)
        // lwjgl

        System.out.print(typedChar + "    " + keyCode + "\n");
        this.textf.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void draw() {
//        container.drawString(container.getFontRenderer(), text, xPosition, yPosition, 4210752);
//
//        ResourceLocation textureLocation = new ResourceLocation(TEXTURE);
//        //container.dra
//        container.mc.getTextureManager().bindTexture(textureLocation);
//        container.drawTexturedModalRect(xPosition, yPosition,
//                TI_BKGRD_X_POS, TI_BKGRD_Y_POS,
//                xSize, ySize);
//        if(hasClearButton) {
//            container.mc.getTextureManager().bindTexture(textureLocation);
//            container.drawTexturedModalRect(xPosition + xSize - 20, yPosition,
//                    TI_CLEAR_X_POS, TI_CLEAR_Y_POS,
//                    20, ySize);
//        }
// super.drawScreen(par1, par2, par3);
        //   this.textf.drawTextBox();TexturedElement
        this.textf.updateCursorCounter();
        this.textf.drawTextBox();
        super.draw();
    }

    public void clearField() {
        // text = "";
    }

    @Override
    public void onHover(int mouseX, int mouseY) {

    }

    @Override
    public List<String> addHoveringText(List<String> hoveringText) {
        hoveringText.add("TextInput");
        return hoveringText;
    }

    @Override
    public void onClicked(int mouseX, int mouseY, int mouseButton) {

        this.textf.mouseClicked(mouseX, mouseY, mouseButton);

        //if( xSize - mouseX < 20 ) {
        //    clearField();
        //}
        System.out.print("Click!");
    }
}
