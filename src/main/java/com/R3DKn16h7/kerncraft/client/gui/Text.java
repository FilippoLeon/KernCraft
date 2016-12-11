package com.R3DKn16h7.kerncraft.client.gui;

import net.minecraft.client.gui.FontRenderer;

import java.util.List;

/**
 * Created by Filippo on 27/11/2016.
 */
public class Text extends Widget {
    public static final int TEXT_DEFAULT_HEIGHT = 6;
    private String text;
    private String disp_text;
    private Alignment alignment;
    private Ellipsis ellipsis;
    private boolean adaptive_size = false;

    Text(AdvancedGuiContainer container, int xPosition, int yPosition,
         int xSize, int ySize,
         Alignment alignment, Ellipsis ellipsis) {
        super(container, xPosition, yPosition, xSize, ySize);
        this.alignment = alignment;
        this.ellipsis = ellipsis;
    }

    Text(AdvancedGuiContainer container, int xPosition, int yPosition,
         int xSize, int ySize,
         Alignment alignment) {
        super(container, xPosition, yPosition, xSize, ySize);
        this.alignment = alignment;
        this.ellipsis = Ellipsis.OVERFLOW;
    }

    Text(AdvancedGuiContainer container, int xPosition, int yPosition,
         Alignment alignment) {
        super(container, xPosition, yPosition, 0, TEXT_DEFAULT_HEIGHT);
        this.adaptive_size = true;
        this.alignment = alignment;
        this.ellipsis = Ellipsis.OVERFLOW;
    }

    @Override
    public void draw() {
        super.draw();

        FontRenderer fr = container.getFontRenderer();

        if (adaptive_size) {
            disp_text = text;
            return;
        }

        String end_text = "";
        switch (ellipsis) {
            case DOTS:
            case CENTER_DOTS:
                end_text += "...";
            case TRUNCATE:
                int w = fr.getStringWidth(text);
                if (w < xSize) {
                    disp_text = text;
                    break;
                }
                int t = text.length();
                int d = text.length() / 2;
                int l = d;
                while (d > 1) {
                    d /= 2;

                    int new_w = fr.getStringWidth(text.substring(0, l) + end_text);
                    if (new_w < xSize) {
                        l += d;
                    } else if (new_w > xSize) {
                        l -= d;
                    } else {
                        disp_text = text.substring(0, l) + end_text;
                        break;
                    }
                }
                if (ellipsis == Ellipsis.CENTER_DOTS)
                    disp_text = text.substring(0, l / 2) + end_text + text.substring(l / 2, t);
                else disp_text = text.substring(0, l) + end_text;
                break;
            case OVERFLOW:
                disp_text = text;
                break;
        }

        switch (alignment) {
            case LEFT:
                container.drawString(fr, disp_text, xPosition, yPosition, 4210752);
                break;
            case MIDDLE:
                container.drawCenteredString(fr, disp_text,
                        xPosition + xSize / 2, yPosition, 4210752);
                break;
            case RIGHT:
                container.drawString(fr, disp_text,
                        xPosition + (xSize - fr.getStringWidth(text)), yPosition, 4210752);
                break;
        }

    }

    void setText(String text) {
        this.text = text;

    }

    @Override
    public List<String> addHoveringText(List<String> hoveringText) {
        hoveringText.add("Text");
        return hoveringText;
    }

}
