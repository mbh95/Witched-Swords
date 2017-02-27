package com.comp460.screens.battleECS.ai;

import com.badlogic.ashley.core.Entity;

/**
 * Created by matth on 2/15/2017.
 */
public interface AI {
    void tick(Entity aiUnit, float delta);
}
