package com.R3DKn16h7.kerncraft.items.tools;

import com.R3DKn16h7.kerncraft.KernCraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by filippo on 05-May-17.
 */
@SideOnly(Side.CLIENT)
public class SpecialRenderer implements ItemMeshDefinition {
    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
//stack.getItem().getRegistryName()

//        stack.getMetadata()
//                stack.get
//        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
//        ItemModelMesher
//        public IBakedModel getItemModel(ItemStack stack)
//"cauldron"
//        stack.getMultiTool.(stack).getItem().getRegistryName(), "level=0"
        ItemStack item = MultiTool.getCurrentHeldItem(stack);
        if (item.isEmpty()) {
            return new ModelResourceLocation(
                    KernCraft.MODID + ":" + stack.getUnlocalizedName().substring(5),
                    "inventory");
        }
        return new ModelResourceLocation(item.getItem().getRegistryName(), "inventory");
    }
}