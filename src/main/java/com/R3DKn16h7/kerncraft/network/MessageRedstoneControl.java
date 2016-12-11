package com.R3DKn16h7.kerncraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by Filippo on 11/12/2016.
 */
public class MessageRedstoneControl implements IMessage {
    public int mode;
    public BlockPos pos;

    public MessageRedstoneControl() {
    }

    public MessageRedstoneControl(int a, BlockPos pos) {
        this.mode = a;
        this.pos = pos;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(mode);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.mode = buf.readInt();
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }
}
