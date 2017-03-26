package com.comp460.screens.tactics.components.unit;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

/**
 * Attached to units which are selected by a cursor.
 */
public class SelectedComponent implements Component {

    private static final ComponentMapper<SelectedComponent> mapper = ComponentMapper.getFor(SelectedComponent.class);

    public static SelectedComponent get(Entity e) {
        return mapper.get(e);
    }
}
