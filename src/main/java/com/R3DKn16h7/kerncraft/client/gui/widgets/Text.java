package com.R3DKn16h7.kerncraft.client.gui.widgets;

import com.R3DKn16h7.kerncraft.client.gui.IAdvancedGuiContainer;
import net.minecraft.client.gui.FontRenderer;

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

    private boolean shadow = false;
    private int textColor = 4210752;

    public Text(IAdvancedGuiContainer container, int xPosition, int yPosition,
                int xSize, int ySize,
                Alignment alignment, Ellipsis ellipsis) {
        super(container, xPosition, yPosition, xSize, ySize);
        this.alignment = alignment;
        this.ellipsis = ellipsis;
    }

    public Text(IAdvancedGuiContainer container, int xPosition, int yPosition,
                int xSize, int ySize,
                Alignment alignment) {
        super(container, xPosition, yPosition, xSize, ySize);
        this.alignment = alignment;
        this.ellipsis = Ellipsis.OVERFLOW;
    }

    public Text(IAdvancedGuiContainer container, int xPosition, int yPosition,
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

        int offset = 0;

        switch (alignment) {
            case LEFT:
                fr.drawString(disp_text, xPosition, yPosition + offset, textColor, this.shadow);
                break;
            case MIDDLE:
                // TODO: vertical alignment option
                offset = (ySize - 6) / 2;
                int size = fr.getStringWidth(disp_text);
                fr.drawString(disp_text,
                        xPosition + (xSize - size) / 2, yPosition + offset, textColor);
                break;
            case RIGHT:
                fr.drawString(disp_text,
                        xPosition + (xSize - fr.getStringWidth(text)), yPosition + offset, textColor, this.shadow);
                break;
        }

    }

    public void setText(String text) {
        this.text = text;
    }

    public Text setShadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public Text setColor(int color) {
        textColor = color;
        return this;
    }

}
