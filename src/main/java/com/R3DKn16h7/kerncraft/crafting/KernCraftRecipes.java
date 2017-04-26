package com.R3DKn16h7.kerncraft.crafting;

import com.R3DKn16h7.kerncraft.blocks.KernCraftBlocks;
import com.R3DKn16h7.kerncraft.elements.ElementRegistry;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filippo on 20-Apr-17.
 */
public class KernCraftRecipes {
    // Static register of recipes
    static public List<ISmeltingRecipe> EXTRACTOR_RECIPES = new ArrayList<>();
    public static List<ISmeltingRecipe> CHEMICAL_FURNACE_RECIPES = new ArrayList<>();

    public KernCraftRecipes() {
        RegisterCraftingRecipes();
        RegisterExtractorRecipes();
        RegisterChemicalFurnaceRecipes();

        parseRecipeXml();
    }

    /**
     * Add new recipe
     *
     * @param item     Principal item that will allow to extract elements.
     * @param catalyst Additional item (optional) that acts as Catalyst
     * @param outs     LAB_BONNET list of output Elements
     * @param energy   The required energy (TODO: change to "cost") to perform a smelting operation.
     * @return Return false if registration failed (TODO)
     */
    static public boolean addExtractorRecipe(Item item, Item catalyst,
                                             ElementStack[] outs, int energy) {
        EXTRACTOR_RECIPES.add(
                new ExtractorRecipe(item, catalyst, outs, energy)
        );
        return true;
    }

    static public boolean addChemicalFurnaceRecipe(ElementStack[] inputs,
                                                   NonNullList<ItemStack> outputs,
                                                   int energy, FluidStack fluid,
                                                   int cost) {
        CHEMICAL_FURNACE_RECIPES.add(
                new ChemicalFurnaceRecipe(inputs, outputs, energy, fluid, cost)
        );
        return true;
    }

    private static ItemStack parseAsItemStack(Element elem) {
        String nodeValue = elem.getTextContent();
        // TODO default if empty
        int amount;
        try {
            amount = Integer.parseInt(elem.getAttributes().getNamedItem("amount").getNodeValue());
        } catch (Exception e) {
            amount = 1;
        }
        int meta;
        try {
            meta = Integer.parseInt(elem.getAttributes().getNamedItem("meta").getNodeValue());
        } catch (Exception e) {
            meta = 0;
        }
        String nbt;
        try {
            nbt = elem.getAttributes().getNamedItem("nbt").getNodeValue();
        } catch (Exception e) {
            nbt = "";
        }

        switch (elem.getNodeName()) {
            case "Item":
                return GameRegistry.makeItemStack(nodeValue, meta, amount, nbt);
            case "Block":
                return new ItemStack(Block.getBlockFromName(nodeValue), amount, meta);
            case "OreDict":
                if (OreDictionary.getOres(nodeValue).size() > 0) {
                    return OreDictionary.getOres(nodeValue).get(0);
                }
            default:
                return ItemStack.EMPTY;
        }
    }

    private static FluidStack parseAsFluid(Element elem) {
        int amount;
        try {
            amount = Integer.parseInt(elem.getAttributes().getNamedItem("amount").getNodeValue());
        } catch (NullPointerException e) {
            amount = 1000;
        }
        String fluid = elem.getTextContent();
        return new FluidStack(FluidRegistry.getFluid(fluid), amount);

    }

    private static ElementStack parseAsElementStack(Element element) {
        int amount;
        try {
            amount = Integer.parseInt(element.getAttributes().getNamedItem("amount").getNodeValue());
        } catch (Exception e) {
            amount = 1;
        }
        float prob;
        try {
            prob = Float.parseFloat(element.getAttributes().getNamedItem("probability").getNodeValue());
        } catch (Exception e) {
            prob = 1.0f;
        }

        int id;
        try {
            id = Integer.parseInt(element.getNodeValue());
        } catch (Exception e) {
            id = ElementRegistry.symbolToId(element.getTextContent());
        }
        return new ElementStack(id, amount, prob);
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
                    input = parseAsItemStack(((Element) nChildNode.getElementsByTagName("*").item(0)));
                    break;
                case "Catalyst":
                    if (!catalyst.isEmpty()) {
                        System.err.println("Warning: Too many catalysts for Extractor Recipe.");
                    }
                    catalyst = parseAsItemStack(((Element) nChildNode.getElementsByTagName("*").item(0)));
                    break;
                case "Output":
                    NodeList nChildList = nChildNode.getElementsByTagName("*");
                    if (nChildList.getLength() > 4) {
                        System.err.println("Warning: Too many output elements for Extractor Recipe.");
                    }
                    for (int j = 0; j < Math.min(nChildList.getLength(), 4); j++) {
                        elements[j] = parseAsElementStack(((Element) nChildList.item(j)));
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

        EXTRACTOR_RECIPES.add(
                new ExtractorRecipe(input.getItem(), catalyst.getItem(), elements, energy)
        );
    }

    static public void parseChemicalFurnaceRecipe(Node nNode) {
        int energy;
        try {
            energy = Integer.parseInt(nNode.getAttributes().getNamedItem("energy").getNodeValue());
        } catch (Exception e) {
            energy = 0;
        }
        int cost;
        try {
            cost = Integer.parseInt(nNode.getAttributes().getNamedItem("cost").getNodeValue());
        } catch (Exception e) {
            cost = 0;
        }
        ElementStack[] inputs = new ElementStack[2];
        NonNullList<ItemStack> outputs = NonNullList.create();
        FluidStack fluid = null;

        NodeList nList = nNode.getChildNodes();
        for (int i = 0; i < nList.getLength(); ++i) {
            if (nList.item(i).getNodeType() != Node.ELEMENT_NODE) continue;

            Element nChildNode = ((Element) nList.item(i));
            switch (nChildNode.getNodeName()) {
                case "Input":
                    NodeList nChildList = nChildNode.getElementsByTagName("*");
                    if (nChildList.getLength() > 2) {
                        System.err.println("Warning: Too many inputs elements for Extractor Recipe.");
                    }
                    for (int j = 0; j < Math.min(nChildList.getLength(), 2); j++) {
                        inputs[j] = parseAsElementStack(((Element) nChildList.item(j)));
                    }
                    break;
                case "Output":
                    NodeList nChildListO = nChildNode.getElementsByTagName("*");
                    if (nChildListO.getLength() > 2) {
                        System.err.println("Warning: Too many output elements for Extractor Recipe.");
                    }
                    for (int j = 0; j < Math.min(nChildListO.getLength(), 2); j++) {
                        outputs.add(parseAsItemStack(((Element) nChildListO.item(j))));
                    }
                    break;
                case "Fluid":
                    if (fluid != null) {
                        System.err.println("Warning: Too many fluids for Extractor Recipe.");
                    }
                    fluid = parseAsFluid(nChildNode);
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

        CHEMICAL_FURNACE_RECIPES.add(
                new ChemicalFurnaceRecipe(inputs, outputs, energy, fluid, cost)
        );
    }

    private void RegisterCraftingRecipes() {

        GameRegistry.addRecipe(new ItemStack(KernCraftItems.PORTABLE_BEACON),
                "##", "##", '#', KernCraftBlocks.TEST_BLOCK);

        GameRegistry.addShapedRecipe(new ItemStack(KernCraftItems.CANISTER),
                "#N#", "N N", "#N#", '#', Items.IRON_INGOT, 'N', Items.IRON_INGOT);
        GameRegistry.addShapedRecipe(new ItemStack(KernCraftItems.POTATO_BATTERY),
                "i  ",
                "P- ",
                "   ",
                'P', Items.POISONOUS_POTATO, 'i', Items.GOLD_INGOT, '-', Items.IRON_INGOT);
        GameRegistry.addShapedRecipe(new ItemStack(KernCraftItems.ELECTROLYTIC_CELL),
                "WiW",
                " iB",
                "ppp",
                'W', Items.GLASS_BOTTLE, 'i', Blocks.IRON_BARS,
                'p', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'B', KernCraftItems.POTATO_BATTERY);

        ItemStack canister = new ItemStack(KernCraftItems.CANISTER, 1);
        NBTTagCompound comp = new NBTTagCompound();
        comp.setInteger("Element", 1);
        canister.writeToNBT(comp);
        //IRecipe
        GameRegistry.addShapelessRecipe(
                new ItemStack(KernCraftItems.TYROCINIUM_CHYMICUM),
                Items.BOOK,
                canister
        );

        int[] elements = {ElementRecipe.ANY_ELEMENT};
        int[] quantities = {0};
        NonNullList<ItemStack> ing = NonNullList.create();
        ing.add(new ItemStack(Items.BOOK));
//        CraftingManager.getInstance().addRecipe(new ElementRecipe(KernCraftItems.TYROCINIUM_CHYMICUM,
//                ing, elements, quantities));
    }

    private void RegisterExtractorRecipes() {
        addExtractorRecipe(Item.getItemFromBlock(Blocks.IRON_ORE),
                Item.getItemFromBlock(Blocks.ANVIL),
                new ElementStack[]{
                        new ElementStack(1, 1),
                        new ElementStack(4, 1)
                }, 10);
        addExtractorRecipe(Item.getItemFromBlock(Blocks.DIAMOND_ORE),
                Item.getItemFromBlock(Blocks.LAPIS_BLOCK),
                new ElementStack[]{
                        new ElementStack(1, 1),
                        new ElementStack(5, 5)
                }, 10);
        addExtractorRecipe(Item.getItemFromBlock(Blocks.DIAMOND_BLOCK),
                null,
                new ElementStack[]{
                        new ElementStack("Be", 4, 0.5f),
                        new ElementStack("Ar", 5)
                },
                10);
        addExtractorRecipe(Item.getItemFromBlock(Blocks.IRON_BLOCK),
                null,
                new ElementStack[]{
                        new ElementStack("Pu", 400, 0.5f),
                        new ElementStack("Pm", 5),
                        new ElementStack("H", 1, 0.8f),
                        new ElementStack("He", 2)
                },
                10);
    }

    private void RegisterChemicalFurnaceRecipes() {
        addChemicalFurnaceRecipe(
                new ElementStack[]{
                        new ElementStack("H", 4, 0),
                        new ElementStack("O", 2, 0),
                },
                null,
                100,
                new FluidStack(FluidRegistry.WATER, 1000),
                5
        );
    }

    public void parseRecipeXml() {
        Document doc;
        try {
            String fileName = "assets/kerncraft/config/recipes.xml";
            DataInputStream in = new DataInputStream(getClass().getClassLoader()
                    .getResourceAsStream(fileName));

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(in);

            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        NodeList nList = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < nList.getLength(); ++i) {
            if (nList.item(i).getNodeType() != Node.ELEMENT_NODE) continue;

            Node nNode = nList.item(i);
            try {
                switch (nNode.getNodeName()) {
                    case "Extractor":
                        parseExtractorRecipe(nNode);
                        break;
                    case "ChemicalFurnace":
                        parseChemicalFurnaceRecipe(nNode);
                        break;
                    default:
                        System.out.println(
                                String.format("Skipping unknown recipe '%s'",
                                        nNode.getNodeName()
                                )
                        );
                        break;
                }
            } catch (Exception e) {
                continue;
            }
        }
    }
}
