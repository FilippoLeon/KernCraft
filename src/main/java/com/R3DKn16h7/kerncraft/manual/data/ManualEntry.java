package com.R3DKn16h7.kerncraft.manual.data;

import org.w3c.dom.Node;

import java.util.List;

/**
 * Created by Filippo on 21-Apr-17.
 */
public class ManualEntry implements IManualEntry {
    public ManualChapter chapter;
    public String name;
    private int index;

    public ManualEntry(Node entryNode, int index, ManualChapter chapter) {
        name = entryNode.getAttributes().getNamedItem("name").getNodeValue();
        this.index = index;
        this.chapter = chapter;
    }

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public int getNumberOfChilds() {
        return 0;
    }

    @Override
    public List<IManualEntry> getChilds() {
        return null;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public IManualEntry getParent() {
        return chapter;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
