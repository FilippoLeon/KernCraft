package com.R3DKn16h7.kerncraft.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.capabilities.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.ElementContainerProvider;
import com.R3DKn16h7.kerncraft.capabilities.IElementContainer;
import com.R3DKn16h7.kerncraft.elements.ElementRegistry;
import com.R3DKn16h7.kerncraft.utils.PotionImprovedHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Portable beacon that doesn't need to be placed in the world.
 * TODO: baubles + COFH compoatibility
 * @author R3DKn16h7
 */
@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class PortableBeacon extends Item implements IBauble {
    static final Map<Integer, Effect> effectMap = new HashMap<>();
    public static String unlocalizedName = "portable_beacon";

    public PortableBeacon() {
        super();

        this.setUnlocalizedName(unlocalizedName);
        this.setRegistryName(unlocalizedName);
        this.setCreativeTab(KernCraft.KERNCRAFT_CREATIVE_TAB);
        this.setMaxStackSize(1);
        GameRegistry.register(this);

        addEffect("He", Target.Self, PotionImprovedHelper.HASTE, 300, 1);
        addEffect("U", Target.HittedEntity, PotionImprovedHelper.WITHER, 300, 0);
        addEffect("C", Target.OnUse, PotionImprovedHelper.REGENERATION, 300, 0);
    }

    public void addEffect(String name, Target target, int effect, int duration, int amplifier) {
        effectMap.put(ElementRegistry.symbolToId(name),
                new Effect(target, effect, duration, amplifier));
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn,
                         Entity entityIn, int itemSlot, boolean isSelected) {
        tryApplyEffect(entityIn, stack, Target.Self);
    }

    private void tryApplyEffect(Entity entityIn, ItemStack stack, Target target) {
        IElementContainer cap = ElementCapabilities.getCapability(stack);
        if (entityIn != null
                && entityIn instanceof EntityLivingBase
                && ElementCapabilities.hasCapability(stack)) {
            EntityLivingBase player = ((EntityLivingBase) entityIn);
            for (int i : cap.getElements()) {
                if (!effectMap.containsKey(i)) continue;
                Effect eff = effectMap.get(i);
                if (eff.target != target) continue;
                if (!PotionImprovedHelper.hasPotionEffectBool(player, eff.effect)
                        && cap.removeAmountOf(i, 4, true) == 4) {
                    cap.removeAmountOf(i, 4, false);
                    PotionImprovedHelper.addPotionEffect(player, eff.effect, eff.duration, eff.amplifier);
                }
            }
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (ElementCapabilities.hasCapability(stack)) {
            IElementContainer cap = ElementCapabilities.getCapability(stack);
            return 1. - cap.getTotalAmount() / cap.getCapacity();

        }
        return 0;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player,
                                      World world, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        // TODO DEPRECATED
        if (!world.isRemote) {
            tryApplyEffect(player, player.getHeldItem(hand), Target.OnUse);

            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack,
                                                @Nullable NBTTagCompound nbt) {
        return new ElementContainerProvider(5, 5000);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        // TODO DEPRECATED
        if (!player.world.isRemote) {
            tryApplyEffect(entity, stack, Target.HittedEntity);
            return true;
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn,
                               List<String> tooltip, boolean advanced) {
        ElementCapabilities.addTooltip(stack, tooltip);

        super.addInformation(stack, playerIn, tooltip, advanced);
    }

    @Override
    @Optional.Method(modid="baubles")
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.BELT;
    }

    @Override
    @Optional.Method(modid="baubles")
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        tryApplyEffect(player, itemstack, Target.Self);
    }

    @Override
    @Optional.Method(modid="baubles")
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    @Optional.Method(modid="baubles")
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    @Optional.Method(modid="baubles")
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    @Optional.Method(modid="baubles")
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    @Optional.Method(modid="baubles")
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
        return false;
    }

    enum Target {
        HittedEntity,
        Self,
        OnUse,
    }

    class Effect {
        int amplifier;
        Target target;
        int effect;
        int duration;

        Effect(Target target, int effect, int duration) {
            this(target, effect, duration, 0);
        }

        Effect(Target target, int effect, int duration, int amplifier) {
            this.effect = effect;
            this.target = target;
            this.duration = duration;
            this.amplifier = amplifier;

        }
    }
}
