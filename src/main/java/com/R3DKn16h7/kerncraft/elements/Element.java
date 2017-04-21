package com.R3DKn16h7.kerncraft.elements;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.DataInputStream;

/**
 * Created by Filippo on 20-Apr-17.
 *
 * Represent a single element.
 */
public class Element {
    public int id;
    public String symbol;
    public String register_name;
    public String name;
    public int group;
    public int period;
    public float mass;
    public float density;
    public float radius;
    public float conductivity;
    public float sp_heat;
    public float th_conductivity;
    public String color;
    public boolean toxic;
    public ElementBase.ElementState state;
    public ElementBase.ElementFamily family;
    public float half_life;
    public String description;

    Element(int id_, JsonObject js) {
        // TODO: remove try
        id = id_;
        symbol = js.get("symbol").getAsString();
        try {
            mass = js.get("mass").getAsFloat();
        } catch (RuntimeException e) {
            density = 0;
        }

        String gr = js.get("group").getAsString();
        if (gr.equals("A")) {
            group = ElementBase.GROUP.Actinide.getValue();
        } else if (gr.equals("L")) {
            group = ElementBase.GROUP.Lanthanide.getValue();
        } else {
            try {
                group = js.get("group").getAsInt();
            } catch (RuntimeException e) {

            }
        }

        family = ElementBase.ElementFamily.fromString(js.get("family").getAsString());

        period = js.get("period").getAsInt();
        name = js.get("english").getAsString();
        try {
            density = js.get("density").getAsFloat();
        } catch (RuntimeException e) {
            density = 0;
        }
        try {
            radius = js.get("radius").getAsFloat();
        } catch (RuntimeException e) {
            radius = 0;
        }
        try {
            conductivity = js.get("conductivity").getAsFloat();
        } catch (RuntimeException e) {
            conductivity = 0;
        }

        try {
            th_conductivity = js.get("th_conductivity").getAsFloat();
        } catch (RuntimeException e) {
            th_conductivity = 0;
        }
        try {
            sp_heat = js.get("sp_heat").getAsFloat();
        } catch (RuntimeException e) {
            sp_heat = 0;
        }
        color = js.get("color").getAsString();
        try {
            toxic = js.get("toxic").getAsBoolean();
        } catch (RuntimeException e) {
            toxic = false;
        }

        state = ElementBase.ElementState.fromString(js.get("state").getAsString());

        try {
            half_life = js.get("half_life").getAsFloat();
        } catch (RuntimeException e) {
            half_life = 0;
        }

        try {
            description = js.get("description").getAsString();
        } catch (RuntimeException e) {

        }

    }

    public String loadDescription() {
        try {
            String fname = "assets/kerncraft/config/elements_descriptions.xml";
            System.out.println(String.format("Reading file = \"%s.xml\" for Element %s description",
                    fname, this.name)
            );
            DataInputStream in = new DataInputStream(getClass().getClassLoader()
                    .getResourceAsStream(fname));


            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(in);
            doc.getDocumentElement().normalize();

            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile("//Element[@id='" + this.id + "']");
            Object exprResult = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodeList = (NodeList) exprResult;
            if (nodeList.getLength() > 0) {
                String lang = Minecraft.getMinecraft().gameSettings.language;

                expr = xpath.compile("//Description[@lang='" + lang + "']");
                exprResult = expr.evaluate(nodeList.item(0), XPathConstants.NODESET);
                NodeList nodeList2 = (NodeList) exprResult;
                if (nodeList2.getLength() > 0) {

                    return nodeList2.item(0).getTextContent();
                } else {
                    return "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getLocalizedName() {
        return name;
    }
}
