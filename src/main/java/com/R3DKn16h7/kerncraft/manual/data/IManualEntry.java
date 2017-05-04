package com.R3DKn16h7.kerncraft.manual.data;

import java.util.List;

/**
 * Created by Filippo on 21-Apr-17.
 */
public interface IManualEntry {
    boolean isLocked();

    int getNumberOfChilds();

    List<IManualEntry> getChilds();

    String getTitle();

    IManualEntry getParent();

    int getIndex();
}
