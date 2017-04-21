package com.R3DKn16h7.kerncraft.manual.data;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filippo on 21-Apr-17.
 */
public class ManualChapter {
    List<ManualEntry> manualEntryList = new ArrayList<>();
    String name = null;
    String unlocksWith = null;
    private int index;

    public ManualChapter(Node chapterNode, int index) {
        name = chapterNode.getAttributes().getNamedItem("name").getNodeValue();
        this.index = index;
        if (chapterNode.getAttributes().getNamedItem("unlockedWith") != null) {
            unlocksWith = chapterNode.getAttributes().getNamedItem("unlockedWith").getNodeValue();
        }

        NodeList nodeList = chapterNode.getChildNodes();
        int I = 0;
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNode.getNodeName().equals("Entry")) {
                //calls this method for all the children which is Element
                System.out.println(String.format("Reading entry \"%s\"", currentNode.getAttributes().getNamedItem("name").getNodeValue()));
                manualEntryList.add(new ManualEntry(currentNode, I++, this));
            }
        }
    }
}
