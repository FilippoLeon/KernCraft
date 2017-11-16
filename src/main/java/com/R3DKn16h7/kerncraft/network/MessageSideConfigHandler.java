package com.R3DKn16h7.kerncraft.network;

import com.R3DKn16h7.kerncraft.tileentities.ISideConfigurable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Filippo on 11/12/2016.
 */
public class MessageSideConfigHandler implements IMessageHandler<MessageSideConfig, IMessage> {

    @Override
    public IMessage onMessage(MessageSideConfig message, MessageContext ctx) {
        System.out.println("Block at " + message.pos + " set slot " + message.slotId + " to sideConfig " + message.side);
        EntityPlayer player = ctx.getServerHandler().player;
        TileEntity te = player.world.getTileEntity(message.pos);
        if (te instanceof ISideConfigurable) {
            ((ISideConfigurable) te).setSlotSide(message.slotId, message.side);
        }
        // TODO: handle any tile entity
        return null;
    }

}
