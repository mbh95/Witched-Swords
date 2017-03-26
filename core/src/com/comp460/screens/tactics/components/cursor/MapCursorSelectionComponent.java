package com.comp460.screens.tactics.components.cursor;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

/**
 * Attached to the map cursor when a unit is selected
 */
public class MapCursorSelectionComponent implements Component {
    public Entity selected;

    private static final ComponentMapper<MapCursorSelectionComponent> mapper = ComponentMapper.getFor(MapCursorSelectionComponent.class);

    public static MapCursorSelectionComponent get(Entity e) {
        return mapper.get(e);
    }
}
