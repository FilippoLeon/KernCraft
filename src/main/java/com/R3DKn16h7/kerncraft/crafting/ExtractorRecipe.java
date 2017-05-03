package com.R3DKn16h7.kerncraft.crafting;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represents the recipe for the extractor.
 */
public class ExtractorRecipe implements ISmeltingRecipe {
    public Item item;
    public Item catalyst;

    /**
     * TODO: Might need to switch to ArrayList to favour loops
     */
    public ElementStack[] outs;
    public int energy;

    public ExtractorRecipe(Item item_, Item catalyst_,
                           ElementStack[] outs_, int energy_) {
        item = item_;
        catalyst = catalyst_;
        outs = outs_;
        energy = energy_;
    }

    static public void parseExtractorRecipe(Node nNode) {
        int energy;
        try {
            energy = Integer.parseInt(
                    nNode.getAttributes().getNamedItem("energy").getNodeValue()
            );
        } catch (NullPointerException e) {
            energy = 0;
        }
        ItemStack input = ItemStack.EMPTY;
        ItemStack catalyst = ItemStack.EMPTY;
        ElementStack[] elements = new ElementStack[4];

        NodeList nList = nNode.getChildNodes();
        for (int i = 0; i < nList.getLength(); ++i) {
            if (nList.item(i).getNodeType() != Node.ELEMENT_NODE) continue;

            Element nChildNode = ((Element) nList.item(i));
            switch (nChildNode.getNodeName()) {
                case "Input":
                    if (!input.isEmpty()) {
                        System.err.println("Warning: Too many inputs for Extractor Recipe.");
                    }
                    input = KernCraftRecipes.parseAsItemStack(((Element) nChildNode.getElementsByTagName("*").item(0)));
                    break;
                case "Catalyst":
                    if (!catalyst.isEmpty()) {
                        System.err.println("Warning: Too many catalysts for Extractor Recipe.");
                    }
                    catalyst = KernCraftRecipes.parseAsItemStack(((Element) nChildNode.getElementsByTagName("*").item(0)));
                    break;
                case "Output":
                    NodeList nChildList = nChildNode.getElementsByTagName("*");
                    if (nChildList.getLength() > 4) {
                        System.err.println("Warning: Too many output elements for Extractor Recipe.");
                    }
                    for (int j = 0; j < Math.min(nChildList.getLength(), 4); j++) {
                        try {
                            elements[j] = KernCraftRecipes.parseAsElementStack(((Element) nChildList.item(j)));
                        } catch (Exception e) {
                            KernCraft.LOGGER.error("Invalid recipe, cannot parse as element stack.");
                            KernCraft.LOGGER.error(e.getLocalizedMessage());
                            e.printStackTrace();
                            break;
                        }
                    }
                    break;
                default:
                    System.err.println("Warning: Unrecognized element for Extractor Recipe.");
                    break;
            }
        }

        if (input.isEmpty()) {
            System.err.println("Fatal: No input for Extractor Recipe.");
            return;
        }
        if (elements[0] == null) {
            System.err.println("Warning: No output elements for Extractor Recipe.");
            return;
        }

        if (energy < 0) {
            System.err.println("Fatal: Negative energy for Extractor Recipe.");
            return;
        }

        KernCraftRecipes.EXTRACTOR_RECIPES.add(
                new ExtractorRecipe(input.getItem(), catalyst.getItem(), elements, energy)
        );
    }

    @Override
    public int getCost() {
        return energy;
    }
}
