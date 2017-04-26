package com.R3DKn16h7.kerncraft.elements;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.awt.*;
import java.io.DataInputStream;

/**
 * Created by Filippo on 20-Apr-17.
 *
 * Represent a single element. Contains all the information of a single
 * element of the Periodic Table, including all details such as group, mass,
 * (short) shortDescription, color, ...
 *
 * This is instantiated only once and then it is just retrieved and
 * used. Never modified.
 *
 * TODO: support for shortDescription and name localization
 */
public class Element {
    /**
     * Unique identifier, also known as atomic number.
     */
    public final int id;
    /**
     * Short symbol in the form H, He, ..
     */
    public final String symbol;
    /**
     * Unlocalized name, also registry name and unique identifier.
     */
    public final String name;
    public final int group;
    public final int period;
    public final float mass;
    public final float density;
    public final float radius;
    public final float conductivity;
    public final float specificHeat;
    public final float thermalConductivity;
    public final String color;
    public final boolean toxic;
    /**
     * State at room temperature.
     */
    public final State state;
    public final Family family;
    /**
     * Denotes the half life of the element. A value of -1 indicates
     * that the element is stable.
     */
    public final float halfLife;
    /**
     * A very short description of the element. The length shall
     * not bee to big. Use the Xml representation to obatin the
     * long, localized description.
     * <p>
     * TODO: probably
     */
    public final String shortDescription;
    /**
     * Amount at which the element does: Kaboooooooooom! The value
     * -1 indicates a stable element.
     */
    private final int criticalMass;

    Element(int atomicNumber, JsonObject jsonObject) {
        this.id = atomicNumber;
        this.name = jsonObject.get("english").getAsString();
        this.symbol = jsonObject.get("symbol").getAsString();

        String gr = jsonObject.get("group").getAsString();
        switch (gr) {
            case "A":
                this.group = Group.ACTINIDE.getValue();
                break;
            case "L":
                this.group = Group.LANTHANIDE.getValue();
                break;
            default:
                this.group = tryGetAsIntOrDefault("group", 1, jsonObject);
                break;
        }
        this.family = Family.fromString(
                jsonObject.get("family").getAsString()
        );
        this.period = tryGetAsIntOrDefault("period", 0, jsonObject);

        // TODO: remove error if key not present and mark element with no data as mystery property
        this.mass = tryGetAsFloatOrDefault("mass", 1, jsonObject);
        this.density = tryGetAsFloatOrDefault("density", 1, jsonObject);
        this.radius = tryGetAsFloatOrDefault("radius", 1, jsonObject);
        this.conductivity = tryGetAsFloatOrDefault("conductivity", 1, jsonObject);
        this.thermalConductivity = tryGetAsFloatOrDefault("thermalConductivity", 1, jsonObject);
        this.specificHeat = tryGetAsFloatOrDefault("specificHeat", 1, jsonObject);
        this.halfLife = tryGetAsFloatOrDefault("halfLife", -1, jsonObject, false);
        this.criticalMass = tryGetAsIntOrDefault("criticalMass", -1, jsonObject, false);

        color = jsonObject.get("color").getAsString();

        this.toxic = tryGetAsBoolOrDefault("toxic", false, jsonObject);

        state = State.fromString(jsonObject.get("state").getAsString());

        // TODO: Use i18n for this
        if (jsonObject.has("description")) {
            shortDescription = jsonObject.get("description").getAsString();
        } else {
            System.out.println(String.format("No description for element '%s'", name));
            shortDescription = null;
        }
    }

    private static float tryGetAsFloatOrDefault(String key, float defaultValue,
                                                JsonObject jsonObject) {
        return tryGetAsFloatOrDefault(key, defaultValue, jsonObject, true);
    }

    private static float tryGetAsFloatOrDefault(String key, float defaultValue,
                                                JsonObject jsonObject, boolean warnIfError) {
        try {
            return jsonObject.get(key).getAsFloat();
        } catch (RuntimeException e) {
            if (warnIfError) {
                System.err.println(
                        String.format("Error parsing Element Json, string '%s' " +
                                        "of key '%s' not convertible to float.",
                                jsonObject.get(key), key
                        )
                );
            }
            return defaultValue;
        }
    }

    private static int tryGetAsIntOrDefault(String key, int defaultValue,
                                            JsonObject jsonObject) {
        return tryGetAsIntOrDefault(key, defaultValue, jsonObject, true);
    }

    private static int tryGetAsIntOrDefault(String key, int defaultValue,
                                            JsonObject jsonObject, boolean warnIfError) {
        try {
            return jsonObject.get(key).getAsInt();
        } catch (RuntimeException e) {
            if (warnIfError) {
                System.err.println(
                        String.format("Error parsing Element Json, string '%s' " +
                                        "of key '%s' not convertible to int.",
                                jsonObject.get(key), key
                        )
                );
            }
            return defaultValue;
        }
    }

    private boolean tryGetAsBoolOrDefault(String key, boolean defaultValue,
                                          JsonObject jsonObject) {
        try {
            return jsonObject.get(key).getAsBoolean();
        } catch (RuntimeException e) {
            System.err.println(
                    String.format("Error parsing Element Json, string '%s' " +
                                    "of key '%s' not convertible to bool.",
                            jsonObject.get(key), key
                    )
            );
            return defaultValue;
        }
    }

    /**
     * Given the element, read the xml file containing the <b>long</b>
     * descriptions and return the parsed string.
     *
     * @return
     */
    public String loadDescription() {
        // TODO: move this string somewhere else?
        String fileName = "assets/kerncraft/config/elements_descriptions.xml";
        System.out.println(
                String.format("Reading file = \"%s.xml\" for Element " +
                                "%s description.",
                        fileName, this.name
                )
        );
        try {
            DataInputStream in = new DataInputStream(
                    getClass().getClassLoader().getResourceAsStream(fileName)
            );

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(in);

            String lang = Minecraft.getMinecraft().gameSettings.language;

            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile(
                    "//Element[@name='" + this.name + "']/Description[@lang='" + lang + "']"
            );

            NodeList nodeList = (NodeList) expr.evaluate(doc.getDocumentElement(),
                    XPathConstants.NODESET
            );
            if (nodeList.getLength() > 0) {
                return nodeList.item(0).getTextContent();
            } else {
                return "";
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return "";
    }

    public String getLocalizedName() {
        return name;
    }

    /**
     * Converts symbol and room temperature state to a pretty colorized string.
     *
     * @return colorized symbol string.
     */
    public String toSymbol() {
        return state.toColor() + symbol + TextFormatting.RESET.toString();
    }

    /**
     * True if element with given amount surpasses critical mass and goes
     * Kabooooom.
     *
     * @param quantity amount of element to check.
     * @return does quantity reach the critical mass of @this
     */
    public boolean reachedCriticalMass(int quantity) {
        return criticalMass > 0 && quantity > criticalMass;
    }

    /**
     * Enum class converting Group names into Enums for easy use.
     */
    public enum Group {
        ACTINIDE(-2),
        LANTHANIDE(-3);

        private int value;

        Group(int value) {
            this.value = value;
        }

        public static String toString(int value) {
            switch (value) {
                case -2:
                    return "A";
                case -3:
                    return "L";
                default:
                    return Integer.toString(value);
            }
        }

        public String toString() {
            return toString(value);
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Enum class for easy manipulation of element state.
     */
    public enum State {
        GAS, LIQUID, SOLID, UNKNOWN;

        public static State fromString(String s) {
            switch (s) {
                case "g":
                    return GAS;
                case "l":
                    return LIQUID;
                case "s":
                    return SOLID;
                default:
                    return UNKNOWN;
            }
        }

        public String toColor() {
            switch (this) {
                case GAS:
                    return TextFormatting.AQUA.toString();
                case LIQUID:
                    return TextFormatting.BLUE.toString();
                case SOLID:
                    return TextFormatting.RESET.toString();
                case UNKNOWN:
                    return TextFormatting.GRAY.toString();
                default:
                    return "";
            }
        }
    }

    public enum Family {
        ALKALI, ALKALINE, BASIC_METAL, SEMIMETAL, NON_METAL, HALOGEN, NOBLE_GAS, TRANSITION,
        ACTINIDES, LANTHANIDES, UNKNOWN;

        public static Family fromString(String s) {
            switch (s) {
                case "Alkali":
                    return ALKALI;
                case "Alkaline":
                    return ALKALINE;
                case "BasicMetal":
                    return BASIC_METAL;
                case "Semimetal":
                    return SEMIMETAL;
                case "NonMetal":
                    return NON_METAL;
                case "Halogen":
                    return HALOGEN;
                case "Noble":
                    return NOBLE_GAS;
                case "Transition":
                    return TRANSITION;
                case "L":
                    return LANTHANIDES;
                case "A":
                    return ACTINIDES;
                default:
                    return UNKNOWN;
            }
        }

        public String I18n() {
            return I18n.format(String.format("element.family.%s.name", this));
        }

        public Color toColor() {
            switch (this) {
                case ALKALI:
                    return new Color(255, 0, 0);
                case ALKALINE:
                    return new Color(240, 150, 0);
                case TRANSITION:
                    return new Color(250, 203, 40);
                case BASIC_METAL:
                    return new Color(100, 255, 0);
                case SEMIMETAL:
                    return new Color(0, 155, 135);
                case NON_METAL:
                    return new Color(0, 105, 135);
                case HALOGEN:
                    return new Color(100, 55, 235);
                case NOBLE_GAS:
                    return new Color(140, 0, 235);
                case LANTHANIDES:
                    return new Color(0, 205, 0);
                case ACTINIDES:
                    return new Color(0, 100, 0);
                case UNKNOWN:
                    return new Color(14, 14, 14);
                default:
                    return null;
            }
        }
    }
}
