package com.R3DKn16h7.kerncraft.items;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.capabilities.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.ElementContainerProvider;
import com.R3DKn16h7.kerncraft.capabilities.IElementContainer;
import com.R3DKn16h7.kerncraft.elements.Element;
import com.R3DKn16h7.kerncraft.utils.PlayerHelper;
import com.R3DKn16h7.kerncraft.utils.PotionImprovedHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Filippo on 26-Apr-17.
 */
public abstract class AbstractElementContainerItem extends Item {
    public AbstractElementContainerItem() {
        super();

        this.setUnlocalizedName(getName());
        this.setRegistryName(getName());
        this.setCreativeTab(KernCraft.KERNCRAFT_CREATIVE_TAB);

        GameRegistry.register(this);
    }

    public abstract String getName();

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ElementContainerProvider(1, 1000);
    }


    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {

        NBTTagCompound nbt = stack.getTagCompound();

        IElementContainer cap = ElementCapabilities.getCapability(stack);

        NBTTagCompound nbt2 = cap.serializeNBT();
        if (nbt == null) {
            nbt = new NBTTagCompound();
        }
        nbt.setTag("capabilityElementContainer", nbt2);

        return nbt;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn,
                         Entity entityIn, int itemSlot, boolean isSelected) {
        if (worldIn.isRemote && ElementCapabilities.hasCapability(stack)) {
            IElementContainer cap = ElementCapabilities.getCapability(stack);
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("capabilityElementContainer")) {
                cap.deserializeNBT(stack.getTagCompound().getCompoundTag("capabilityElementContainer"));
            }
        }

        if (entityIn != null && entityIn instanceof EntityPlayer) {
            EntityPlayer entity = (EntityPlayer) entityIn;
            IElementContainer cap = ElementCapabilities.getCapability(stack);


            if (cap.getNumberOfElements() == 0) return;

            int qty = cap.getTotalAmount();
            if (qty == 0) return;

            Element elem = ElementCapabilities.getFirstElement(stack);
            if (elem.toxic || elem.halfLife > 0) {
                if (PotionImprovedHelper.hasPotionEffect(entity,
                        PotionImprovedHelper.POISON) == null) {
                    PotionImprovedHelper.addPotionEffect(entity,
                            PotionImprovedHelper.POISON, 70);
                }
            }
            if (elem.halfLife > 0) {
                float ticTime = 0.1f;
                int new_qty = (int) Math.floor((float)
                        qty * (ticTime / elem.halfLife * 1e9 * 0.693f)
                );
                cap.removeAmountOf(elem.id, new_qty, false);
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn,
                                                    EnumHand handIn) {

        if (worldIn.isRemote) return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));

        if (PlayerHelper.isCtrlKeyDown() ? handIn.equals(EnumHand.MAIN_HAND) : handIn.equals(EnumHand.OFF_HAND)) {
            // Stack held in hand you are transfering from
            ItemStack itemStack = playerIn.getHeldItem(handIn);
            // Stack held in hand you are transfering to
            ItemStack otherHandStack = playerIn.getHeldItem(PlayerHelper.otherHand(handIn));
            if (ElementCapabilities.hasCapability(otherHandStack)) {
                IElementContainer otherCap = ElementCapabilities.getCapability(otherHandStack);
                IElementContainer cap2 = ElementCapabilities.getCapability(itemStack);

                if (cap2.getNumberOfElements() >= 1) {
                    int id = ElementCapabilities.getFirstElement(cap2).id;

                    int transferable = ElementCapabilities.amountThatCanBeTansfered(
                            cap2, otherCap,
                            id, 100
                    );
                    // If stack where we transfer has many item, split of all except one, transfer,
                    // and give back the rest to the player.
                    int otherHandCount = otherHandStack.getCount();
                    if (otherHandCount > 1) {
                        ItemStack rest = otherHandStack.splitStack(otherHandCount - 1);
                        ElementCapabilities.transferAmount(cap2, otherCap, id, transferable, playerIn);
//                        otherHandStack.setCount(64);
                        if (!playerIn.inventory.addItemStackToInventory(rest)) {
                            Block.spawnAsEntity(worldIn, playerIn.getPosition(), rest);
                        }
//                        otherHandStack.setCount(otherHandCount);
                    } else {
                        ElementCapabilities.transferAmount(cap2, otherCap, id, transferable, playerIn);
                    }
                }

            }
        }

        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return ElementCapabilities.getCapability(stack).getNumberOfElements() > 0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        IElementContainer cap = ElementCapabilities.getCapability(stack);
        return 1.0 - (double) cap.getTotalAmount() / cap.getCapacity();
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        if (ElementCapabilities.hasCapability(stack)) {
            return ElementCapabilities.getCapability(stack).getNumberOfElements() > 0 ? 1 : 64;
        }
        return 64;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player,
                               List<String> tooltipList, boolean b) {
        ElementCapabilities.addDetailedTooltipForSingleElement(stack, tooltipList);
    }
}
