package com.R3DKn16h7.kerncraft.items;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.capabilities.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.ElementContainerProvider;
import com.R3DKn16h7.kerncraft.capabilities.IElementContainer;
import com.R3DKn16h7.kerncraft.elements.Element;
import com.R3DKn16h7.kerncraft.elements.ElementBase;
import com.R3DKn16h7.kerncraft.utils.PlayerHelper;
import com.R3DKn16h7.kerncraft.utils.PotionImprovedHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class Canister extends Item {
    public final static String base_name = "canister";
    // TODO: move to NBT
    public final static int CAPACITY = 1000;
    final int waitTime = 20;
    int elapsed = 0;

    public Canister() {
        super();

        this.addPropertyOverride(new ResourceLocation("element"),
                new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn,
                               @Nullable EntityLivingBase entityIn)
            {
                // TODO: only on shift
                if (PlayerHelper.isCtrlKeyDown()) {
                    if (ElementCapabilities.hasCapability(stack)) {
                        IElementContainer cap = ElementCapabilities.getCapability(stack);
                        if (cap.getNumberOfElements() <= 0) return 0;
                        return ElementCapabilities.getFirstElement(cap).id;
                    }
                }
                return 0;
            }
        });

        this.setUnlocalizedName(base_name);
        this.setRegistryName(base_name);
        this.setCreativeTab(KernCraft.KERNCRAFT_CREATIVE_TAB);

//        setHasSubtypes(true);

        GameRegistry.register(this);
    }

    public static ItemStack getElementItemStack(int i) {
        return getElementItemStack(i, 0);
    }

    public static ItemStack getElementItemStack(int i, int amount) {
        ItemStack itemStack = new ItemStack(KernCraftItems.CANISTER);

        if (i > 0 && i <= ElementBase.NUMBER_OF_ELEMENTS) {
            IElementContainer cap = ElementCapabilities.getCapability(itemStack);
            int amount2 = amount >= 0 ? amount : cap.getCapacity();
            cap.addAmountOf(i, amount2, false);
        }
        return itemStack;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ElementContainerProvider(1, 1000);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn,
                         Entity entityIn, int itemSlot, boolean isSelected) {
        if (/*elapsed > waitTime &&*/ entityIn != null && entityIn instanceof EntityPlayer) {
//            elapsed = 0;
            EntityPlayer entity = (EntityPlayer) entityIn;
            IElementContainer cap = ElementCapabilities.getCapability(stack);


            if (cap.getNumberOfElements() == 0) return;

            int qty = cap.getTotalAmount();

            if(qty == 0) return;

            Element elem = ElementCapabilities.getFirstElement(stack);
            if ( elem.toxic || elem.half_life > 0 ) {
                if (PotionImprovedHelper.hasPotionEffect(entity,
                        PotionImprovedHelper.POISON) == null) {
                    PotionImprovedHelper.addPotionEffect(entity,
                            PotionImprovedHelper.POISON, 70);
                }
            }
            if( elem.half_life > 0) {
                float ticTime = 0.1f;
                int new_qty = (int) Math.floor((float)
                        qty * (ticTime / elem.half_life * 1e9 * 0.693f)
                 );
                cap.removeAmountOf(elem.id, new_qty, false);
            }
            if (elem.reachedCriticalMass(qty)) {
                double x = entityIn.posX, y = entityIn.posY, z = entityIn.posZ;
                worldIn.createExplosion(entityIn, x, y, z, 10, true);
                cap.removeAllOf(elem.id);
            }
        }
//        ++elapsed;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn,
                                                    EnumHand handIn) {

        if (PlayerHelper.isCtrlKeyDown() ? handIn.equals(EnumHand.MAIN_HAND) : handIn.equals(EnumHand.OFF_HAND)) {
            ItemStack itemStack = playerIn.getHeldItem(handIn);
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
                    ElementCapabilities.transferAmount(cap2, otherCap, id, transferable);
                }

            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return ElementCapabilities.getCapability(stack).getNumberOfElements() > 0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        IElementContainer cap = ElementCapabilities.getCapability(stack);
        return 1.0 - (double) cap.getTotalAmount() / cap.getTotalAmount();
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