package com.comp460.screens.tactics.components.unit;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

/**
 * Attached to units which the player controls.
 */
public class PlayerControlledComponent implements Component {

    private static final ComponentMapper<PlayerControlledComponent> mapper = ComponentMapper.getFor(PlayerControlledComponent.class);

    public static PlayerControlledComponent get(Entity e) {
        return mapper.get(e);
    }
}
