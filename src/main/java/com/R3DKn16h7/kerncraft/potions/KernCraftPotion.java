package com.R3DKn16h7.kerncraft.potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Created by Filippo on 27-Apr-17.
 */
public class KernCraftPotion extends Potion {


    public KernCraftPotion() {
        super(true, 13458603);

        setPotionName("potioneffect.random_teleport");
        setIconIndex(5, 2);
        setBeneficial();
    }


    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_) {
        super.performEffect(entityLivingBaseIn, p_76394_2_);

        World worldIn = entityLivingBaseIn.world;

        if (!worldIn.isRemote) {
            double d0 = entityLivingBaseIn.posX;
            double d1 = entityLivingBaseIn.posY;
            double d2 = entityLivingBaseIn.posZ;

            for (int i = 0; i < 16; ++i) {
                double d3 = entityLivingBaseIn.posX + (entityLivingBaseIn.getRNG().nextDouble() - 0.5D) * 16.0D;
                double d4 = MathHelper.clamp(entityLivingBaseIn.posY + (double) (entityLivingBaseIn.getRNG().nextInt(16) - 8), 0.0D, (double) (worldIn.getActualHeight() - 1));
                double d5 = entityLivingBaseIn.posZ + (entityLivingBaseIn.getRNG().nextDouble() - 0.5D) * 16.0D;

                if (entityLivingBaseIn.isRiding()) {
                    entityLivingBaseIn.dismountRidingEntity();
                }

                if (entityLivingBaseIn.attemptTeleport(d3, d4, d5)) {
                    worldIn.playSound(null, d0, d1, d2, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    entityLivingBaseIn.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                    break;
                }
            }

//            if (entityLivingBaseIn instanceof EntityPlayer)
//            {
//                ((EntityPlayer)entityLivingBaseIn).getCooldownTracker().setCooldown(this, 20);
//            }
        }

    }

    /**
     * checks if Potion effect is ready to be applied this tick.
     */
    @Override
    public boolean isReady(int duration, int amplifier) {
//        if (this == MobEffects.REGENERATION) {
        int k = 20 >> amplifier;
        return k <= 0 || duration % k == 0;
//        }
//        return false;
    }
}
