package com.R3DKn16h7.kerncraft.capabilities.element;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by Filippo on 19-Apr-17.
 */
public class ElementContainerStorage
        implements Capability.IStorage<IElementContainer> {

    @Override
    public NBTBase writeNBT(Capability<IElementContainer> capability,
                            IElementContainer instance, EnumFacing side) {

        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<IElementContainer> capability,
                        IElementContainer instance, EnumFacing side, NBTBase nbt) {

        instance.deserializeNBT(((NBTTagCompound) nbt));

    }
}
