package com.comp460.tactics.systems.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

/**
 * Created by Belinda on 2/22/17.
 */
public class HelpRenderingSystem extends IteratingSystem {

    public HelpRenderingSystem(Family family) {
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
