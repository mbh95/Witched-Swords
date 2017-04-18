package com.comp460.screens.battle.units.protagonists.zane.moves;

import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitAbility;
import com.comp460.screens.battle.units.protagonists.zane.Zane;

/**
 * Created by Belinda on 4/10/2017.
 */
public class Parry extends BattleUnitAbility {

    public static float duration = 1f;
    public Zane zane;
    public Counterattack counterattack;

    public Parry(Zane zane) {
        super("parry", "Parry", "attack", "A parry to an incoming enemy attack followed by a counterattack.", 1, 0);
        this.zane = zane;
        counterattack = new Counterattack(zane);
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);
        zane.parry(0.5f);
    }
}
