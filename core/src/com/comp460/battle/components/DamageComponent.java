package com.comp460.battle.components;

import com.badlogic.ashley.core.Component;
import com.comp460.battle.BattleUnit;

/**
 * Created by matth on 2/12/2017.
 */
public class DamageComponent implements Component, Cloneable {
    public int amount;
    public int lifesteal = 0;
    public boolean removeOnContact = true;

    public DamageComponent() {

    }

    public DamageComponent(BattleUnit owner, DamageComponent template) {
        this.amount = template.amount;
        this.lifesteal = template.lifesteal;
        this.removeOnContact = template.removeOnContact;
    }
}
