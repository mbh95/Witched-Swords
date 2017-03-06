package com.comp460.screens.battle.units.protagonists.andre.moves;

import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitAbility;
import com.comp460.screens.battle.units.protagonists.andre.Andre;

/**
 * Created by Belinda on 2/17/17.
 */
public class Smash extends BattleUnitAbility {

    Andre andre;

    public Smash(Andre andre) {
        super("smash", "Smash!", "attack", "Punch the ground and create a shockwave that damages enemies.");
        this.andre = andre;
    }

    @Override
    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return user.curEnergy >= 2 || andre.smashCol >= 0;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);
        if (andre.smashCol >= 0) {
            return;
        }
        user.removeEnergy(2);
        andre.smash();
    }
}
