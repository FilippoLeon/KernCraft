package com.R3DKn16h7.kerncraft.manual.data;

import org.w3c.dom.Node;

/**
 * Created by Filippo on 21-Apr-17.
 */
public class ManualEntry {
    private int index;
    private ManualChapter chapter;

    public ManualEntry(Node entryNode, int index, ManualChapter chapter) {
        String name = entryNode.getAttributes().getNamedItem("name").getNodeValue();
        this.index = index;
        this.chapter = chapter;
    }
}
