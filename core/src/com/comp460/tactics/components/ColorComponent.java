package com.comp460.tactics.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by matthewhammond on 2/20/17.
 */
public class ColorComponent implements Component {
    public Color color;

    public ColorComponent populate(Color color) {
        this.color = color;
        return this;
    }
}
