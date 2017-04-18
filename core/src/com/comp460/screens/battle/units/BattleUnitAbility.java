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
    public int energyCost;
    public int hpCost;

    public BattleUnitAbility(String id, String name, String animId, String description, int energyCost, int hpCost) {
        this.id = id;
        this.name = name;
        this.animationId = animId;
        this.description = description;
        this.energyCost = energyCost;
        this.hpCost = hpCost;
    }

    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return user.curHP >= hpCost && user.curEnergy >= energyCost;
    }

    public void attempt(BattleUnit user, BattleScreen screen) {
        if (canUse(user, screen)) {
            use(user, screen);
        } else {
            screen.flashAbilityReq(user, this);
        }
    }

    public void use(BattleUnit user, BattleScreen screen) {

        user.startAnimation(this.animationId);
        if (hpCost != 0)
            user.applyDamage(new DamageVector(hpCost, user));
        if (energyCost != 0)
            user.removeEnergy(energyCost);
    }

    public static BattleUnitAbility getNullMove() {
        return new BattleUnitAbility("none", "None", BattleAnimationManager.defaultBattleAnimID, "", 0, 0);
    }
}
