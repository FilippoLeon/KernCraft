package com.R3DKn16h7.kerncraft.network;

import com.R3DKn16h7.kerncraft.tileentities.IMessageIntReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Filippo on 11/12/2016.
 */
public class MessageIntHandler implements IMessageHandler<MessageInt, IMessage> {

    @Override
    public IMessage onMessage(MessageInt message, MessageContext ctx) {
        System.out.println("Block at " + message.pos + " sent int " + message.message);
        EntityPlayer player = ctx.getServerHandler().player;
        TileEntity te = player.world.getTileEntity(message.pos);
        if (te instanceof IMessageIntReceiver) {

            ((IMessageIntReceiver) te).receiveMessage(message.message);
        }
        // TODO: handle any tile entity
        return null;
    }

}
