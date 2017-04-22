package com.R3DKn16h7.kerncraft.manual.data;

import com.R3DKn16h7.kerncraft.capabilities.ITyrociniumProgressCapability;
import com.R3DKn16h7.kerncraft.capabilities.TyrociniumProgressDefaultCapability;
import net.minecraft.client.Minecraft;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filippo on 21-Apr-17.
 */
public class ManualChapter implements IManualEntry {
    private final Manual parent;
    public String name = null;
    List<IManualEntry> manualEntryList = new ArrayList<>();
    String unlocksWith = null;
    private int index;

    public ManualChapter(Node chapterNode, int index, Manual parent) {
        this.parent = parent;
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

    @Override
    public boolean isLocked() {
        if (unlocksWith != null) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player.hasCapability(TyrociniumProgressDefaultCapability.INSTANCE, null)) {
                ITyrociniumProgressCapability capability = mc.player.getCapability(TyrociniumProgressDefaultCapability.INSTANCE, null);
                return !capability.isContentUnlocked(unlocksWith);
            }
        }
        return false;
    }

    @Override
    public int getNumberOfChilds() {
        return manualEntryList.size();
    }

    @Override
    public List<IManualEntry> getChilds() {
        return manualEntryList;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public IManualEntry getParent() {
        return parent;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
