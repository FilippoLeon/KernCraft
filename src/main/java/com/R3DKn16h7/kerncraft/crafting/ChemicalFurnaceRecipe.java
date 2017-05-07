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
 * Represents the recipe for the extractor.
 */
public class ChemicalFurnaceRecipe implements ISmeltingRecipe {
    private static final int MAX_INPUTS = 4;
    // TODO: might want to switch to ArrayList to ease loop
    public List<ElementStack> inputs;
    public NonNullList<ItemStack> outputs;
    public int energy;
    public FluidStack fluid;
    public int cost;

    public ChemicalFurnaceRecipe(List<ElementStack> inputs,
                                 NonNullList<ItemStack> outputs,
                                 int energy, FluidStack fluid, int cost) {

        this.inputs = inputs;
        this.outputs = NonNullList.create();
        if (outputs != null) {
            if (outputs.size() > 0) this.outputs.add(outputs.get(0));
            if (outputs.size() > 1) this.outputs.add(outputs.get(1));
        }
        this.energy = energy;
        this.fluid = fluid;
        this.cost = cost;
    }

    static public void parseChemicalFurnaceRecipe(Node nNode) {
        Element nElement = ((Element) nNode);

        int energy = KernCraftRecipes.readAsIntOrDefault(nElement, "energy", 0);
        int cost = KernCraftRecipes.readAsIntOrDefault(nElement, "cost", 0);

        List<ElementStack> inputs = new ArrayList<ElementStack>();
        NonNullList<ItemStack> outputs = NonNullList.create();
        FluidStack fluid = null;

        NodeList nList = nNode.getChildNodes();
        for (int i = 0; i < nList.getLength(); ++i) {
            if (nList.item(i).getNodeType() != Node.ELEMENT_NODE) continue;

            Element nChildNode = ((Element) nList.item(i));
            switch (nChildNode.getNodeName()) {
                case "Input":
                    inputs = KernCraftRecipes.parseAsElementStackList(
                            nChildNode,
                            0, MAX_INPUTS
                    );
                    break;
                case "Output":
                    NodeList nChildListO = nChildNode.getElementsByTagName("*");
                    if (nChildListO.getLength() > 2) {
                        System.err.println("Warning: Too many output elements for Extractor Recipe.");
                    }
                    for (int j = 0; j < Math.min(nChildListO.getLength(), 2); j++) {
                        outputs.add(KernCraftRecipes.parseAsItemStack(((Element) nChildListO.item(j))));
                    }
                    break;
                case "Fluid":
                    if (fluid != null) {
                        System.err.println("Warning: Too many fluids for Extractor Recipe.");
                    }
                    fluid = KernCraftRecipes.parseAsFluid(nChildNode);
                    break;
                default:
                    System.err.println("Warning: Unrecognized element for Extractor Recipe.");
                    break;
            }
        }

        if (cost < 0) {
            System.err.println("Fatal: Negative cost for Extractor Recipe.");
            return;
        }

        KernCraftRecipes.CHEMICAL_FURNACE_RECIPES.add(
                new ChemicalFurnaceRecipe(inputs, outputs, energy, fluid, cost)
        );
    }

    @Override
    public int getCost() {
        return cost;
    }
}
