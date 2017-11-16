package com.R3DKn16h7.kerncraft.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.R3DKn16h7.kerncraft.capabilities.element.ElementCapabilities;
import com.R3DKn16h7.kerncraft.capabilities.element.ElementContainerProvider;
import com.R3DKn16h7.kerncraft.capabilities.element.IElementContainer;
import com.R3DKn16h7.kerncraft.elements.ElementRegistry;
import com.R3DKn16h7.kerncraft.items.AbstractElementContainerItem;
import com.R3DKn16h7.kerncraft.utils.PlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nullable;
import java.util.List;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class AlchemistRing extends AbstractElementContainerItem implements IBauble {
    public static int id;

    public AlchemistRing() {

        super();

        id = ElementRegistry.symbolToId("Si");
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn,
                                                    EntityPlayer playerIn,
                                                    EnumHand hand) {

        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(hand));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn,
                                      World worldIn,
                                      BlockPos pos,
                                      EnumHand hand,
                                      EnumFacing facing,
                                      float hitX, float hitY, float hitZ) {

        return super.onItemUse(playerIn, worldIn, pos,
                hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn,
                         Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public String getName() {
        return "alchemist_ring";
    }

    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.RING;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        if (player.world.isRemote) return;

        Vec3d pos1 = player.getForward().scale(0.2);
        Vec3d pos2 = new Vec3d(pos1.x, 0, pos1.z);
        Vec3d pos = player.getPositionVector().add(pos2);
        pos = pos.addVector(0., PlayerHelper.isCtrlKeyDown() ? -2. : -1., 0.);
        BlockPos intPos = new BlockPos(pos.x, pos.y, pos.z);
        if (!PlayerHelper.isShiftDown() && player.world.isAirBlock(intPos)) {
            IElementContainer cap = ElementCapabilities.getCapability(itemstack);
            int sim = cap.removeAmountOf(id, 1, true);
            if (sim >= 1) {
                cap.removeAmountOf(id, 1, false);
                player.world.setBlockState(intPos, Blocks.COBBLESTONE.getDefaultState());
            }
        }
    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
        return false;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean onLeftClickEntity(ItemStack stack,
                                     EntityPlayer player,
                                     Entity entity) {
        return true;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        ElementContainerProvider cap = new ElementContainerProvider(2, 1000);
//        cap.elementContainer.removeAcceptedState(Element.State.GAS);
        return cap;
    }

}