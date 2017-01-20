package com.comp460.tactics.map.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by matthewhammond on 1/19/17.
 */
public class KeyboardMapMovementComponent implements Component {
    public int countdown, delay;

    public KeyboardMapMovementComponent populate(int delay) {
        this.delay = delay;
        if (this.countdown > this.delay) {
            this.countdown = this.delay;
        }
        return this;
    }

}
