package com.comp460.common.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

/**
 * Attached to entities to give them a position on the screen. Sprites are sorted by z before rendering.
 */
public class TransformComponent implements Component {
    public final Vector3 pos;

    public TransformComponent(float x, float y, float z) {
        this.pos = new Vector3(x, y, z);
    }

    private static final ComponentMapper<TransformComponent> mapper = ComponentMapper.getFor(TransformComponent.class);
    public static TransformComponent get(Entity e) {
        return mapper.get(e);
    }
}
