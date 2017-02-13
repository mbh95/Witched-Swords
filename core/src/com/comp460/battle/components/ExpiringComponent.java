package com.comp460.battle.components;

import com.badlogic.ashley.core.Component;
import com.comp460.battle.BattleUnit;

/**
 * Created by matth on 2/12/2017.
 */
public class ExpiringComponent implements Component, Cloneable {
    public float duration;

    public ExpiringComponent() {

    }

    public ExpiringComponent(BattleUnit owner, ExpiringComponent template) {
        this.duration = template.duration;
    }
}
