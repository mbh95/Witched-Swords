package com.comp460.common.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

/**
 * Attached to entities to prevent them from being rendered;
 */
public class InvisibleComponent implements Component {
    private static final ComponentMapper<InvisibleComponent> mapper = ComponentMapper.getFor(InvisibleComponent.class);
    public static InvisibleComponent get(Entity e) {
        return mapper.get(e);
    }
}
