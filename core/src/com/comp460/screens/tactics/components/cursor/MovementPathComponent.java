package com.comp460.screens.tactics.components.cursor;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.comp460.screens.tactics.components.map.MapPositionComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Attached to the cursor when a path is being drawn.
 */
public class MovementPathComponent implements Component {
    public List<MapPositionComponent> positions = new ArrayList<>();

    private static final ComponentMapper<MovementPathComponent> mapper = ComponentMapper.getFor(MovementPathComponent.class);

    public static MovementPathComponent get(Entity e) {
        return mapper.get(e);
    }
}
