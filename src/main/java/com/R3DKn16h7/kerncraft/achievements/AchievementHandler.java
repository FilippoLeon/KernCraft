package com.R3DKn16h7.kerncraft.achievements;

import net.minecraft.init.Items;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

/**
 * Created by Filippo on 08/12/2016.
 */
public class AchievementHandler {

    public static Achievement FIRST_STEP;
    public static Achievement SECOND_STEP;
    public static AchievementPage PAGE;

    public AchievementHandler() {

        FIRST_STEP = new Achievement("", "FirstAchievement",
                0, 0, Items.ACACIA_DOOR, null);
        SECOND_STEP = new Achievement("", "SecondAchievement",
                0, 4, Items.ARROW, null);
        PAGE = new AchievementPage("KernCraft", FIRST_STEP, SECOND_STEP);
        AchievementPage.registerAchievementPage(PAGE);
    }
}
