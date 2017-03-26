package com.comp460.screens.tactics.components.cursor;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
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

    private static final ComponentMapper<MapCursorComponent> mapper = ComponentMapper.getFor(MapCursorComponent.class);

    public static MapCursorComponent get(Entity e) {
        return mapper.get(e);
    }
}
