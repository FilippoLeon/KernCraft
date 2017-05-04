package com.R3DKn16h7.kerncraft.crafting;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the recipe for the Electrolyzer.
 */
public class ElectrolyzerRecipe implements ISmeltingRecipe {
    // TODO: might want to switch to ArrayList to ease loop
    public ItemStack anode;
    public ItemStack cathode;
    public NonNullList<ItemStack> input = NonNullList.create();
    public List<ElementStack> outputs = new ArrayList<>();
    public FluidStack fluid;
    public int energy;
    public int cost;

    public ElectrolyzerRecipe(ItemStack anode, ItemStack cathode,
                              NonNullList<ItemStack> input, List<ElementStack> outputs,
                              FluidStack fluid, int cost, int energy) {
        this.anode = anode;
        this.cathode = cathode;
        this.input = input;
        this.outputs = outputs;

        this.energy = energy;
        this.fluid = fluid;
        this.cost = cost;
    }

    static void parseElectrolyzerRecipe(Node nNode) {
        Element nElement = ((Element) nNode);

        int energy = KernCraftRecipes.readAsIntOrDefault(nElement, "energy", 0);
        int cost = KernCraftRecipes.readAsIntOrDefault(nElement, "cost", 0);

        NonNullList<ItemStack> input = NonNullList.create();
        ItemStack anode = ItemStack.EMPTY;
        ItemStack cathode = ItemStack.EMPTY;
        List<ElementStack> outputs = new ArrayList<>();
        FluidStack fluid = null;


        NodeList nList = nNode.getChildNodes();
        for (int i = 0; i < nList.getLength(); ++i) {
            if (nList.item(i).getNodeType() != Node.ELEMENT_NODE) continue;

            Element nChildNode = ((Element) nList.item(i));
            switch (nChildNode.getNodeName()) {
                case "Anode":
                    try {
                        anode = KernCraftRecipes.parseAsItemStackList(nChildNode,
                                1, 1).get(0);
                    } catch (Exception e) {
                        KernCraft.LOGGER.error("No anode specified, invalid recipe");
                        e.printStackTrace();
                        return;
                    }
                    break;
                case "Cathode":
                    try {
                        cathode = KernCraftRecipes.parseAsItemStackList(nChildNode,
                                1, 1).get(0);
                    } catch (Exception e) {
                        KernCraft.LOGGER.error("No cathode specified, invalid recipe");
                        e.printStackTrace();
                        return;
                    }
                    break;
                case "Input":
                    try {
                        input = KernCraftRecipes.parseAsItemStackList(nChildNode,
                                1, 1);
                    } catch (Exception e) {
                        KernCraft.LOGGER.error("No anode specified, invalid recipe");
                        e.printStackTrace();
                        return;
                    }
                    break;
                case "Output":
                    outputs = KernCraftRecipes.parseAsElementStackList(nChildNode,
                            -1, 2);
                    break;
                case "Fluid":
                    if (fluid != null) {
                        KernCraft.LOGGER.warn("Too many fluids for Extractor Recipe.");
                    }
                    fluid = KernCraftRecipes.parseAsFluid(nChildNode);
                    break;
                default:
                    KernCraft.LOGGER.warn("Unrecognized element for Extractor Recipe.");
                    break;
            }
        }

        if (cost < 0) {
            KernCraft.LOGGER.error("Negative cost for Extractor Recipe.");
            return;
        }

        KernCraftRecipes.ELECTROLYZER_RECIPES.add(
                new ElectrolyzerRecipe(anode, cathode, input,
                        outputs, fluid, cost, energy)
        );
    }

    @Override
    public int getCost() {
        return cost;
    }
}
