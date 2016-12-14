package com.R3DKn16h7.kerncraft.tileentities;

import com.R3DKn16h7.kerncraft.network.ModGuiHandler;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ChemicalFurnaceBlockEntity extends MachineBlock {

    public ChemicalFurnaceBlockEntity(String unlocalizedName) {
        super(unlocalizedName);

        setGui(ModGuiHandler.CHEMICAL_FURNACE_TILE_ENTITY_GUI);

        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this).setRegistryName(unlocalizedName));
        GameRegistry.registerTileEntity(ChemicalFurnaceTileEntity.class, unlocalizedName);

    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new ChemicalFurnaceTileEntity();
    }

}
