package com.comp460.screens.tactics.components.unit;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

/**
 * Attached to units which are able to move on the current turn.
 */
public class ReadyToMoveComponent implements Component {

    private static final ComponentMapper<ReadyToMoveComponent> mapper = ComponentMapper.getFor(ReadyToMoveComponent.class);

    public static ReadyToMoveComponent get(Entity e) {
        return mapper.get(e);
    }
}
