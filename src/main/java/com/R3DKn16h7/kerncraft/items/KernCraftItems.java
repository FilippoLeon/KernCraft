package com.R3DKn16h7.kerncraft.items;

import com.R3DKn16h7.kerncraft.items.armor.LabCoat;
import com.R3DKn16h7.kerncraft.items.baubles.AlchemistRing;
import com.R3DKn16h7.kerncraft.items.baubles.PortableBeacon;
import com.R3DKn16h7.kerncraft.items.containers.Canister;
import com.R3DKn16h7.kerncraft.items.containers.Flask;
import com.R3DKn16h7.kerncraft.items.containers.PressurizedCell;
import com.R3DKn16h7.kerncraft.items.disabled.TestItem;
import com.R3DKn16h7.kerncraft.items.energy.PotatoBattery;
import com.R3DKn16h7.kerncraft.items.manual.TyrociniumChymicum;
import com.R3DKn16h7.kerncraft.items.materials.ElectrolyticCell;
import com.R3DKn16h7.kerncraft.items.materials.MaterialItem;
import com.R3DKn16h7.kerncraft.items.shields.ExtraShield;
import com.R3DKn16h7.kerncraft.items.shields.GlassShield;
import com.R3DKn16h7.kerncraft.items.shields.RomanShield;
import com.R3DKn16h7.kerncraft.items.upgrades.UpgradeItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class KernCraftItems {
    public static AlchemistRing ALCHEMIST_RING;
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

    public static Map<String, MaterialItem> MATERIALS = new HashMap<>();
    public static Map<String, UpgradeItem> UPGRADES = new HashMap<>();

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

        ALCHEMIST_RING = new AlchemistRing();

        MaterialItem.create("lithium_chloride");
        MaterialItem.create("calcium_chloride");
        MaterialItem.create("potassium_chloride");

        UpgradeItem.create("extra_slot");
    }
}
