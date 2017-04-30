package com.R3DKn16h7.kerncraft.items;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.blocks.KernCraftBlocks;
import com.R3DKn16h7.kerncraft.utils.PlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class AlchemistRIng extends Item {
    public static String base_name = "alchemist_ring";

    public AlchemistRIng() {
        super();

        this.setUnlocalizedName(base_name);
        this.setRegistryName(base_name);
        this.setCreativeTab(KernCraft.KERNCRAFT_CREATIVE_TAB);
        GameRegistry.register(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn,
                                                    EntityPlayer playerIn,
                                                    EnumHand hand) {

        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(hand));
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
        BlockPos pos = entityIn.getPosition();
        pos = pos.add(0, PlayerHelper.isCtrlKeyDown() ? -2 : -1, 0);
        if (!PlayerHelper.isShiftDown() && worldIn.isAirBlock(pos)) {

//            worldIn.getBlockState(pos))
            worldIn.setBlockState(pos, KernCraftBlocks.TEST_BLOCK.getDefaultState());
        }

        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack,
                                     EntityPlayer player,
                                     Entity entity) {
        return true;
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lores, boolean b) {

    }
}