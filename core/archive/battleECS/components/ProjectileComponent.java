package com.comp460.screens.battleECS.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by matth on 2/13/2017.
 */
public class ProjectileComponent implements Component {
    public int dr;
    public int dc;
    public float delay;
    public transient float countdown;


    public ProjectileComponent(int dr, int dc, float delay) {
        this.dr = dr;
        this.dc = dc;
        this.delay = delay;
        this.countdown = delay;
    }
}
