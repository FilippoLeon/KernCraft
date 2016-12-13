package com.R3DKn16h7.kerncraft.client.gui.widgets;

import com.R3DKn16h7.kerncraft.client.gui.IAdvancedGuiContainer;
import com.R3DKn16h7.kerncraft.network.KernCraftNetwork;
import com.R3DKn16h7.kerncraft.network.MessageRedstoneControl;
import com.R3DKn16h7.kerncraft.tileentities.IRedstoneSettable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    public StateButton(IAdvancedGuiContainer container, int id, int x, int y,
                       int widthIn, int heightIn) {
        super(container, id, x, y, widthIn, heightIn);
    }

    public StateButton(IAdvancedGuiContainer container, int x, int y,
                       int widthIn, int heightIn) {
        super(container, x, y, widthIn, heightIn);
    }

    public StateButton(IAdvancedGuiContainer container, int x, int y) {
        this(container, x, y, 200, 20);
    }

    public static StateButton REDSTONE_MODE(IAdvancedGuiContainer container,
                                            IRedstoneSettable te) {
        IntConsumer sdd = (int state) -> {
            te.setMode(state);
            //KernCraftNetwork.networkWrapper.sendToAll(new MessageRedstoneControl(state, te.getPos()));
            KernCraftNetwork.networkWrapper.sendToServer(new MessageRedstoneControl(state, te.getPos()));
        };

        StateButton btb2 = new StateButton(container, -20, 20, 20, 20);
        btb2.setText("")
                .setAlignment(Widget.Alignment.MIDDLE);
        btb2.addState(new StateButton.State("kerncraft:textures/gui/widgets.png",
                "", "Active with signal", 16 * 4, 16 * 7))
                .addState(new StateButton.State("kerncraft:textures/gui/widgets.png",
                        "", "Active without signal", 16 * 5, 16 * 7))
                .addState(new StateButton.State("kerncraft:textures/gui/widgets.png",
                        "", "Ignore", 16 * 2, 16 * 7))
                .addOnStateChanged(sdd);
        return btb2;
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
        stateIdx++;
        if (stateIdx >= states.size()) stateIdx = 0;
        State state = states.get(stateIdx);
        setIcon(state.texture, state.xTexture, state.yTexture, 16, 16);
        setText(state.text);
        setTooltip(state.tooltip);
        if (OnStateChangedConsumer != null) OnStateChangedConsumer.accept(stateIdx);
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

        public State(String texture, String text, String tooltip, int xTexture, int yTexture) {
            this.texture = texture;
            this.text = text;
            this.tooltip = tooltip;
            this.xTexture = xTexture;
            this.yTexture = yTexture;
        }
    }
}
