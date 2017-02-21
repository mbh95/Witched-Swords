package com.comp460.tactics.components.core;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

/**
 * Attached to entities to give them a position on the screen. Sprites are sorted by z before rendering.
 */
public class TransformComponent implements Component {
    public final Vector3 pos;

    public TransformComponent(float x, float y, float z) {
        this.pos = new Vector3(x, y, z);
    }
}
