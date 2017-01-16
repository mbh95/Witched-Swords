package com.comp460.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by matthewhammond on 1/16/17.
 */
public class SnapToComponent implements Component {
    public Entity other;
    public SnapToComponent populate(Entity other) {
        this.other = other;
        return this;
    }
}
