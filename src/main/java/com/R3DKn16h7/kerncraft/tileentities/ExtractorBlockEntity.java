package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.network.ModGuiHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class ExtractorBlockEntity extends MachineBlock {

    public ExtractorBlockEntity(String unlocalizedName) {
        super(unlocalizedName);

        setGui(ModGuiHandler.EXTRACTOR_TILE_ENTITY_GUI);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new ExtractorTileEntity();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
