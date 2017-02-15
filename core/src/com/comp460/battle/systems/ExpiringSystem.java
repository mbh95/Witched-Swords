package com.comp460.battle.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.battle.Mappers;
import com.comp460.battle.components.ExpiringComponent;
import com.comp460.battle.components.WarningComponent;

/**
 * Created by matth on 2/13/2017.
 */
public class ExpiringSystem extends IteratingSystem {

    private static final Family expiringFamily = Family.all(ExpiringComponent.class).exclude(WarningComponent.class).get();

    public ExpiringSystem() {
        super(expiringFamily);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ExpiringComponent exp = Mappers.expiringM.get(entity);
        exp.duration-=deltaTime;
        if (exp.duration <= 0) {
            this.getEngine().removeEntity(entity);
        }
    }
}
