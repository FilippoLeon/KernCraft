package com.R3DKn16h7.kerncraft.items;

import com.R3DKn16h7.kerncraft.elements.ElementBase;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class Canister extends Item {
    public final static String base_name = "canister";
    public final static int CAPACITY = 1000;
    final int waitTime = 20;
    int elapsed = 0;

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

    @Override
    public void onUpdate(ItemStack stack, World worldIn,
                         Entity entityIn, int itemSlot, boolean isSelected) {
        System.out.println("Update!");
        if (elapsed > waitTime && entityIn != null && entityIn instanceof EntityPlayer) {
            elapsed = 0;
            EntityPlayer entity = (EntityPlayer) entityIn;

            ElementBase.Element elem = Canister.getElement(stack);

            if(elem == null) return;

            int qty = 0;

            NBTTagCompound nbt = null;
            if (stack.hasTagCompound()) {
                nbt = stack.getTagCompound();
                if (nbt.hasKey("Quantity")) {
                    qty = nbt.getInteger("Quantity");
                }
            }

            if(qty == 0) return;

            if ( elem.toxic || elem.half_life > 0 ) {
                Potion poison_pot = Potion.getPotionFromResourceLocation("poison");
                if (entity.getActivePotionEffect(poison_pot) == null) {
                    entity.addPotionEffect(new PotionEffect(poison_pot, 50));
                }
            }
            if( elem.half_life > 0) {
                float ticTime = 0.1f;
                int new_qty = (int) Math.floor((float)
                                qty * (1.0f - ticTime / elem.half_life * 1e9 * 0.693f)
                 );
                new_qty = Math.max(0, new_qty);
                nbt.setInteger("Quantity", new_qty);

            }
            if(elem.symbol.matches("Pu") && qty > 70) {
                double x = entityIn.posX, y = entityIn.posY, z = entityIn.posZ;
                worldIn.createExplosion(null, x,y,z,10,true);
                nbt.setInteger("Quantity", 0);
            }
            stack.setTagCompound(nbt);
        }
        ++elapsed;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("Element");
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0 - (double) stack.getTagCompound().getInteger("Quantity") / CAPACITY;
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
                        TextFormatting.RED + "yes" +
                                TextFormatting.RESET + " (half-life: " +
                                String.format("%.2e", element.half_life) + " s)" :
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