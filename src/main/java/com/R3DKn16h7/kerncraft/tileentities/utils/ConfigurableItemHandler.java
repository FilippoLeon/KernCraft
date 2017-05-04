package com.R3DKn16h7.kerncraft.tileentities.utils;

import com.R3DKn16h7.kerncraft.tileentities.ISideConfigurable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filippo on 18-Apr-17.
 */
    public class ConfigurableItemHandler
        implements IItemHandler, IItemHandlerModifiable
    {
        protected ItemStackHandler inputItemHandler, outputItemHandler;
        protected List<Tuple<Integer, Boolean>> slotId;
        ISideConfigurable te;

        public ConfigurableItemHandler(ItemStackHandler inputItemHandler,
                                       ItemStackHandler outputItemHandler, ISideConfigurable te) {
            this.inputItemHandler = inputItemHandler;
            this.outputItemHandler = outputItemHandler;
            this.te = te;

            slotId = new ArrayList<>();
        }

        public void setFromArray(int[] inputArray, int[] outputArray, int side) {
            slotId.clear();
            int I = 0;
            for(int i: inputArray) {
                if(i == side) {
                    slotId.add(new Tuple<>(I, true));
                }
                ++I;
            }
            I = 0;
            for(int i: outputArray) {
                if(i == side) {
                    slotId.add(new Tuple<>(I, false));
                }
                ++I;
            }
        }

        public ItemStack getSlot(int slot) {
            Tuple<Integer, Boolean> location = slotId.get(slot);
            if(location.getSecond()) return inputItemHandler.getStackInSlot(location.getFirst());
            else return outputItemHandler.getStackInSlot(location.getFirst());
        }

        public void setSlot(int slot, ItemStack stack) {
            Tuple<Integer, Boolean> location = slotId.get(slot);
            if(location.getSecond()) inputItemHandler.setStackInSlot(location.getFirst(), stack);
            else outputItemHandler.setStackInSlot(location.getFirst(), stack);
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack)
        {
            validateSlotIndex(slot);
            if (ItemStack.areItemStacksEqual(getSlot(slot), stack))
                return;
            setSlot(slot, stack);
            onContentsChanged(slot);
        }

        @Override
        public int getSlots()
        {
            return slotId.size();
        }

        @Override
        @Nonnull
        public ItemStack getStackInSlot(int slot)
        {
            validateSlotIndex(slot);
            return getSlot(slot);
        }

        @Override
        @Nonnull
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
        {
            if (stack.isEmpty())
                return ItemStack.EMPTY;

            validateSlotIndex(slot);

            ItemStack existing = getSlot(slot);

            int limit = getStackLimit(slot, stack);

            if (!existing.isEmpty())
            {
                if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                    return stack;

                limit -= existing.getCount();
            }

            if (limit <= 0)
                return stack;

            boolean reachedLimit = stack.getCount() > limit;

            if (!simulate)
            {
                if (existing.isEmpty())
                {
                    setSlot(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
                }
                else
                {
                    existing.grow(reachedLimit ? limit : stack.getCount());
                }
                onContentsChanged(slot);
            }

            return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()- limit) : ItemStack.EMPTY;
        }

        @Nonnull
        public ItemStack extractItem(int slot, int amount, boolean simulate)
        {
            if (amount == 0)
                return ItemStack.EMPTY;

            validateSlotIndex(slot);

            ItemStack existing = getSlot(slot);

            if (existing.isEmpty())
                return ItemStack.EMPTY;

            int toExtract = Math.min(amount, existing.getMaxStackSize());

            if (existing.getCount() <= toExtract)
            {
                if (!simulate)
                {
                    setSlot(slot, ItemStack.EMPTY);
                    onContentsChanged(slot);
                }
                return existing;
            }
            else
            {
                if (!simulate)
                {
                    setSlot(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                    onContentsChanged(slot);
                }

                return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
            }
        }

        @Override
        public int getSlotLimit(int slot)
        {
            return 64;
        }

        protected int getStackLimit(int slot, @Nonnull ItemStack stack)
        {
            return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
        }

        protected void validateSlotIndex(int slot)
        {
            if (slot < 0 || slot >= slotId.size())
                throw new RuntimeException("Slot " + slot + " not in valid range - [0," + slotId.size() + ")");
        }

        protected void onLoad()
        {

        }

        protected void onContentsChanged(int slot)
        {
            te.contentChanged();
        }
    }