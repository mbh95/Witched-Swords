package com.comp460.tactics.components.cursor;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Attached to the map cursor, encodes a movement delay and which entity is selected.
 */
public class MapCursorComponent implements Component {
    public float countdown, delay;
    public Entity selection;
    public Entity hovered;

    public MapCursorComponent(float delay) {
        this.delay = delay;
        this.countdown = this.delay;

    }
}
