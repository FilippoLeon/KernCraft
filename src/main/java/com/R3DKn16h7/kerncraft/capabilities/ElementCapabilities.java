package com.R3DKn16h7.kerncraft.capabilities;

import com.R3DKn16h7.kerncraft.elements.Element;
import com.R3DKn16h7.kerncraft.elements.ElementBase;
import com.R3DKn16h7.kerncraft.utils.PlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

/**
 * Created by Filippo on 22-Apr-17.
 */
public class ElementCapabilities {

    @CapabilityInject(IElementContainer.class)
    public static Capability<IElementContainer>
            CAPABILITY_ELEMENT_CONTAINER = null;

    public static boolean hasCapability(ItemStack stack) {
        return stack.hasCapability(CAPABILITY_ELEMENT_CONTAINER, null);
    }

    public static IElementContainer getCapability(ItemStack stack) {
        return stack.getCapability(CAPABILITY_ELEMENT_CONTAINER, null);
    }

    public static void addTooltip(ItemStack stack,
                                  List<String> listOfTooltipInfo) {
        if (hasCapability(stack)) {
            IElementContainer cap = getCapability(stack);

            listOfTooltipInfo.add(String.format("Elements: %d/%d",
                    cap.getNumberOfElements(), cap.getMaxNumberOfElements()));
            listOfTooltipInfo.add(String.format("Amount: %d/%d",
                    cap.getTotalAmount(), cap.getCapacity()));

            if (PlayerHelper.isCtrlKeyDown()) {
                for (Map.Entry entry : cap.getElementMap().entrySet()) {
                    Element elem = ElementBase.getElement(((Integer) entry.getKey()));
                    String elemStr = elem.toSymbol();
                    listOfTooltipInfo.add(String.format("* %d u of %s",
                            entry.getValue(), elemStr));
                }
            } else {
                listOfTooltipInfo.add("Hold ctrl for more stuff.");
            }
        }
    }

    public static int amountThatCanBeTansfered(IElementContainer from,
                                               IElementContainer to,
                                               int id, int i) {
        int removable = from.removeAmountOf(id, 100, true);
        return to.addAmountOf(
                id, removable, true
        );
    }

    public static int transferAmount(IElementContainer from,
                                     IElementContainer to,
                                     int id, int transferable, Entity receiver) {
        from.removeAmountOf(id, transferable, false);
        return to.addAmountOf(
                id, transferable, false, receiver
        );
    }

    @SideOnly(Side.CLIENT)
    public static void addDetailedTooltipForSingleElement(ItemStack stack,
                                                          List<String> tooltipList) {
        if (!hasCapability(stack)) return;

        IElementContainer cap = getCapability(stack);

        Element element = getFirstElement(cap);
        if (element == null) return;

        tooltipList.add(String.format("Element: %s (%s%s) - %d",
                element.getLocalizedName(), element.toSymbol(), TextFormatting.GRAY, element.id));

        if (PlayerHelper.isCtrlKeyDown()) {
            tooltipList.add(String.format("Mass: %s", element.mass));
            tooltipList.add(String.format("Toxic: %s",
                    element.toxic ?
                            TextFormatting.RED + "yes" :
                            TextFormatting.GREEN + "no"
                    )
            );
            tooltipList.add(String.format("Radioactive: %s",
                    element.half_life > 0 ?
                            TextFormatting.RED + "yes" +
                                    TextFormatting.RESET +
                                    String.format(
                                            " (half-life: %.2e s)",
                                            element.half_life
                                    ) :
                            TextFormatting.GREEN + "no")
            );
        }

        int amount = cap.getTotalAmount();
        int maxAmount = cap.getCapacity();
        if (amount > 0) {
            tooltipList.add(String.format("Quantity: %d/%d", amount, maxAmount));
        }

        if (PlayerHelper.isCtrlKeyDown()) {
            tooltipList.add(String.format("%s%sRight Click to transfer from\n" +
                            "off hand to main hand. Hold\n" +
                            "%sCTRL%s and Right Click to transfer\n" +
                            "from main hand to off hand.",
                    com.mojang.realmsclient.gui.ChatFormatting.ITALIC,
                    TextFormatting.GRAY.toString(),
                    TextFormatting.AQUA.toString(),
                    TextFormatting.GRAY.toString()
                    )
            );
        } else {
            tooltipList.add(String.format("%s%sHold %sCTRL%s for more stuff...",
                    TextFormatting.ITALIC.toString(),
                    TextFormatting.GRAY.toString(),
                    TextFormatting.AQUA.toString(),
                    TextFormatting.GRAY.toString()
                    )
            );
        }

    }

    static public Element getFirstElement(IElementContainer cap) {
        if (cap.getNumberOfElements() > 0) {
            return ElementBase.getElement(cap.getElements()[0]);
        }
        return null;
    }

    @Deprecated
    static public Element getFirstElement(ItemStack stack) {
        if (hasCapability(stack)) {
            IElementContainer cap = getCapability(stack);
            return getFirstElement(cap);
        }
        return null;
    }
}
