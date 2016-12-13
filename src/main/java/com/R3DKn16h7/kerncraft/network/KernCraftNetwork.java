package com.R3DKn16h7.kerncraft.network;

import com.R3DKn16h7.kerncraft.KernCraft;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Filippo on 11/12/2016.
 */
public class KernCraftNetwork {

    public static SimpleNetworkWrapper networkWrapper;

    public static KernCraftNetwork INSTANCE;

    public static int discriminator = 0;

    public KernCraftNetwork() {
        INSTANCE = this;

        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(KernCraft.MODID);
        networkWrapper.registerMessage(MessageRedstoneControlHandler.class,
                MessageRedstoneControl.class, getNextDiscriminator(), Side.SERVER);
        networkWrapper.registerMessage(MessageIntHandler.class,
                MessageInt.class, getNextDiscriminator(), Side.SERVER);

    }

    public static int getNextDiscriminator() {
        return discriminator++;
    }
}
