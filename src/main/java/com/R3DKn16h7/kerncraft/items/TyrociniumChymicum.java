package com.R3DKn16h7.kerncraft.items;

import com.R3DKn16h7.kerncraft.KernCraft;
import com.R3DKn16h7.kerncraft.crafting.ElementRecipe;
import com.R3DKn16h7.kerncraft.network.ModGuiHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TyrociniumChymicum extends Item {
    public static String base_name = "tyrocinium_chymicum";

    public TyrociniumChymicum() {
        super();

        this.setUnlocalizedName(base_name);
        this.setRegistryName(base_name);
        this.setCreativeTab(CreativeTabs.MISC);
        GameRegistry.register(this);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack,
                                      EntityPlayer entityPlayer,
                                      World world,
                                      BlockPos pos,
                                      EnumHand hand,
                                      EnumFacing facing,
                                      float hitX, float hitY, float hitZ) {

        if (world.isRemote) {
            entityPlayer.openGui(KernCraft.instance, ModGuiHandler.MANUAL_GUI, world, 0, 0, 0);
        }

        return super.onItemUse(stack, entityPlayer, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    public void addRecipes() {
        ItemStack canister = new ItemStack(ModItems.canister, 1, 1);
        NBTTagCompound comp = new NBTTagCompound();
        comp.setInteger("element", 1);
        canister.writeToNBT(comp);
        //IRecipe
        GameRegistry.addShapelessRecipe(new ItemStack(this), Items.BOOK, canister);

        int[] elements = {ElementRecipe.ANY_ELEMENT};
        int[] quantities = {0};
        ItemStack[] ing = {new ItemStack(Items.BOOK)};
        CraftingManager.getInstance().addRecipe(new ElementRecipe(ModItems.TYROCINIUM_CHYMICUM, ing, elements, quantities));
    }
}