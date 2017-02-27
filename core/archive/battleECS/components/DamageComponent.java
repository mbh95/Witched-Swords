package com.comp460.screens.battleECS.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by matth on 2/15/2017.
 */
public class DamageComponent implements Component {
    public int amount;
    public int lifeSteal;
    public boolean destroyOnHit = true;

    public DamageComponent(int amount, int lifeSteal, boolean destroyOnHit) {
        this.amount = amount;
        this.lifeSteal = lifeSteal;
        this.destroyOnHit = destroyOnHit;
    }
}
