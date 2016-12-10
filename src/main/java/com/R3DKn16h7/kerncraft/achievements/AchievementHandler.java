package com.R3DKn16h7.kerncraft.achievements;

import com.R3DKn16h7.kerncraft.items.ModItems;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

/**
 * Created by Filippo on 08/12/2016.
 */
public class AchievementHandler {

    public static Achievement APPRENTICE;
    public static Achievement MASTER_OF_POTATOES;
    public static Achievement CHEMIST;
    public static AchievementPage PAGE;

    public AchievementHandler() {

        APPRENTICE = new Achievement("apprentice", "Apprentice",
                0, 0, ModItems.CANISTER, null);
        MASTER_OF_POTATOES = new Achievement("mop", "Master of potatoes",
                0, 3, ModItems.POTATO_BATTERY, APPRENTICE);
        CHEMIST = new Achievement("chemist", "Chemist",
                0, 6, ModItems.ELECTROLYTIC_CELL, MASTER_OF_POTATOES);

        PAGE = new AchievementPage("KernCraft", APPRENTICE, MASTER_OF_POTATOES, CHEMIST);
        AchievementPage.registerAchievementPage(PAGE);
    }
}
