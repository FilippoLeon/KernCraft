package com.R3DKn16h7.kerncraft.tileentities.utils;

import com.google.common.collect.Multiset;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by filippo on 04-May-17.
 */
public class UpgradeHandler implements INBTSerializable<NBTTagCompound> {

    Multiset<Upgrade> container;

    public void add(Upgrade upgrade) {
        container.add(upgrade);
    }

    public int getCount(Upgrade upgrade) {
        return container.count(upgrade);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();

        for (Multiset.Entry<Upgrade> upg : container.entrySet()) {
            nbt.setInteger(upg.getElement().toString(), upg.getCount());
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        for (Upgrade upg : Upgrade.values()) {
            if (nbt.hasKey(upg.toString())) {
                container.setCount(upg, nbt.getInteger(upg.toString()));
            }
        }
    }
}
