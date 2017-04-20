package com.R3DKn16h7.kerncraft.network;

import com.R3DKn16h7.kerncraft.capabilities.ITyrociniumProgressCapability;
import com.R3DKn16h7.kerncraft.capabilities.TyrociniumProgressDefaultCapability;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Filippo on 11/12/2016.
 */
public class MessageSyncTyrociniumProgressHandler
        implements IMessageHandler<MessageSyncTyrociniumProgress, IMessage> {

    @Override
    public IMessage onMessage(MessageSyncTyrociniumProgress message, MessageContext ctx) {
        if(Minecraft.getMinecraft().player.hasCapability(TyrociniumProgressDefaultCapability.INSTANCE, null)) {
            ITyrociniumProgressCapability capability = Minecraft.getMinecraft().player.getCapability(TyrociniumProgressDefaultCapability.INSTANCE, null);
            capability.unpackMessage(message);
        }

        return null;
    }

}
