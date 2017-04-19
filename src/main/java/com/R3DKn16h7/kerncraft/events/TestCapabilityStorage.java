package com.R3DKn16h7.kerncraft.events;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by Filippo on 19-Apr-17.
 */
public class TestCapabilityStorage implements Capability.IStorage<IExampleCapability> {

    @Override
    public NBTBase writeNBT(Capability<IExampleCapability> capability, IExampleCapability instance, EnumFacing side) {
        // return an NBT tag
//        NBTTagCompound nbt = new NBTTagCompound();
//        nbt.setBoolean("locked", instance.isContentLocked(1));
        return new NBTTagInt(1);
    }

    @Override
    public void readNBT(Capability<IExampleCapability> capability, IExampleCapability instance, EnumFacing side, NBTBase nbt) {
        // load from the NBT tag
//        if(nbt instanceof NBTTagCompound && ((NBTTagCompound) nbt).hasKey("locked")) {
//            instance.unlockContent(1, ((NBTTagCompound) nbt).getBoolean("locked"));
//        }
    }
}
