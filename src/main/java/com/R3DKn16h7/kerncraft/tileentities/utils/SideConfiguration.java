package com.R3DKn16h7.kerncraft.tileentities.utils;

import com.R3DKn16h7.kerncraft.tileentities.ISideConfigurable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by Filippo on 18-Apr-17.
 */
public class SideConfiguration {

    public static final int NUMBER_OF_SIDES = 6;
    public ConfigurableItemHandler[] configurableItemHandler;
    private int size;
    private int[] inputSides;
    private int[] outputSides;

    public SideConfiguration(ItemStackHandler inputItemHandler,
                             ItemStackHandler outputItemHandler, ISideConfigurable te) {
        configurableItemHandler = new ConfigurableItemHandler[NUMBER_OF_SIDES];

        inputSides = new int[inputItemHandler.getSlots()];
        outputSides = new int[outputItemHandler.getSlots()];
        size = inputItemHandler.getSlots() + outputItemHandler.getSlots();

        for (int i = 0; i < NUMBER_OF_SIDES; ++i) {
            configurableItemHandler[i] = new ConfigurableItemHandler(inputItemHandler, outputItemHandler, te);
            configurableItemHandler[i].setFromArray(inputSides, outputSides, i);
        }
    }

    public int getSize() {
        return size;
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

    public void setSlotSide(int slot_id, int side) {
        if (slot_id < inputSides.length) inputSides[slot_id] = side;
        else if (slot_id >= inputSides.length) outputSides[slot_id - inputSides.length] = side;
        else {
            System.out.println("Error, invalid slot!");
        }

        for (int i = 0; i < NUMBER_OF_SIDES; ++i) {
            configurableItemHandler[i].setFromArray(inputSides, outputSides, i);
        }
    }

    public EnumSide getSlotSide(int slot_id) {
        if (slot_id < inputSides.length) return EnumSide.fromInt(inputSides[slot_id]);
        else if (slot_id >= inputSides.length) return EnumSide.fromInt(outputSides[slot_id - inputSides.length]);
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

        for (int i = 0; i < NUMBER_OF_SIDES; ++i) {
            configurableItemHandler[i].setFromArray(inputSides, outputSides, i);
        }
    }

    public void recompute(ItemStackHandler input, ItemStackHandler output, ISideConfigurable te) {
        inputSides = new int[input.getSlots()];
        outputSides = new int[output.getSlots()];
        size = input.getSlots() + output.getSlots();

        for (int i = 0; i < NUMBER_OF_SIDES; ++i) {
            configurableItemHandler[i] = new ConfigurableItemHandler(input, output, te);
            configurableItemHandler[i].setFromArray(inputSides, outputSides, i);
        }
    }

    public enum EnumSide {
        Disabled(NUMBER_OF_SIDES),
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

        public static EnumSide fromInt(int i) {
            for (EnumSide b : EnumSide.values()) {
                if (b.getValue() == i) {
                    return b;
                }
            }
            return null;
        }

        public int getValue() {
            return value;
        }
    }
}
