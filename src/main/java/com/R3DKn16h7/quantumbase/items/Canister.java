package com.R3DKn16h7.quantumbase.items;

import com.R3DKn16h7.quantumbase.QuantumBase;
import com.R3DKn16h7.quantumbase.elements.ElementBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class Canister extends Item {
    public static String base_name = "canister";

    public Canister() {
        super();

        this.setUnlocalizedName(base_name);
        this.setRegistryName(base_name);
        this.setCreativeTab(CreativeTabs.MISC);
        GameRegistry.register(this);
    }

    static public ElementBase.Element getElement(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Element")) {
            int element_id = stack.getTagCompound().getInteger("Element");
            return ElementBase.getElement(element_id);
        }
        return null;
    }

    public void addRecipes() {
        GameRegistry.addRecipe(new ItemStack(this),
                "##", "##", '#', Blocks.IRON_BLOCK);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn,
                         Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn != null && entityIn instanceof EntityPlayer) {

            ElementBase.Element elem = Canister.getElement(stack);
            if (elem != null && elem.toxic) {
                if (((EntityPlayer) entityIn).getActivePotionEffect(Potion.getPotionFromResourceLocation("poison")) == null)
                    ((EntityPlayer) entityIn).addPotionEffect(new PotionEffect(Potion.getPotionById(1), 5));
            }
        }
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
                                                    EntityPlayer playerIn, EnumHand hand) {
        NBTTagCompound nbt;
        if (stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
        } else {
            nbt = new NBTTagCompound();
        }


        if (nbt.hasKey("Uses")) {
            nbt.setInteger("Uses", nbt.getInteger("Uses") + 5);
        } else {
            nbt.setInteger("Uses", 1);
        }
        stack.setTagCompound(nbt);

        return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {

        return stack.hasTagCompound() && stack.getTagCompound().hasKey("Element");
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0 - (double) stack.getTagCompound().getInteger("Quantity") / 1000.0;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lores, boolean b) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Uses")) {
            lores.add(Integer.toString(stack.getTagCompound().getInteger("Uses")));
        }
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Element")) {
            int element_id = stack.getTagCompound().getInteger("Element");
            ElementBase.Element element = ElementBase.getElement(element_id);

            lores.add("Element: " + element.state.toColor() + element.symbol +
                    TextFormatting.RESET.toString() +
                    " ("
                    + element.id + ")");

            boolean isCtrlKeyDown = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
            if (!isCtrlKeyDown && Minecraft.IS_RUNNING_ON_MAC)
                isCtrlKeyDown = Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA);

            if (isCtrlKeyDown) {
                lores.add("Mass: " + element.mass);
                lores.add("Toxic: " + (element.toxic ? TextFormatting.RED + "yes" : TextFormatting.GREEN + "no"));
                lores.add("Radioactive: " + (element.half_life > 0 ?
                        TextFormatting.RED + "yes (hl: " + element.half_life + ")" :
                        TextFormatting.GREEN + "no"));
            } else {
                lores.add("Hold ctrl for more stuff.");
            }
        }
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Quantity")) {
            lores.add("Quantity: " + Integer.toString(stack.getTagCompound().getInteger("Quantity")));
        }
    }
}