package com.R3DKn16h7.kerncraft.network;

import com.R3DKn16h7.kerncraft.client.gui.ChemicalFurnaceGuiContainer;
import com.R3DKn16h7.kerncraft.client.gui.ExtractorGuiContainer;
import com.R3DKn16h7.kerncraft.client.gui.LampGui;
import com.R3DKn16h7.kerncraft.guicontainer.ChemicalFurnaceContainer;
import com.R3DKn16h7.kerncraft.guicontainer.ExtractorContainer;
import com.R3DKn16h7.kerncraft.manual.TyrociniumChymicumGui;
import com.R3DKn16h7.kerncraft.tileentities.ChemicalFurnaceTileEntity;
import com.R3DKn16h7.kerncraft.tileentities.ExtractorTileEntity;
import com.R3DKn16h7.kerncraft.tileentities.LampTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {

    public static final int EXTRACTOR_TILE_ENTITY_GUI = 0;
    public static final int LAMP_TILE_ENTITY_GUI = 4;
    public static final int TIME_MACHINE_TILE_ENTITY_GUI = 2;
    public static final int CHEMICAL_FURNACE_TILE_ENTITY_GUI = 3;
    public static final int MANUAL_GUI = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player,
                                      World world, int x, int y, int z) {

        switch (ID) {
            case EXTRACTOR_TILE_ENTITY_GUI:
                return new ExtractorContainer(player.inventory,
                        (ExtractorTileEntity) world.getTileEntity(
                                new BlockPos(x, y, z))
                );
            case CHEMICAL_FURNACE_TILE_ENTITY_GUI:
                return new ChemicalFurnaceContainer(player.inventory,
                        (ChemicalFurnaceTileEntity) world.getTileEntity(
                                new BlockPos(x, y, z))
                );
            case LAMP_TILE_ENTITY_GUI:
                return new LampServer((LampTileEntity) world.getTileEntity(
                                new BlockPos(x, y, z)));
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player,
                                      World world, int x, int y, int z) {
        switch (ID) {
            case EXTRACTOR_TILE_ENTITY_GUI:
                return new ExtractorGuiContainer(player.inventory,
                        (ExtractorTileEntity) world.getTileEntity(
                                new BlockPos(x, y, z)));
            case CHEMICAL_FURNACE_TILE_ENTITY_GUI:
                return new ChemicalFurnaceGuiContainer(player.inventory,
                        (ChemicalFurnaceTileEntity) world.getTileEntity(
                                new BlockPos(x, y, z)));
            case LAMP_TILE_ENTITY_GUI:
                return new LampGui(player.inventory,
                        (LampTileEntity) world.getTileEntity(
                                new BlockPos(x, y, z)));
            case MANUAL_GUI:
                return TyrociniumChymicumGui.Factory();
        }

        return null;
    }

}
