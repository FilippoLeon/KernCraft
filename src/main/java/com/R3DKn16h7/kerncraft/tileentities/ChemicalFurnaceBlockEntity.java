package com.R3DKn16h7.kerncraft.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ChemicalFurnaceBlockEntity extends MachineBlock {

    public ChemicalFurnaceBlockEntity(String unlocalizedName) {
        super(unlocalizedName);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new ExtractorTileEntity();
    }

}
