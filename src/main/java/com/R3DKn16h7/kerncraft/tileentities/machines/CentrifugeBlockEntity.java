package com.R3DKn16h7.kerncraft.tileentities.machines;

import com.R3DKn16h7.kerncraft.network.ModGuiHandler;
import com.R3DKn16h7.kerncraft.tileentities.MachineBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CentrifugeBlockEntity extends MachineBlock {

    public CentrifugeBlockEntity(String unlocalizedName) {
        super(unlocalizedName);

        setGui(ModGuiHandler.CENTRIFUGE_TILE_ENTITY_GUI);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new CentrifugeTileEntity();
    }

}
