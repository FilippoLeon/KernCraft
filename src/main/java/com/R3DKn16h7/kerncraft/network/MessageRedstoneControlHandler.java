package com.R3DKn16h7.kerncraft.network;

import com.R3DKn16h7.kerncraft.tileentities.IRedstoneSettable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Filippo on 11/12/2016.
 */
public class MessageRedstoneControlHandler implements IMessageHandler<MessageRedstoneControl, IMessage> {

    @Override
    public IMessage onMessage(MessageRedstoneControl message, MessageContext ctx) {
        System.out.println("Block at " + message.pos + " sent " + message.mode);
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        TileEntity te = player.world.getTileEntity(message.pos);
        if (te instanceof IRedstoneSettable) {

            ((IRedstoneSettable) te).setMode(message.mode);
        }
        // TODO: handle any tile entity
        return null;
    }

}
