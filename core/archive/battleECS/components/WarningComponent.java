package com.comp460.screens.battleECS.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by matth on 2/14/2017.
 */
public class WarningComponent implements Component {
    public float duration;
    public transient Color color;

    public WarningComponent(float duration, Color color) {
        this.duration = duration;
        this.color = color;
    }
}