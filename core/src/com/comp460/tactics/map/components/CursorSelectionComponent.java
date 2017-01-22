package com.comp460.tactics.map.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by matthewhammond on 1/22/17.
 */
public class CursorSelectionComponent implements Component {
    public Entity selection;

    public CursorSelectionComponent populate(Entity selection) {
        this.selection = selection;
        return this;
    }
}
