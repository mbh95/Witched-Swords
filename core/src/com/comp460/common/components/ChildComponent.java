package com.comp460.common.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

/**
 * Attached to children entities to allow them to inherit certain properties from their parents (for example relative position)
 */
public class ChildComponent implements Component {
    public Entity parent;

    public ChildComponent(Entity parent) {
        this.parent = parent;
    }

    private static final ComponentMapper<ChildComponent> mapper = ComponentMapper.getFor(ChildComponent.class);
    public static ChildComponent get(Entity e) {
        return mapper.get(e);
    }
}
