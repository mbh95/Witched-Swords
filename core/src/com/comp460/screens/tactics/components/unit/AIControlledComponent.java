package com.comp460.screens.tactics.components.unit;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

/**
 * Attached to units which the AI controls.
 */
public class AIControlledComponent implements Component {
    private static final ComponentMapper<AIControlledComponent> mapper = ComponentMapper.getFor(AIControlledComponent.class);

    public static AIControlledComponent get(Entity e) {
        return mapper.get(e);
    }
}
