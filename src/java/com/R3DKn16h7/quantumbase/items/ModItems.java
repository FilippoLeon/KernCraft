package com.R3DKn16h7.quantumbase.items;

import net.minecraft.item.Item;

public class ModItems {
    public static Item portableBeacon;
    public static Canister canister;

    public static void createItems() {
    	
    	portableBeacon = new PortableBeacon();
        canister = new Canister();
    }
}
