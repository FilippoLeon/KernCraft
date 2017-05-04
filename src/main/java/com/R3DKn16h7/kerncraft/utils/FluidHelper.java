package com.R3DKn16h7.kerncraft.utils;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;

/**
 * Created by Filippo on 24-Apr-17.
 */
public class FluidHelper {
    @Deprecated
    public static boolean isItemBucketOfFluid(Item item) {
        return item == Items.WATER_BUCKET
                || item == Items.LAVA_BUCKET
                || item == ForgeModContainer.getInstance().universalBucket;
    }

    @Deprecated
    public static FluidStack getFluidFromStack(ItemStack stack) {
        Item item = stack.getItem();
        if (item == Items.WATER_BUCKET) {
            return new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);
        } else if (item == Items.LAVA_BUCKET) {
            return new FluidStack(FluidRegistry.LAVA, 1000);
        } else if (item == ForgeModContainer.getInstance().universalBucket) {
            return ForgeModContainer.getInstance().universalBucket.getFluid(stack);
        }
        return null;
    }

    @Deprecated
    public static ItemStack bucketOf(FluidStack stack) {
        return UniversalBucket.getFilledBucket(
                ForgeModContainer.getInstance().universalBucket, stack.getFluid()
        );
    }
}
