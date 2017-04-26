package com.R3DKn16h7.kerncraft.items;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

public class KernCraftItems {
    public static Flask FLASK;
    public static Item PORTABLE_BEACON;
    public static Canister CANISTER;

    public static ExtraShield ROMAN_SHIELD;
    public static ExtraShield GLASS_SHIELD;

    public static TestItem TEST_ITEM;

    public static LabCoat LAB_BONNET, LAB_COAT, LAB_BOOTS, LAB_PANTS;

    public static TyrociniumChymicum TYROCINIUM_CHYMICUM;

    public static ElectrolyticCell ELECTROLYTIC_CELL;
    public static PotatoBattery POTATO_BATTERY;
    public static PressurizedCell PRESSURIZED_CELL;

    public KernCraftItems() {
        PORTABLE_BEACON = new PortableBeacon();
        CANISTER = new Canister();
        FLASK = new Flask();
        PRESSURIZED_CELL = new PressurizedCell();

        ROMAN_SHIELD = new RomanShield("roman_shield");
        GLASS_SHIELD = new GlassShield("glass_shield");

        TEST_ITEM = new TestItem();

        LAB_BONNET = new LabCoat(EntityEquipmentSlot.HEAD);
        LAB_COAT = new LabCoat(EntityEquipmentSlot.CHEST);
        LAB_PANTS = new LabCoat(EntityEquipmentSlot.LEGS);
        LAB_BOOTS = new LabCoat(EntityEquipmentSlot.FEET);

        TYROCINIUM_CHYMICUM = new TyrociniumChymicum();

        ELECTROLYTIC_CELL = new ElectrolyticCell();
        POTATO_BATTERY = new PotatoBattery();
    }
}
