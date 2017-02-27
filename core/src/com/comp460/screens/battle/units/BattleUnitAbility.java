package com.comp460.screens.battle.units;

import com.comp460.assets.BattleAnimationManager;
import com.comp460.screens.battle.BattleScreen;

/**
 * Created by matth on 2/15/2017.
 */
public class BattleUnitAbility {
    public final String id;
    public final String name;
    public final String animationId;
    public final String description;

    public BattleUnitAbility(String id, String name, String animId, String description) {
        this.id = id;
        this.name = name;
        this.animationId = animId;
        this.description = description;
    }

    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return false;
    }

    public void use(BattleUnit user, BattleScreen screen) {

    }

    public static BattleUnitAbility getNullMove() {
        return new BattleUnitAbility("none", "None", BattleAnimationManager.defaultBattleAnimID, "");
    }
}
