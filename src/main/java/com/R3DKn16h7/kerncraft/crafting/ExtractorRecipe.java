package com.R3DKn16h7.kerncraft.crafting;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the recipe for the extractor.
 */
public class ExtractorRecipe implements ISmeltingRecipe {
    public ItemStack item;
    public ItemStack catalyst;

    /**
     * TODO: Might need to switch to ArrayList to favour loops
     */
    public ElementStack[] outputs;
    public int energy;
    public List<FluidStack> fluids = new ArrayList<>();

    public ExtractorRecipe(ItemStack item, ItemStack catalyst,
                           ElementStack[] outputs, int energy) {
        this.item = item;
        this.catalyst = catalyst;
        this.outputs = outputs;
        this.energy = energy;
    }

    public ExtractorRecipe(ItemStack input, ItemStack catalyst,
                           ElementStack[] outputs, int energy,
                           List<FluidStack> fluids) {
        this(input, catalyst, outputs, energy);
        this.fluids = fluids;
    }

    static public void parseExtractorRecipe(Node nNode) {
        Element nElement = ((Element) nNode);
        if (nElement == null) return;

        int energy = KernCraftRecipes.readAsIntOrDefault(nElement, "energy", 0);
        ItemStack input = null;
        ItemStack catalyst = ItemStack.EMPTY;
        ElementStack[] elements = new ElementStack[4];
        List<FluidStack> fluids = new ArrayList<>();

        NodeList nList = nNode.getChildNodes();
        for (int i = 0; i < nList.getLength(); ++i) {
            if (nList.item(i).getNodeType() != Node.ELEMENT_NODE) continue;

            Element nChildNode = ((Element) nList.item(i));
            switch (nChildNode.getNodeName()) {
                case "Input":
                    if (input != null && !input.isEmpty()) {
                        KernCraft.LOGGER.warn("Too many inputs for Extractor Recipe.");
                    }
                    try {
                        input = KernCraftRecipes.parseAsItemStack(
                                ((Element) nChildNode.getElementsByTagName("*").item(0))
                        );
                    } catch (Exception e) {
                        KernCraft.LOGGER.warn("Invalid input for extractor recipe.");
                    }
                    break;
                case "Catalyst":
                    if (!catalyst.isEmpty()) {
                        KernCraft.LOGGER.warn("Too many catalysts for Extractor Recipe.");
                    }
                    try {
                        catalyst = KernCraftRecipes.parseAsItemStack(((Element) nChildNode.getElementsByTagName("*").item(0)));
                    } catch (Exception e) {
                            KernCraft.LOGGER.warn("Invalid catalyst for extractor recipe.");
                    }
                    break;
                case "Output":
                    NodeList nChildList = nChildNode.getElementsByTagName("*");
                    if (nChildList.getLength() > 4) {
                        KernCraft.LOGGER.warn("Too many output elements for Extractor Recipe.");
                    }
                    for (int j = 0; j < Math.min(nChildList.getLength(), 4); j++) {
                        try {
                            elements[j] = KernCraftRecipes.parseAsElementStack(((Element) nChildList.item(j)));
                        } catch (Exception e) {
                            KernCraft.LOGGER.error("Invalid Extractor recipe, cannot parse as element stack.");
                            KernCraft.LOGGER.error(e.getLocalizedMessage());
                            e.printStackTrace();
                            break;
                        }
                    }
                    break;
                case "Fluid":
                    try {
                        FluidStack fluidStack = KernCraftRecipes.parseAsFluid(nChildNode);
                        if (fluidStack == null) {
                            KernCraft.LOGGER.error("Invalid fluid for Extractor recipe.");
                            return;
                        }
                        fluids.add(fluidStack);
                    } catch (Exception e) {
                        KernCraft.LOGGER.error("Invalid fluid for Extractor recipe.");
                        e.printStackTrace();
                        return;
                    }
                    break;
                default:
                    KernCraft.LOGGER.warn("Unrecognized element for Extractor Recipe.");
                    break;
            }
        }

        if (input != null && input.isEmpty()) {
            KernCraft.LOGGER.error("Air as input recipe is a no-no.");
            return;
        }

        if (elements[0] == null) {
            KernCraft.LOGGER.error("No output elements for Extractor Recipe.");
            return;
        }

        if (energy < 0) {
            KernCraft.LOGGER.error("Negative energy for Extractor Recipe.");
            return;
        }

        KernCraftRecipes.EXTRACTOR_RECIPES.add(
                new ExtractorRecipe(input, catalyst,
                        elements, energy, fluids)
        );
    }

    @Override
    public int getCost() {
        return energy;
    }
}
