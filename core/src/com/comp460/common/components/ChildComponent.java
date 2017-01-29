package com.comp460.common.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by matthewhammond on 1/16/17.
 */
public class ChildComponent implements Component {
    public Entity parent;

    public ChildComponent populate(Entity parent) {
        this.parent = parent;
        return this;
    }
}
