package com.R3DKn16h7.kerncraft.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.R3DKn16h7.kerncraft.blocks.KernCraftBlocks;
import com.R3DKn16h7.kerncraft.capabilities.element.ElementContainerProvider;
import com.R3DKn16h7.kerncraft.items.AbstractElementContainerItem;
import com.R3DKn16h7.kerncraft.utils.PlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
    public AlchemistRing() {
        super();
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
//        ItemStack stack = playerIn.getHeldItem(hand);

        return super.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn,
                         Entity entityIn, int itemSlot, boolean isSelected) {
//        if( worldIn.isRemote) {
//            BlockPos pos = entityIn.getPosition();
//            pos = pos.add(0, PlayerHelper.isCtrlKeyDown() ? -2 : -1, 0);
//            if (!PlayerHelper.isShiftDown() && worldIn.isAirBlock(pos)) {
//
////            worldIn.getBlockState(pos))
//                worldIn.setBlockState(pos, KernCraftBlocks.TEST_BLOCK.getDefaultState());
//            }
//        }
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

//        BlockPos pos = player.getPosition();
        Vec3d pos1 = player.getForward().scale(0.2);
        Vec3d pos2 = new Vec3d(pos1.xCoord, 0, pos1.zCoord);
        Vec3d pos = player.getPositionVector().add(pos2);
        pos = pos.addVector(0., PlayerHelper.isCtrlKeyDown() ? -2. : -1., 0.);
        BlockPos intPos = new BlockPos(pos.xCoord, pos.yCoord, pos.zCoord);
        if (!PlayerHelper.isShiftDown() && player.world.isAirBlock(intPos)) {

//            worldIn.getBlockState(pos))
            player.world.setBlockState(intPos, KernCraftBlocks.TEST_BLOCK.getDefaultState());
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


    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lores, boolean b) {

    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        ElementContainerProvider cap = new ElementContainerProvider(2, 1000);
//        cap.elementContainer.removeAcceptedState(Element.State.GAS);
        return cap;
    }

}