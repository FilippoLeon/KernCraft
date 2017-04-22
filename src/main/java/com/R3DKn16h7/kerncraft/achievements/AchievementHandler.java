package com.R3DKn16h7.kerncraft.achievements;

import com.R3DKn16h7.kerncraft.items.KernCraftItems;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Filippo on 08/12/2016.
 */
public class AchievementHandler {
    public static final boolean DEBUG_NO_PREREQUISITE_FOR_ACHIEVEMENTS = true;
    public static final Map<String, Achievement> achievementUnlocks = new HashMap<>();

    public static Achievement APPRENTICE;
    public static Achievement MASTER_OF_POTATOES;
    public static Achievement CHEMIST;
    public static Achievement LEARNER;
    public static AchievementPage PAGE;

    public AchievementHandler() {

        APPRENTICE = new Achievement("apprentice", "Apprentice",
                0, 0, KernCraftItems.CANISTER, null);
        MASTER_OF_POTATOES = new Achievement("mop", "Master of potatoes",
                0, 3, KernCraftItems.POTATO_BATTERY, APPRENTICE);
        CHEMIST = new Achievement("chemist", "Chemist",
                0, 6, KernCraftItems.ELECTROLYTIC_CELL, MASTER_OF_POTATOES);
        LEARNER = new Achievement("learner", "Learner",
                0, 9,
                KernCraftItems.TYROCINIUM_CHYMICUM, thisOrNullIfDebug(CHEMIST));

        PAGE = new AchievementPage("KernCraft", APPRENTICE, MASTER_OF_POTATOES, CHEMIST, LEARNER);
        AchievementPage.registerAchievementPage(PAGE);

        achievementUnlocks.put("first_steps", AchievementHandler.LEARNER);
    }

    private Achievement thisOrNullIfDebug(Achievement ach) {
        return DEBUG_NO_PREREQUISITE_FOR_ACHIEVEMENTS ? null : ach;
    }
}
