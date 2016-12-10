package com.R3DKn16h7.kerncraft.events;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * Created by Filippo on 10/12/2016.
 */
public class EventUpdate {
    public static ArrayList<ItemStack> updateable = new ArrayList<ItemStack>();

    public static int elapsed = 0;
    public static int ticTime = 40;

    public static void tick() {
        elapsed++;
        if (elapsed > ticTime) {
            elapsed = 0;
            System.out.println("Event");
        }
    }

}
