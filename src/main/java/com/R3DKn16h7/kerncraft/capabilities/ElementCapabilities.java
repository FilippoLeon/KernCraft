package com.R3DKn16h7.kerncraft.capabilities;

import com.R3DKn16h7.kerncraft.elements.Element;
import com.R3DKn16h7.kerncraft.elements.ElementBase;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Map;

/**
 * Created by Filippo on 22-Apr-17.
 */
public class ElementCapabilities {

    @CapabilityInject(IElementContainer.class)
    public static Capability<IElementContainer>
            CAPABILITY_ELEMENT_CONTAINER = null;

    public static void addTooltip(ItemStack stack, List<String> listOfTooltipInfo) {
        if (stack.hasCapability(CAPABILITY_ELEMENT_CONTAINER, null)) {
            IElementContainer cap =
                    stack.getCapability(CAPABILITY_ELEMENT_CONTAINER, null);


            boolean isCtrlKeyDown = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ||
                    Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
            if (!isCtrlKeyDown && Minecraft.IS_RUNNING_ON_MAC)
                isCtrlKeyDown = Keyboard.isKeyDown(Keyboard.KEY_LMETA) ||
                        Keyboard.isKeyDown(Keyboard.KEY_RMETA);

            listOfTooltipInfo.add(String.format("Elements: %d/%d",
                    cap.getNumberOfElements(), +cap.getMaxNumberOfElements()));
            listOfTooltipInfo.add(String.format("Amount: %d/%d",
                    cap.getTotalAmount(), cap.getCapacity()));

            if (isCtrlKeyDown) {
                for (Map.Entry entry : cap.getElementMap().entrySet()) {
                    Element elem = ElementBase.getElement(((Integer) entry.getKey()));
                    String elemStr = elem.toSymbol();
                    listOfTooltipInfo.add(String.format("%d u of %s",
                            entry.getValue(), elemStr));
                }
            } else {
                listOfTooltipInfo.add("Hold ctrl for more stuff.");
            }
        }
    }
}
