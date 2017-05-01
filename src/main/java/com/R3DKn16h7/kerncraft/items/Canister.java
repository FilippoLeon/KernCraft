package com.R3DKn16h7.kerncraft.items;

import com.R3DKn16h7.kerncraft.capabilities.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.IElementContainer;
import com.R3DKn16h7.kerncraft.elements.ElementRegistry;
import com.R3DKn16h7.kerncraft.utils.PlayerHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class Canister extends AbstractElementContainerItem {
    public Canister() {
        super();

        this.addPropertyOverride(new ResourceLocation("element"),
                new IItemPropertyGetter() {
                    @SideOnly(Side.CLIENT)
                    public float apply(ItemStack stack, @Nullable World worldIn,
                                       @Nullable EntityLivingBase entityIn) {
                        if (PlayerHelper.isCtrlKeyDown()) {
                            if (ElementCapabilities.hasCapability(stack)) {
                                IElementContainer cap = ElementCapabilities.getCapability(stack);
                                if (cap.getNumberOfElements() <= 0) return 0;
                                return ElementCapabilities.getFirstElement(cap).id;
                            }
                        }
                        return 0;
                    }
                }
        );
    }


    public static ItemStack getElementItemStack(int i) {
        return getElementItemStack(i, 0);
    }

    public static ItemStack getElementItemStack(int i, int amount) {
        ItemStack itemStack = new ItemStack(KernCraftItems.CANISTER);

        if (i > 0 && i <= ElementRegistry.NUMBER_OF_ELEMENTS) {
            IElementContainer cap = ElementCapabilities.getCapability(itemStack);
            int amount2 = amount >= 0 ? amount : cap.getCapacity();
            cap.addAmountOf(i, amount2, false);
        }
        return itemStack;
    }

    public String getName() {
        return "canister";
    }

}