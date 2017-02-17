package com.comp460.battle.units;

import com.comp460.assets.AnimationManager;
import com.comp460.battle.BattleScreen;

/**
 * Created by matth on 2/15/2017.
 */
public class BattleUnitAbility {
    public String id = "none";
    public String name = "None";
    public String animationId = AnimationManager.defaultBattleAnimID;
    public String description = "";

    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return false;
    }

    public void use(BattleUnit user, BattleScreen screen) {

    }
}
