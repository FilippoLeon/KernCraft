package com.R3DKn16h7.kerncraft.tileentities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LampTileEntity extends TileEntity implements IRedstoneSettable, IMessageIntReceiver {

    public int redstoneMode = 0;
    public int lightLevel = 0;

    public LampTileEntity() {

    }

    public int getRedstoneMode() {
        return redstoneMode;
    }

    public void setMode(int i) {
        redstoneMode = i;
        updateState();
    }

    public void receiveMessage(int i) {
        if (i == 0 && lightLevel < 15) ++lightLevel;
        else if (i == 1 && lightLevel > 0) --lightLevel;
        else return;
        updateState();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
    }

    public void updateState() {
        if (redstoneMode == 0 && !this.worldObj.isBlockPowered(pos)) {
            worldObj.setBlockState(this.pos, ModTileEntities.LAMP.getDefaultState()
                    .withProperty(LampBlockEntity.POWERED, 0));
        } else if (redstoneMode == 1 && this.worldObj.isBlockPowered(pos)) {
            worldObj.setBlockState(this.pos, ModTileEntities.LAMP.getDefaultState()
                    .withProperty(LampBlockEntity.POWERED, 0));
        } else {
            worldObj.setBlockState(this.pos, ModTileEntities.LAMP
                    .getDefaultState().withProperty(LampBlockEntity.POWERED, lightLevel));
        }

    }
}