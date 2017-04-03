package com.comp460.screens.battle.units;

/**
 * Created by matth on 2/17/2017.
 */
public class DamageVector {

    public BattleUnit source;

    public int trueDamage;

    public DamageVector(int trueDamage, BattleUnit source) {
        this.trueDamage = trueDamage;
        this.source = source;
    }

    public DamageVector(DamageVector prototype) {
        this.trueDamage = prototype.trueDamage;
        this.source = prototype.source;
    }
}
