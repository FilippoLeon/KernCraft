package com.R3DKn16h7.kerncraft.manual.data;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filippo on 21-Apr-17.
 */
public class Manual implements IManualEntry {
    public List<IManualEntry> manualChapterList = new ArrayList<>();
    Document doc = null;

    public Manual(String fileName) {
        parseXml(fileName);

        System.out.println(getDescription("first_steps", "beginner_chemist"));
    }

    private void parseXml(String fileName) {
        try {
            System.out.println(String.format("Reading file = \"%s.xml\" for Manual", fileName));
            DataInputStream in = new DataInputStream(getClass().getClassLoader()
                    .getResourceAsStream(fileName));

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(in);

            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            int I = 0;
            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node currentNode = nodeList.item(i);
                if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNode.getNodeName().equals("Chapter")) {
                    //calls this method for all the children which is Element
                    System.out.println(String.format("Reading chapter \"%s\"",
                            currentNode.getAttributes().getNamedItem("name").getNodeValue()));
                    manualChapterList.add(new ManualChapter(currentNode, I++, this));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDescription(String chapter, String entry) {

        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList nodeList;
        try {
            XPathExpression expr = xpath.compile("/Manual/Chapter[@name='" + chapter + "']/Entry[@name='" + entry + "']");
            Object exprResult = expr.evaluate(doc, XPathConstants.NODESET);
            nodeList = (NodeList) exprResult;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        } else {
            return null;
        }
    }

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public int getNumberOfChilds() {
        return manualChapterList.size();
    }

    @Override
    public List<IManualEntry> getChilds() {
        return manualChapterList;
    }

    @Override
    public String getTitle() {
        return "Index";
    }

    @Override
    public IManualEntry getParent() {
        return null;
    }

    @Override
    public int getIndex() {
        return 0;
    }
}
