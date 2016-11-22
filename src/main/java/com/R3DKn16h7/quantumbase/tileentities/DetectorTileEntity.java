package com.R3DKn16h7.quantumbase.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class DetectorTileEntity extends TileEntity implements ITickable {

    boolean entityNear;

    public DetectorTileEntity() {

    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        return nbt;
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

    }

    @Override
    public void update() {

        BlockPos p = pos;
        AxisAlignedBB box = new AxisAlignedBB(p.getX() - 5f, p.getY() - 5f, p.getZ() - 5,
                p.getX() + 5, p.getY() + 5, p.getZ() + 5);

        List<EntityPlayer> ep = worldObj.getEntitiesWithinAABB(EntityPlayer.class, box);

        entityNear = false;
        for (EntityPlayer pl : ep) {
            pl.addPotionEffect(new PotionEffect(Potion.getPotionById(3), 300));
            entityNear = true;

            DetectorBlockEntity _block = (DetectorBlockEntity)
                    this.getWorld().getBlockState(this.getPos()).getBlock();

            this.worldObj.setBlockState(pos,
                    _block.getDefaultState().withProperty(DetectorBlockEntity.POWERED, true));
            return;
        }
        DetectorBlockEntity _block = (DetectorBlockEntity)
                this.getWorld().getBlockState(this.getPos()).getBlock();
        this.worldObj.setBlockState(pos,
                _block.getDefaultState().withProperty(DetectorBlockEntity.POWERED, false));

    }
}
