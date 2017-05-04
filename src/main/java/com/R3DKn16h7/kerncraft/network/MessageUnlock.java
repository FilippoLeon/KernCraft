package com.R3DKn16h7.kerncraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;

/**
 * Created by Filippo on 11/12/2016.
 */
public class MessageUnlock implements IMessage {
    public String message;

    public MessageUnlock() {
    }

    public MessageUnlock(String message) {
        this.message = message;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        byte[] bytes = message.getBytes(StandardCharsets.US_ASCII);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int length = buf.readInt();
        this.message = buf.readBytes(length).toString(StandardCharsets.US_ASCII);
    }
}
