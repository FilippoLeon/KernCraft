package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.tileentities.utils.Upgrade;
import com.R3DKn16h7.kerncraft.tileentities.utils.UpgradeHandler;

/**
 * Created by filippo on 04-May-17.
 */
public interface IUpgradeable {

    UpgradeHandler getUpgrade();

    int getInputSize();

    int getOutputSize();

    void AddUpgrade(Upgrade upgrade);
}
