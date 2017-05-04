package com.R3DKn16h7.kerncraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filippo on 11/12/2016.
 * <p>
 * Syncs fluid information to the CLient, by sending
 * the name, amount and position of the entity. Also
 * sends and id, identifying the "slot" to which the fluid belongs.
 */
public class MessageFluidStackSync implements IMessage {
    /**
     * The position of the Entity we want to notify
     */
    public BlockPos pos;
    /**
     * The fluid name and amount, has obtained by the NBT tag, we are syncing
     */
    public List<FluidTank> fluidTank;

    public MessageFluidStackSync() {
    }

    public MessageFluidStackSync(List<FluidTank> stack,
                                 BlockPos pos) {
        this.pos = pos;
        this.fluidTank = stack;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        if (fluidTank != null) {
            buf.writeInt(fluidTank.size());
            for (int i = 0; i < fluidTank.size(); ++i) {
                if (fluidTank.get(i).getFluid() != null) {
                    byte[] bytes = fluidTank.get(i).getFluid().getFluid()
                            .getName().getBytes(StandardCharsets.US_ASCII);

                    buf.writeInt(bytes.length);
                    buf.writeInt(fluidTank.get(i).getCapacity());
                    buf.writeInt(fluidTank.get(i).getFluidAmount());
                    buf.writeBytes(bytes);
                } else {
                    buf.writeInt(-1);
                    buf.writeInt(fluidTank.get(i).getCapacity());
                }
            }
        } else {
            buf.writeInt(-1);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        int len = buf.readInt();
        if (len > 0) {
            fluidTank = new ArrayList<>(len);
            for (int i = 0; i < len; ++i) {
                int fluidNameLen = buf.readInt();
                fluidTank.add(new FluidTank(buf.readInt()));
                if (fluidNameLen > 0) {
                    int fluidAmount = buf.readInt();
                    String fluidName = buf.readBytes(fluidNameLen).toString(StandardCharsets.US_ASCII);
                    fluidTank.get(i).setFluid(FluidRegistry.getFluidStack(fluidName, fluidAmount));
                }
            }
        }
    }
}
