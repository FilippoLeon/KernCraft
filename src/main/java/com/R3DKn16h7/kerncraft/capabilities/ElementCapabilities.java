package com.R3DKn16h7.kerncraft.capabilities;

import com.R3DKn16h7.kerncraft.elements.Element;
import com.R3DKn16h7.kerncraft.elements.ElementRegistry;
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
                    Element elem = ElementRegistry.getElement(((Integer) entry.getKey()));
                    String elemStr = elem.toSymbol();
                    listOfTooltipInfo.add(String.format("* %d u of %s",
                            entry.getValue(), elemStr));
                }
            } else {
                listOfTooltipInfo.add("Hold ctrl for more stuff.");
            }
        }
    }

    /**
     * Get amount of element that can be transfered to the other container.
     *
     * @param from
     * @param to
     * @param id
     * @param amount
     * @return
     */
    public static int amountThatCanBeTansfered(IElementContainer from,
                                               IElementContainer to,
                                               int id, int amount) {
        int removable = from.removeAmountOf(id, 100, true);
        return to.addAmountOf(
                id, removable, true
        );
    }

    public static int transferAmount(IElementContainer from,
                                     IElementContainer to,
                                     int id, int transferable,
                                     Entity receiver) {
        int removable = from.removeAmountOf(id, transferable, false);
        return to.addAmountOf(
                id, removable, false, receiver
        );
    }

    public static int transferAllElements(IElementContainer from,
                                          IElementContainer to,
                                          int amount,
                                          Object receiver,
                                          boolean simulate,
                                          Map<Integer, Integer> transferable) {
        int[] elemFrom = from.getElements();

        int added = 0;
        for (int element : elemFrom) {
            int removable = from.removeAmountOf(element, amount, simulate);
            int addable = to.addAmountOf(
                    element, removable, simulate, receiver
            );
            if (transferable != null) {
                if (transferable.containsKey(element)) {
                    transferable.put(element, transferable.get(element) + addable);
                } else {
                    transferable.put(element, addable);
                }
            }
            added += addable;
        }
        return added;
    }


    @SideOnly(Side.CLIENT)
    public static void addDetailedTooltipForSingleElement(ItemStack stack,
                                                          List<String> tooltipList) {
        if (!hasCapability(stack)) return;

        IElementContainer cap = getCapability(stack);

        Element element = getFirstElement(cap);
        if (element == null) {
            // Add some info to empty Container
            tooltipList.add(String.format("Can contain elements."));
            if (PlayerHelper.isCtrlKeyDown()) {
                int maxAmount = cap.getCapacity();
                if (maxAmount > 0) {
                    tooltipList.add(String.format("Capacity: %d", maxAmount));
                }
                tooltipList.add(String.format("Confines: %b", cap.doesConfine()));
                tooltipList.add(String.format("Isolates: %b", cap.doesIsolate()));
                tooltipList.add(String.format("State:%s%s%s",
                        cap.acceptsElementWithState(Element.State.GAS) ? " Gas" : "",
                        cap.acceptsElementWithState(Element.State.LIQUID) ? " Liquid" : "",
                        cap.acceptsElementWithState(Element.State.SOLID) ? " Solid" : ""
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
            return;
        }

        tooltipList.add(String.format("Element: %s (%s%s) - #%d",
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
                    element.halfLife > 0 ?
                            TextFormatting.RED + "yes" +
                                    TextFormatting.RESET +
                                    String.format(
                                            " (half-life: %.2e s)",
                                            element.halfLife
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
            tooltipList.add(String.format("-------------\n" +
                            "%s%sRight Click to transfer from\n" +
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
            return ElementRegistry.getElement(cap.getElements()[0]);
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
