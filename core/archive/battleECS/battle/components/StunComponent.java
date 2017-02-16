package com.comp460.battle.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by matth on 2/15/2017.
 */
public class StunComponent implements Component {
    public float duration;

    public StunComponent(float duration) {
        this.duration = duration;
    }
}
