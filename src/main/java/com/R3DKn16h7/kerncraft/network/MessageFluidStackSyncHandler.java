package com.R3DKn16h7.kerncraft.network;

import com.R3DKn16h7.kerncraft.tileentities.SmeltingTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;

/**
 * Created by Filippo on 11/12/2016.
 */
public class MessageFluidStackSyncHandler
        implements IMessageHandler<MessageFluidStackSync, IMessage> {

    @Override
    public IMessage onMessage(MessageFluidStackSync message,
                              MessageContext ctx) {
        TileEntity te = Minecraft.getMinecraft().player.world.getTileEntity(message.pos);
        if (te instanceof SmeltingTileEntity) {
            SmeltingTileEntity smeltingTe = ((SmeltingTileEntity) te);

            int numberOfTanks = message.fluidTank.size();
            smeltingTe.tank = new ArrayList<FluidTank>(numberOfTanks);
            for (int i = 0; i < numberOfTanks; ++i) {
                smeltingTe.tank.add(message.fluidTank.get(i));
            }
        }
        // TODO: handle any tile entity
        return null;
    }

}
