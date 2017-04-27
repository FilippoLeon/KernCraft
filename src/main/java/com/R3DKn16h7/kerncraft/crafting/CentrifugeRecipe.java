package com.R3DKn16h7.kerncraft.crafting;

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
 * Represents the recipe for the Centrifuge.
 */
public class CentrifugeRecipe implements ISmeltingRecipe {
    public NonNullList<ItemStack> inputs = NonNullList.create();
    public List<ElementStack> outputs = new ArrayList<>();
    public FluidStack fluid;
    public int energy;
    public int cost;

    public CentrifugeRecipe(NonNullList<ItemStack> inputs,
                            List<ElementStack> outputs,
                            FluidStack fluid, int energy, int cost) {
        this.inputs = inputs;
        this.outputs = outputs;

        this.energy = energy;
        this.fluid = fluid;
        this.cost = cost;
    }

    static void parseCentrifugeRecipe(Node nNode) {
        Element nElement = ((Element) nNode);

        int energy = KernCraftRecipes.readAsIntOrDefault(nElement, "energy", 0);
        int cost = KernCraftRecipes.readAsIntOrDefault(nElement, "cost", 0);

        NonNullList<ItemStack> inputs = NonNullList.create();
        List<ElementStack> outputs = new ArrayList<>();
        FluidStack fluid = null;


        NodeList nList = nNode.getChildNodes();
        for (int i = 0; i < nList.getLength(); ++i) {
            if (nList.item(i).getNodeType() != Node.ELEMENT_NODE) continue;

            Element nChildNode = ((Element) nList.item(i));
            switch (nChildNode.getNodeName()) {
                case "Input":
                    inputs = KernCraftRecipes.parseAsItemStackList(nChildNode,
                            -1, 4);
                    break;
                case "Output":
                    outputs = KernCraftRecipes.parseAsElementStackList(nChildNode,
                            1, 6);
                    break;
                case "Fluid":
                    if (fluid != null) {
                        System.err.println("Warning: Too many fluids for Recipe.");
                    }
                    fluid = KernCraftRecipes.parseAsFluid(nChildNode);
                    break;
                default:
                    System.err.println("Warning: Unrecognized element for Recipe.");
                    break;
            }
        }

        if (cost < 0) {
            System.err.println("Fatal: Negative cost for Extractor Recipe.");
            return;
        }

        KernCraftRecipes.CENTRIFUGE_RECIPES.add(
                new CentrifugeRecipe(inputs, outputs, fluid, energy, cost)
        );
    }

    @Override
    public int getCost() {
        return cost;
    }
}
