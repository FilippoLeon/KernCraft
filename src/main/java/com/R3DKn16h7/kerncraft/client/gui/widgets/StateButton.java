package com.R3DKn16h7.kerncraft.client.gui.widgets;

import com.R3DKn16h7.kerncraft.client.gui.IAdvancedGui;
import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.MessageRedstoneControl;
import com.R3DKn16h7.kerncraft.tileentities.IRedstoneSettable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.IntConsumer;

/**
 * Created by Filippo on 09/12/2016.
 */
@SideOnly(Side.CLIENT)
public class StateButton extends BetterButton {
    public int stateIdx = 0;

    public ArrayList<State> states = new ArrayList<State>();
    private IntConsumer OnStateChangedConsumer;

    public StateButton(IAdvancedGui container, int id, int x, int y,
                       int widthIn, int heightIn) {
        super(container, id, x, y, widthIn, heightIn);
    }

    public StateButton(IAdvancedGui container, int x, int y,
                       int widthIn, int heightIn) {
        super(container, x, y, widthIn, heightIn);
    }

    public StateButton(IAdvancedGui container, int x, int y) {
        this(container, x, y, 200, 20);
    }

    public static StateButton REDSTONE_MODE(IAdvancedGui container,
                                            IRedstoneSettable te) {
        // TODO: BETTER FIX THIS

        TileEntity te2 = ((TileEntity) te);
        BlockPos pos = te2.getPos();
        IntConsumer sdd = (int state) -> {
            te.setRedstoneMode(state);
            KernCraftNetwork.networkWrapper.sendToServer(new MessageRedstoneControl(state,  pos));
        };

        StateButton redstoneModeStateButton = new StateButton(container, -20, 20, 20, 20);
        redstoneModeStateButton.setText("")
                .setAlignment(Widget.Alignment.MIDDLE);
        redstoneModeStateButton.addState(new StateButton.State("kerncraft:textures/gui/widgets.png",
                "", "Active with signal", 16 * 4, 16 * 7))
                .addState(new StateButton.State("kerncraft:textures/gui/widgets.png",
                        "", "Active without signal", 16 * 5, 16 * 7))
                .addState(new StateButton.State("kerncraft:textures/gui/widgets.png",
                        "", "Ignore", 16 * 2, 16 * 7))
                .addOnStateChanged(sdd);
        return redstoneModeStateButton;
    }

    public StateButton setState(int i) {
        if (states == null || states.size() == 0) return this;
        stateIdx = i % states.size();
        State state = states.get(stateIdx);
        if (state.texture != null)
            setIcon(state.texture, state.xTexture, state.yTexture, 16, 16);
        if (state.tint != null)
            setTint(state.tint);
        setText(state.text);
        setTooltip(state.tooltip);
        if (OnStateChangedConsumer != null) OnStateChangedConsumer.accept(stateIdx);
        return this;
    }

    public StateButton addState(State state) {
        states.add(state);
        if (states.size() == 1) {
            nextState();
        }
        return this;
    }

    public StateButton addOnStateChanged(IntConsumer consumer) {
        OnStateChangedConsumer = consumer;
        return this;
    }

    public void nextState() {
        //setIcon();
        setState(++stateIdx);
    }

    @Override
    public void onClicked(int mouseX, int mouseY, int mouseButton) {
        nextState();
    }

    static public class State {
        private final int xTexture;
        private final int yTexture;
        public String texture;
        public String text;
        public String tooltip;
        public Color tint;

        public State() {
            this.xTexture = 0;
            this.yTexture = 0;
        }

        public State(String texture, String text, String tooltip, int xTexture, int yTexture) {
            this.texture = texture;
            this.text = text;
            this.tooltip = tooltip;
            this.xTexture = xTexture;
            this.yTexture = yTexture;
        }

        public State setTint(Color tint) {
            this.tint = tint;
            return this;
        }

        public State setText(String txt) {
            this.text = txt;
            return this;
        }

        public State setTooltip(String txt) {
            this.tooltip = txt;
            return this;
        }
    }
}
