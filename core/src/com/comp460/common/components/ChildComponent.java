package com.comp460.common.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Attached to children entities to allow them to inherit certain properties from their parents (for example relative position)
 */
public class ChildComponent implements Component {
    public Entity parent;

    public ChildComponent(Entity parent) {
        this.parent = parent;
    }
}
