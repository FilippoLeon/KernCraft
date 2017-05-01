package com.R3DKn16h7.kerncraft.crafting;

import com.R3DKn16h7.kerncraft.blocks.KernCraftBlocks;
import com.R3DKn16h7.kerncraft.elements.ElementRegistry;
import com.R3DKn16h7.kerncraft.elements.ElementStack;
import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
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
    static public List<ExtractorRecipe> EXTRACTOR_RECIPES = new ArrayList<>();
    public static List<ChemicalFurnaceRecipe> CHEMICAL_FURNACE_RECIPES = new ArrayList<>();
    public static List<CentrifugeRecipe> CENTRIFUGE_RECIPES = new ArrayList<>();
    public static List<ElectrolyzerRecipe> ELECTROLYZER_RECIPES = new ArrayList<>();

    public KernCraftRecipes() {
        RegisterCraftingRecipes();

        parseRecipeXml();
    }

    static ItemStack parseAsItemStack(Element elem) {
        String nodeValue = elem.getTextContent();
        // TODO default if empty
        int amount = readAsIntOrDefault(elem, "amount", 1);
        int meta = readAsIntOrDefault(elem, "meta", 0);
        String nbt = readAsStringOrDefault(elem, "nbt", "");

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

    static FluidStack parseAsFluid(Element elem) {
        int amount = readAsIntOrDefault(elem, "amount", 1000);
        String fluid = elem.getTextContent();
        return new FluidStack(FluidRegistry.getFluid(fluid), amount);
    }

    static ElementStack parseAsElementStack(Element element) {

        int amount = readAsIntOrDefault(element, "amount", 1);
        float prob = readAsFloatOrDefault(element, "probability", 1.0f);

        int id = 188;
        String content = element.getTextContent();
        try {
            id = Integer.parseInt(content);
        } catch (Exception e) {
            id = ElementRegistry.symbolToId(content);
        }
        return new ElementStack(id, amount, prob);
    }

    private static float readAsFloatOrDefault(Element nElement,
                                              String key, float defaultValue) {
        if (nElement.hasAttribute(key)) {
            try {
                return Float.parseFloat(nElement.getAttribute(key));
            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
                e.printStackTrace();
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    static int readAsIntOrDefault(Element nElement,
                                  String key, int defaultValue) {
        String text;
        if (key == null) {
            text = nElement.getTextContent();
        } else if (nElement.hasAttribute(key)) {
            text = nElement.getAttribute(key);
        } else {
            return defaultValue;
        }
        try {
            return Integer.parseInt(text);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
            return defaultValue;
        }
    }


    private static String readAsStringOrDefault(Element nElement,
                                                String key, String defaultValue) {
        if (nElement.hasAttribute(key)) {
            return nElement.getAttribute(key);
        }
        return defaultValue;
    }

    public static NonNullList<ItemStack> parseAsItemStackList(Element nElement,
                                                              int minLength,
                                                              int maxLength) {
        NonNullList<ItemStack> itemStackList = NonNullList.create();

        NodeList nChildList = nElement.getElementsByTagName("*");
        if (nChildList.getLength() > maxLength) {
            System.err.println("Warning: Too many inputs for Recipe.");
        }
        int avail = nChildList.getLength();
        if (maxLength > 0) avail = Math.min(nChildList.getLength(), maxLength);
        for (int j = 0; j < avail; j++) {
            itemStackList.add(
                    KernCraftRecipes.parseAsItemStack((Element) nChildList.item(j))
            );
        }
        if (avail < minLength) {
            System.err.println("Warning: Too few inputs for Recipe.");
            for (int j = avail; j < minLength; j++) {
                itemStackList.add(
                        ItemStack.EMPTY
                );
            }
        }
        return itemStackList;
    }

    public static List<ElementStack> parseAsElementStackList(Element nElement,
                                                             int minLength,
                                                             int maxLength) {
        List<ElementStack> itemStackList = NonNullList.create();

        NodeList nChildList = nElement.getElementsByTagName("*");
        if (maxLength != -1 && nChildList.getLength() > maxLength) {
            System.err.println("Warning: Too many inputs for Recipe.");
        }
        int avail = nChildList.getLength();
        if (maxLength > 0) avail = Math.min(nChildList.getLength(), maxLength);
        for (int j = 0; j < avail; j++) {
            itemStackList.add(
                    KernCraftRecipes.parseAsElementStack((Element) nChildList.item(j))
            );
        }
        if (avail < minLength) {
            System.err.println("Warning: Too few inputs for Recipe.");
            for (int j = avail; j < minLength; j++) {
                itemStackList.add(null);
            }
        }
        return itemStackList;
    }

    private void RegisterCraftingRecipes() {
        //// TEST RECIPE
        GameRegistry.addRecipe(new ItemStack(KernCraftItems.PORTABLE_BEACON),
                "##", "##", '#', KernCraftBlocks.TEST_BLOCK);

        //// CANISTER RECIPE
        if (OreDictionary.doesOreNameExist("plateIron") && OreDictionary.doesOreNameExist("rodIron")) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(KernCraftItems.CANISTER, 16),
                    new Object[]{
                            " # ",
                            "# #",
                            "i#i",
                            '#', "plateIron", 'i', "rodIron"
                    }
            ));
        } else {
            GameRegistry.addShapedRecipe(new ItemStack(KernCraftItems.CANISTER, 16),
                    " # ",
                    "# #",
                    "i#i",
                    '#', Items.IRON_INGOT, 'i', Blocks.IRON_BARS);
        }
        //// ERLENMEYER FLASK
        GameRegistry.addShapedRecipe(new ItemStack(KernCraftItems.FLASK, 8),
                " # ",
                " # ",
                "###",
                '#', Blocks.GLASS_PANE);
        //// CELL RECIPE
        if (OreDictionary.doesOreNameExist("plateIron") && OreDictionary.doesOreNameExist("rodIron")) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(KernCraftItems.PRESSURIZED_CELL, 16),
                    new Object[]{
                            " i ",
                            " # ",
                            " # ",
                            '#', "plateIron", 'i', "rodIron"
                    }
            ));
        } else {
            GameRegistry.addShapedRecipe(new ItemStack(KernCraftItems.PRESSURIZED_CELL, 16),
                    " i ",
                    " # ",
                    " # ",
                    '#', Items.IRON_INGOT, 'i', Blocks.IRON_BARS);
        }

        //// POTATO BATTERY RECIPE
        if (OreDictionary.doesOreNameExist("rodZinc") && OreDictionary.doesOreNameExist("rodCopper")) {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(KernCraftItems.POTATO_BATTERY),
                    new Object[]{Items.POISONOUS_POTATO, "rodZinc", "rodCopper"}
            ));
        } else if (OreDictionary.doesOreNameExist("ingotZinc") && OreDictionary.doesOreNameExist("ingotCopper")) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(KernCraftItems.POTATO_BATTERY),
                    new Object[]{Items.POISONOUS_POTATO, "ingotZinc", "ingotCopper"}
            ));
        } else {
            GameRegistry.addShapedRecipe(new ItemStack(KernCraftItems.POTATO_BATTERY),
                    Items.POISONOUS_POTATO, Items.GOLD_INGOT, Items.IRON_INGOT);
        }

        //// ELECTROLYTIC CELL
        if (OreDictionary.doesOreNameExist("wireCopper") &&
                OreDictionary.doesOreNameExist("rodZinc") &&
                OreDictionary.doesOreNameExist("rodCopper")) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(KernCraftItems.ELECTROLYTIC_CELL),
                    new Object[]{
                            " P ",
                            "c c",
                            "ZWC",
                            'W', new ItemStack(Items.POTIONITEM, 1),
                            'c', "wireCopper",
                            'C', "rodCopper",
                            'Z', "rodZinc",
                            'P', KernCraftItems.POTATO_BATTERY
                    }
            ));
        } else {
            GameRegistry.addRecipe(new ItemStack(KernCraftItems.ELECTROLYTIC_CELL),
                    " P ",
                    "c c",
                    "ZWC",
                    'W', PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM, 1), PotionType.getPotionTypeForName("water")),
                    'c', Blocks.IRON_BARS,
                    'C', Items.GOLD_INGOT,
                    'Z', Items.IRON_INGOT,
                    'P', KernCraftItems.POTATO_BATTERY);
        }

        //// CRAFTING WITH ELEMENTS, new RECIPE
        ItemStack canister = new ItemStack(KernCraftItems.CANISTER, 1);
        //IRecipe
        GameRegistry.addShapelessRecipe(
                new ItemStack(KernCraftItems.TYROCINIUM_CHYMICUM),
                Items.BOOK,
                canister
        );

        GameRegistry.addRecipe(new ElementRecipe(
                new ItemStack(KernCraftItems.TYROCINIUM_CHYMICUM),
                new Object[]{
                        "BOH",
                        "   ",
                        "HI ",
                        'B', Items.WATER_BUCKET,
                        'O', new ElementStack("O", 4),
                        'H', new ElementStack("Fe", 15),
                        'I', Items.IRON_INGOT
                }
        ));
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
            switch (nNode.getNodeName()) {
                case "Extractor":
                    ExtractorRecipe.parseExtractorRecipe(nNode);
                    break;
                case "ChemicalFurnace":
                    ChemicalFurnaceRecipe.parseChemicalFurnaceRecipe(nNode);
                    break;
                case "Electrolyzer":
                    ElectrolyzerRecipe.parseElectrolyzerRecipe(nNode);
                    break;
                case "Centrifuge":
                    CentrifugeRecipe.parseCentrifugeRecipe(nNode);
                    break;
                default:
                    System.out.println(
                            String.format("Skipping unknown recipe '%s'",
                                    nNode.getNodeName()
                            )
                    );
                    break;
            }
        }
    }
}
