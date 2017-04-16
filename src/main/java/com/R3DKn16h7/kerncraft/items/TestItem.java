package com.R3DKn16h7.kerncraft.items;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class TestItem extends Item {
    public static String base_name = "test_item";

    public TestItem() {
        super();

        this.setUnlocalizedName(base_name);
        this.setRegistryName(base_name);
        this.setCreativeTab(CreativeTabs.MISC);
        GameRegistry.register(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick( World worldIn,
                                                    EntityPlayer playerIn,
                                                    EnumHand hand) {
        ItemStack stack = playerIn.getHeldItem(hand);
        NBTTagCompound nbt;
        if (stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
        } else {
            nbt = new NBTTagCompound();
        }

        if (nbt.hasKey("Uses")) {

            int c = 1;
            if (playerIn.isSneaking()) c = -1;
            nbt.setInteger("Uses", nbt.getInteger("Uses") + c);
        } else {
            nbt.setInteger("Uses", 1);
        }
        stack.setTagCompound(nbt);

        return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn,
                                      World worldIn,
                                      BlockPos pos,
                                      EnumHand hand,
                                      EnumFacing facing,
                                      float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        NBTTagCompound nbt;
        nbt = stack.getTagCompound();
        if (nbt != null && nbt.hasKey("Uses")) {
            int e = nbt.getInteger("Uses");
            Minecraft.getMinecraft().player.sendChatMessage("setEntityState" + String.format("%d", e));
            worldIn.setEntityState(playerIn, (byte) e);
        }

        worldIn.setWorldTime(0);

        return super.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack,
                                     EntityPlayer player,
                                     Entity entity) {
        NBTTagCompound nbt;
        nbt = stack.getTagCompound();
        if (nbt != null && nbt.hasKey("Uses")) {
            int e = nbt.getInteger("Uses");
            Minecraft.getMinecraft().player.sendChatMessage("setEntityState" + String.format("%d", e));
            Minecraft.getMinecraft().world.setEntityState(entity, (byte) e);
        }

        return true;
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lores, boolean b) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Uses")) {
            lores.add(Integer.toString(stack.getTagCompound().getInteger("Uses")));
        }
    }
}