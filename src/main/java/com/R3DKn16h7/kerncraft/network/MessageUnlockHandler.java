package com.R3DKn16h7.kerncraft.network;

import com.R3DKn16h7.kerncraft.achievements.AchievementHandler;
import com.R3DKn16h7.kerncraft.capabilities.manual.ITyrociniumProgressCapability;
import com.R3DKn16h7.kerncraft.capabilities.manual.TyrociniumProgressDefaultCapability;
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
            System.out.println(String.format("Recv. unlock message '%s' from client", message.message));
            EntityPlayer player = ctx.getServerHandler().playerEntity;

            if (player.hasCapability(TyrociniumProgressDefaultCapability.INSTANCE, null)) {
                ITyrociniumProgressCapability capability = player.getCapability(TyrociniumProgressDefaultCapability.INSTANCE, null);

                if(message.message.equals("sync")) {
                    player.sendMessage(new TextComponentString("Request sync from client."));
                    KernCraftNetwork.networkWrapper.sendTo(new MessageSyncTyrociniumProgress(capability), ((EntityPlayerMP) player));
                    return null;
                }

                if (AchievementHandler.achievementUnlocks.containsKey(message.message)) {
                    player.addStat(AchievementHandler.achievementUnlocks.get(message.message), 1);
                }
                if (capability != null && capability.isContentLocked(message.message)) {

                    capability.unlockContent(message.message);
                    player.sendMessage(new TextComponentString("Server just Unlocked: " + message.message));
                }
            }
        }
        return null;
    }

}
