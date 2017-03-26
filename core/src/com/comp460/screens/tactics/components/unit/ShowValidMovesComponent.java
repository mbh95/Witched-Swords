package com.comp460.screens.tactics.components.unit;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

/**
 * Attached to units to have their valid moves visible.
 */
public class ShowValidMovesComponent implements Component {

    private static final ComponentMapper<ShowValidMovesComponent> mapper = ComponentMapper.getFor(ShowValidMovesComponent.class);

    public static ShowValidMovesComponent get(Entity e) {
        return mapper.get(e);
    }
}
