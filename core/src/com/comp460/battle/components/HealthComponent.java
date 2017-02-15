package com.comp460.battle.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by matth on 2/14/2017.
 */
public class HealthComponent implements Component {
    public int maxHP;
    public int curHP;

    public HealthComponent(int maxHP) {
        this(maxHP, maxHP);
    }

    public HealthComponent(int maxHP, int curHP) {
        this.maxHP = maxHP;
        this.curHP = curHP;
    }
}
