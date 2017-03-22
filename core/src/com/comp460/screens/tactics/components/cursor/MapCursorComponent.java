package com.comp460.screens.tactics.components.cursor;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Attached to the map cursor, encodes a movement delay.
 */
public class MapCursorComponent implements Component {
    public float countdown, delay;

    public MapCursorComponent(float delay) {
        this.delay = delay;
        this.countdown = this.delay;
    }
}
