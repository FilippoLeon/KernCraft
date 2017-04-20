package com.R3DKn16h7.kerncraft.network;

import com.R3DKn16h7.kerncraft.achievements.AchievementHandler;
import com.R3DKn16h7.kerncraft.capabilities.ITyrociniumProgressCapability;
import com.R3DKn16h7.kerncraft.capabilities.TyrociniumProgressDefaultCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Filippo on 11/12/2016.
 */
public class MessageUnlockHandler implements IMessageHandler<MessageUnlock, IMessage> {

    @Override
    public IMessage onMessage(MessageUnlock message, MessageContext ctx) {
        if(ctx.side == Side.SERVER) {
            System.out.println("Recv. unlock message from client");
            EntityPlayer player = ctx.getServerHandler().playerEntity;


            if (player.hasCapability(TyrociniumProgressDefaultCapability.INSTANCE, null)) {
                ITyrociniumProgressCapability capability = player.getCapability(TyrociniumProgressDefaultCapability.INSTANCE, null);

                if(message.message.equals("sync")) {
                    player.sendMessage(new TextComponentString("Request sync from client."));
                    KernCraftNetwork.networkWrapper.sendTo(new MessageSyncTyrociniumProgress(capability), ((EntityPlayerMP) player));
                    return null;
                }

                if (capability != null && capability.isContentLocked(message.message)) {
                    if (message.message == "first_steps") {
                        player.addStat(AchievementHandler.LEARNER, 1);
                    }

                    capability.unlockContent(message.message);
                    player.sendMessage(new TextComponentString("Server just Unlocked: " + message.message));
                }
            }
        }
//            KernCraftNetwork.networkWrapper.sendToAll(new MessageUnlock(1));
//        } else {
//            System.out.println("Recv. unlock message from server");
//            EntityPlayer player = Minecraft.getMinecraft().player;
//            if(player.hasCapability(TyrociniumProgressDefaultCapability.INSTANCE, null)) {
//                ITyrociniumProgressCapability capability = player.getCapability(TyrociniumProgressDefaultCapability.INSTANCE, null);
//                if(capability.isContentLocked(1)) {
//                    capability.unlockContent(1);
//                    player.sendMessage(new TextComponentString("Client Unlocked stuff"));
//                }
//            }
//        }

        return null;
    }

}
