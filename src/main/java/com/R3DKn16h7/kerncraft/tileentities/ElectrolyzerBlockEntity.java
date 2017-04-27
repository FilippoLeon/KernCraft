package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.network.ModGuiHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ElectrolyzerBlockEntity extends MachineBlock {

    public ElectrolyzerBlockEntity(String unlocalizedName) {
        super(unlocalizedName);

        setGui(ModGuiHandler.CHEMICAL_FURNACE_TILE_ENTITY_GUI);

    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new ChemicalFurnaceTileEntity();
    }

}
