package com.R3DKn16h7.kerncraft;

import com.R3DKn16h7.kerncraft.achievements.AchievementHandler;
import com.R3DKn16h7.kerncraft.blocks.ModBlocks;
import com.R3DKn16h7.kerncraft.crafting.ModCrafting;
import com.R3DKn16h7.kerncraft.elements.ElementBase;
import com.R3DKn16h7.kerncraft.items.ModItems;
import com.R3DKn16h7.kerncraft.network.ModGuiHandler;
import com.R3DKn16h7.kerncraft.tileentities.ModTileEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    @Mod.Instance(KernCraft.MODID)
    public static KernCraft instance;

    public void preInit(FMLPreInitializationEvent e) {

        new ElementBase("asd");

        ModItems.createItems();
        ModBlocks.createBlocks();
        ModTileEntities.createEntities();
    }

    public void init(FMLInitializationEvent e) {

        ModCrafting.initCrafting();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new ModGuiHandler());


        EventHandlerCommon handler = new EventHandlerCommon();
        MinecraftForge.EVENT_BUS.register(handler);

        new AchievementHandler();
//        EventHandlerCommon handler2 = new EventHandlerCommon();
//        MinecraftForge.EVENT_BUS.register(handler);
    }

    public void postInit(FMLPostInitializationEvent e) {

    }

    public class EventHandlerCommon {


        @SubscribeEvent(priority = EventPriority.NORMAL)
        public void onPlayerPickupXP(PlayerPickupXpEvent e) {
//            e.getEntityPlayer().addChatMessage("asd");
            Minecraft.getMinecraft().thePlayer.sendChatMessage("Lol what a noob");
        }

        @SubscribeEvent(priority = EventPriority.NORMAL)
        public void LivingHurtEvent(LivingHurtEvent e) {

            EntityLivingBase ent = e.getEntityLiving();

            if ((ent != null) && ent instanceof EntityPlayer &&
                    //(EntityPlayer) ent).isHandActive() &&
                    ent.isActiveItemStackBlocking() &&
                    ent.getHeldItemOffhand() != null &&
                    ent.getHeldItemOffhand().getItem() == ModItems.EXTRA_SHIELD
                    ) {


//            && this.activeItemStack != null && this.activeItemStack.getItem() == Items.SHIELD
                Minecraft.getMinecraft().thePlayer.sendChatMessage(e.getEntity().getName() +
                        " asdsda " + e.getEntityLiving().getName() + e.getAmount()
                        + "+ asd " + e.getSource().getSourceOfDamage().getName());
                if (e.getSource().getSourceOfDamage() instanceof EntityArrow) {
                    EntityArrow a = (EntityArrow) e.getSource().getSourceOfDamage();
                    EntityLiving lv = (EntityLiving) a.shootingEntity;
                    lv.addPotionEffect(new PotionEffect(Potion.getPotionById(24), 500, 300));
                }
            } else {
                return;
            }
        }

        @SubscribeEvent(priority = EventPriority.LOW)
        public void crafting(PlayerEvent.ItemCraftedEvent event) {
            if (event.crafting.getItem() == Items.ARROW) {
                event.player.addStat(AchievementHandler.FIRST_STEP, 1);
            }
        }
//    public void LivingAttackEvent(LivingAttasackEvent e) {
//            e.getEntityPlayer().addChatMessage("asd");
//            Minecraft.getMinecraft().thePlayer.sendChatMessage("Lol waasaaaaahat a noob");
//        Minecraft.getMinecraft().thePlayer.sendChatMessage(e.getEntity().getName() +
//                "asdsda" + e.getEntityLiving().getName());
//
//        if (e.getEntityLiving() instanceof EntityPlayer)
//            e.setCanceled(true);
//    }
//

    }
}
