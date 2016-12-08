package com.R3DKn16h7.kerncraft.items;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

public class ModItems {
    public static Item portableBeacon;
    public static Canister canister;
    public static ExtraShield EXTRA_SHIELD;
    public static TestItem TEST_ITEM;

    public static LabCoat A,B,C,D;
    public static void createItems() {

        portableBeacon = new PortableBeacon();
        canister = new Canister();
        EXTRA_SHIELD = new ExtraShield();
        TEST_ITEM = new TestItem();

        A = new LabCoat(EntityEquipmentSlot.HEAD);
        B = new LabCoat(EntityEquipmentSlot.CHEST);
        C = new LabCoat(EntityEquipmentSlot.FEET);
        D = new LabCoat(EntityEquipmentSlot.LEGS);
//        IRenderFactory<ExtraShield> renderFactory;
//        RenderingRegistry.registerEntityRenderingHandler(ExtraShield.class, renderFactory);
    }
}
