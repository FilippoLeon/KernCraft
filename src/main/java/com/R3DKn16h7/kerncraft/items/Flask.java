package com.R3DKn16h7.kerncraft.items;

import com.R3DKn16h7.kerncraft.capabilities.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.ElementContainerProvider;
import com.R3DKn16h7.kerncraft.capabilities.IElementContainer;
import com.R3DKn16h7.kerncraft.elements.Element;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class Flask extends AbstractElementContainerItem {
    public Flask() {
        super();

        this.addPropertyOverride(new ResourceLocation("color"),
                new IItemPropertyGetter() {
                    @SideOnly(Side.CLIENT)
                    public float apply(ItemStack stack, @Nullable World worldIn,
                                       @Nullable EntityLivingBase entityIn) {
                        if (ElementCapabilities.hasCapability(stack)) {
                            IElementContainer cap = ElementCapabilities.getCapability(stack);
                            if (cap.getNumberOfElements() <= 0 || cap.getTotalAmount() == 0) return 0;
                            Element elem = ElementCapabilities.getFirstElement(cap);
                            switch (elem.color) {
                                case "black":
                                    return 1;
                                case "white":
                                case "bright white":
                                case "silvery-white":
                                case "grayish-white":
                                case "gray-white":
                                    return 2;
                                case "silvery":
                                case "gray":
                                case "steel gray":
                                case "gray-black":
                                case "silvery gray":
                                    return 3;
                                case "white-yellow":
                                case "yellow(pale)":
                                case "greenish-yellow":
                                case "golden yellow":
                                case "yellow/silvery":
                                    return 4;
                                case "bluish-gray":
                                case "bluish-black":
                                case "bluish-white":
                                case "blue glow":
                                case "silvery-blue":
                                    return 5;
                                case "orange-red":
                                case "red-brown":
                                    return 6;
                            }
                        }
                        return 0;
                    }
                }
        );
    }

    public String getName() {
        return "flask";
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        ElementContainerProvider cap = new ElementContainerProvider(1, 1000);
        cap.elementContainer.removeAcceptedState(Element.State.GAS);
        return cap;
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.).
     * Not called when the player stops using
     * the Item before the action is complete.
     */
    @Override
    public ItemStack onItemUseFinish(ItemStack stack,
                                     World worldIn,
                                     EntityLivingBase entityLiving) {
        if (worldIn.isRemote) return stack;

        if (ElementCapabilities.hasCapability(stack)) {

            IElementContainer cap = ElementCapabilities.getCapability(stack);
            if (cap.getElements().length >= 1) {
                if (cap.removeAmountOf(cap.getElements()[0], 100, true) != 100) return stack;

                cap.removeAmountOf(cap.getElements()[0], 100, false);

                entityLiving.attackEntityFrom(
                        new DamageSource("stupidity"), 19
                );
            }

        }

        return stack;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {

        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {

        if (ElementCapabilities.hasCapability(stack)) {
            IElementContainer cap = ElementCapabilities.getCapability(stack);
            if (cap.getElements().length >= 1) {
                return cap.removeAmountOf(cap.getElements()[0], 100, true) == 100
                        ? EnumAction.DRINK
                        : EnumAction.NONE;
            }
        }
        return EnumAction.NONE;
    }

}