package com.comp460.screens.battleECS.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by matth on 2/12/2017.
 */
public class ExpiringComponent implements Component {
    public float duration;

    public ExpiringComponent(float duration) {
        this.duration = duration;
    }
}
