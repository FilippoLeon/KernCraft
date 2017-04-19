package com.R3DKn16h7.kerncraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by Filippo on 11/12/2016.
 */
public class MessageUnlock implements IMessage {
    public int message;

    public MessageUnlock() {
    }

    public MessageUnlock(int a) {
        this.message = a;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(message);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.message = buf.readInt();
    }
}
