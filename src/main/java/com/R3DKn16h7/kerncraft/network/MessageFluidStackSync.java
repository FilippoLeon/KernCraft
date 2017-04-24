package com.R3DKn16h7.kerncraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;

/**
 * Created by Filippo on 11/12/2016.
 * <p>
 * Syncs fluid information to the CLient, by sending
 * the name, amount and position of the entity. Also
 * sends and id, identifying the "slot" to which the fluid belongs.
 */
public class MessageFluidStackSync implements IMessage {
    /**
     * And ID identifiying which fluid is being synced (for
     * GUIs with many fluids).
     */
    public int id;
    /**
     * The position of the Entity we want to notify
     */
    public BlockPos pos;
    /**
     * The fluid name and amount, has obtained by the NBT tag, we are syncing
     */
    public FluidStack fluidStack;

    public MessageFluidStackSync() {
    }

    public MessageFluidStackSync(int id, FluidStack stack,
                                 BlockPos pos) {
        this.id = id;
        this.pos = pos;
        this.fluidStack = stack;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeInt(fluidStack.amount);
        byte[] bytes = fluidStack.getFluid().getName().getBytes(StandardCharsets.US_ASCII);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.id = buf.readInt();
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        int amount = buf.readInt();
        int len = buf.readInt();
        String fluidName = buf.readBytes(len).toString(StandardCharsets.US_ASCII);
        fluidStack = FluidRegistry.getFluidStack(fluidName, amount);
    }
}
