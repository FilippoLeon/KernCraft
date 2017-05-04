package com.R3DKn16h7.kerncraft.items.manual;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.capabilities.manual.TyrociniumProgressDefaultCapability;
import com.R3DKn16h7.kerncraft.network.ModGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TyrociniumChymicum extends Item {
    public static String base_name = "tyrocinium_chymicum";

    public static boolean sync = false;

    public TyrociniumChymicum() {
        super();

        this.setUnlocalizedName(base_name);
        this.setRegistryName(base_name);
        this.setCreativeTab(CreativeTabs.MISC);
        GameRegistry.register(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn,
                                                    EnumHand hand) {
        if (worldIn.isRemote) {
            playerIn.openGui(KernCraft.instance, ModGuiHandler.MANUAL_GUI, worldIn, 0, 0, 0);
        }
        return super.onItemRightClick(worldIn, playerIn, hand);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn,
                         Entity entityIn, int itemSlot, boolean isSelected) {
        if (!sync && Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.hasCapability(TyrociniumProgressDefaultCapability.INSTANCE, null)) {
            Minecraft.getMinecraft().player.getCapability(TyrociniumProgressDefaultCapability.INSTANCE, null).sync();
            sync = true;
        }
    }
}