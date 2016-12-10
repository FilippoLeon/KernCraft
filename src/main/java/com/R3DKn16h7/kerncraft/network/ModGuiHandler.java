package com.R3DKn16h7.kerncraft.network;

import com.R3DKn16h7.kerncraft.client.gui.ExtractorGuiContainer;
import com.R3DKn16h7.kerncraft.guicontainer.ExtractorContainer;
import com.R3DKn16h7.kerncraft.manual.TyrociniumChymicumGui;
import com.R3DKn16h7.kerncraft.tileentities.ExtractorTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {

    public static final int MOD_TILE_ENTITY_GUI = 0;
    public static final int MANUAL_GUI = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player,
                                      World world, int x, int y, int z) {

        if (ID == MOD_TILE_ENTITY_GUI)
            return new ExtractorContainer(player.inventory,
                    (ExtractorTileEntity) world.getTileEntity(
                            new BlockPos(x, y, z))
            );

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player,
                                      World world, int x, int y, int z) {
        if (ID == MOD_TILE_ENTITY_GUI)
            return new ExtractorGuiContainer(player.inventory,
                    (ExtractorTileEntity) world.getTileEntity(
                            new BlockPos(x, y, z))
            );
        else if (ID == MANUAL_GUI) {
            return TyrociniumChymicumGui.Factory();
        }

        return null;
    }

}
