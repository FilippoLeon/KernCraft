package com.R3DKn16h7.kerncraft.network;

import com.R3DKn16h7.kerncraft.achievements.AchievementHandler;
import com.R3DKn16h7.kerncraft.events.IExampleCapability;
import com.R3DKn16h7.kerncraft.events.TestCabability;
import com.R3DKn16h7.kerncraft.tileentities.IRedstoneSettable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Filippo on 11/12/2016.
 */
public class MessageUnlockHandler implements IMessageHandler<MessageUnlock, IMessage> {

    @Override
    public IMessage onMessage(MessageUnlock message, MessageContext ctx) {
        if(ctx.side == Side.SERVER) {
            System.out.println("Recv. unlock message from client");
            EntityPlayer player = ctx.getServerHandler().playerEntity;

            player.addStat(AchievementHandler.LEARNER, 1);

            if (player.hasCapability(TestCabability.INSTANCE, null)) {
                IExampleCapability capability = player.getCapability(TestCabability.INSTANCE, null);
                capability.unlockContent(1);
            }
            KernCraftNetwork.networkWrapper.sendToAll(new MessageUnlock(1));
        } else {
            System.out.println("Recv. unlock message from server");


        }
//        NBTTagCompound nbt = player.getEntityData();
//        nbt.setBoolean("sdadsad", true);
//        player.writeToNBT(nbt);

        return null;
    }

}
