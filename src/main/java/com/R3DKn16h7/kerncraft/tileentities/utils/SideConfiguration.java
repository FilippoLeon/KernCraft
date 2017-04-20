package com.R3DKn16h7.kerncraft.tileentities.utils;

import com.R3DKn16h7.kerncraft.tileentities.ISideConfigurable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by Filippo on 18-Apr-17.
 */
public class SideConfiguration {

    private int[] inputSides;
    private int[] outputSides;
    public ConfigurableItemHandler[] configurableItemHandler;

    public enum EnumSide {
        Disabled(6),
        Front(0),
        Right(1),
        Back(2),
        Left(3),
        Top(4),
        Bottom(5);

        private final int value;

        EnumSide(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static EnumSide fromInt(int i) {
            for (EnumSide b : EnumSide.values()) {
                if (b.getValue() == i) {
                    return b;
                }
            }
            return null;
        }
    }

    // TODO: use entity facing to obtain correct positioning
    public ConfigurableItemHandler get(EnumFacing facing, EnumFacing tileEntityFacing) {
        int horizontalSide = (facing.getHorizontalIndex() + tileEntityFacing.getHorizontalIndex()) % 4;
//        facing.to
        switch (facing) {
            case DOWN:
                return configurableItemHandler[EnumSide.Bottom.getValue()];
            case UP:
                return configurableItemHandler[EnumSide.Top.getValue()];
            default:
                return configurableItemHandler[horizontalSide];
        }
    }

    public SideConfiguration(ItemStackHandler inputItemHandler,
                             ItemStackHandler outputItemHandler, ISideConfigurable te) {
        configurableItemHandler = new ConfigurableItemHandler[6];

        inputSides = new int[inputItemHandler.getSlots()];
        outputSides = new int[outputItemHandler.getSlots()];

        for(int i = 0; i < 6; ++i) {
            configurableItemHandler[i] = new ConfigurableItemHandler(inputItemHandler, outputItemHandler, te);
            configurableItemHandler[i].setFromArray(inputSides, outputSides, i);
        }
    }

    public void setSlotSide(int slot_id, int side) {
        if(slot_id < inputSides.length) inputSides[slot_id] = side;
        else if(slot_id >= inputSides.length) outputSides[slot_id - inputSides.length] = side;
        else {
            System.out.println("Error, invalid slot!");
        }

        for(int i = 0; i < 6; ++i) {
            configurableItemHandler[i].setFromArray(inputSides, outputSides, i);
        }
    }

    public EnumSide getSlotSide(int slot_id) {
        if(slot_id < inputSides.length) return EnumSide.fromInt( inputSides[slot_id] );
        else if(slot_id >= inputSides.length) return EnumSide.fromInt( outputSides[slot_id - inputSides.length] );
        else {
            System.out.println("Error, invalid slot!");
            return EnumSide.Disabled;
        }
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setIntArray("inputSides", inputSides);
        nbt.setIntArray("outputSides", outputSides);
        return nbt;
    }

    public void deserializeNBT(NBTTagCompound nbt) {
        inputSides = nbt.getIntArray("inputSides");
        outputSides = nbt.getIntArray("outputSides");

        for(int i = 0; i < 6; ++i) {
            configurableItemHandler[i].setFromArray(inputSides, outputSides, i);
        }
    }
}
