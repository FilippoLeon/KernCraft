package com.R3DKn16h7.kerncraft.network;

import com.R3DKn16h7.kerncraft.capabilities.manual.ITyrociniumProgressCapability;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Filippo on 11/12/2016.
 */
public class MessageSyncTyrociniumProgress implements IMessage {
    public Map<String, Boolean> unlocksMap = new HashMap<>();

    public MessageSyncTyrociniumProgress() {
    }

    public MessageSyncTyrociniumProgress(ITyrociniumProgressCapability capability) {
        this.unlocksMap = capability.getMap();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        int nkeys = unlocksMap.entrySet().size();
        buf.writeInt(nkeys);
        for (Map.Entry<String, Boolean> entry : unlocksMap.entrySet()) {
            byte[] bytes = entry.getKey().getBytes(StandardCharsets.US_ASCII);
            buf.writeInt(bytes.length);
        }

        for (Map.Entry<String, Boolean> entry : unlocksMap.entrySet()) {
            if (entry.getValue()) {
                buf.writeBytes(entry.getKey().getBytes(StandardCharsets.US_ASCII));
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int nkeys = buf.readInt();
        int[] keylengths = new int[nkeys];
        for (int i = 0; i < nkeys; ++i) {
            keylengths[i] = buf.readInt();
        }
        for (int i = 0; i < nkeys; ++i) {
            unlocksMap.put(buf.readBytes(keylengths[i]).toString(StandardCharsets.US_ASCII), true);
        }
    }
}
