package com.R3DKn16h7.quantumbase.items;

import java.util.List;

import com.R3DKn16h7.quantumbase.QuantumBase;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Canister extends Item {
	public static String base_name = "canister";

	public Canister() {
		super();

		this.setUnlocalizedName(base_name);
		this.setRegistryName(base_name);
		this.setCreativeTab(CreativeTabs.MISC);
		GameRegistry.register(this);
	}
	
	public void addRecipes() {
        GameRegistry.addRecipe(new ItemStack(this), 
                new Object[] {"##", "##", '#', Blocks.IRON_BLOCK});
	}

	public void registerRender() {
	    Minecraft.getMinecraft()
	    .getRenderItem()
	    .getItemModelMesher()
	    .register(this, 0, 
	            new ModelResourceLocation(QuantumBase.MODID + ":" + 
	                                    this.getUnlocalizedName().substring(5), 
	                    "inventory")
	            );
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, 
	                                                EntityPlayer playerIn, EnumHand hand)
    {
	    NBTTagCompound nbt;
	    if (stack.hasTagCompound())
	    {
	        nbt = stack.getTagCompound();
	    }
	    else
	    {
	        nbt = new NBTTagCompound();
	    }

	    
	    if (nbt.hasKey("Uses"))
	    {
	        nbt.setInteger("Uses", nbt.getInteger("Uses") + 1);
	    }
	    else
	    {
	        nbt.setInteger("Uses", 1);
	    }
	    stack.setTagCompound(nbt);
	   
        return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List lores, boolean b)
	{
	    if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Uses"))
	    {
	        lores.add(Integer.toString(stack.getTagCompound().getInteger("Uses")));
	    }
	        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Element"))
	        {
            lores.add("Element: " + Integer.toString(stack.getTagCompound().getInteger("Element")));
	        }
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Quantity"))
            {
            lores.add("Quantity: " + Integer.toString(stack.getTagCompound().getInteger("Quantity")));
	    }
	}
}