package com.R3DKn16h7.kerncraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by Filippo on 11/12/2016.
 */
public class MessageSideConfig implements IMessage {
    public int slotId;
    public int side;
    public BlockPos pos;

    public MessageSideConfig() {
    }

    public MessageSideConfig(int slotId, int side, BlockPos pos) {
        this.slotId = slotId;
        this.side = side;
        this.pos = pos;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slotId);
        buf.writeInt(side);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.slotId = buf.readInt();
        this.side = buf.readInt();
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }
}
