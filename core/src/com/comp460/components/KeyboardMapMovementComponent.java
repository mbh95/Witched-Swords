package com.comp460.components;

import com.badlogic.ashley.core.Component;
import com.comp460.tactics.TacticsMap;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class KeyboardMapMovementComponent implements Component {
    public int delay, maxDelay;

    public KeyboardMapMovementComponent populate(int maxDelay) {
        this.maxDelay = maxDelay;
        return this;
    }
}
