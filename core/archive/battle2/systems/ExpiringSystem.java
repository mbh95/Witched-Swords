package com.comp460.archive.battle2.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.archive.battle2.components.ExpiringComponent;
import com.comp460.archive.battle2.components.WarningComponent;

/**
 * Created by matth on 2/13/2017.
 */
public class ExpiringSystem extends IteratingSystem {

    private static final Family expiringFamily = Family.all(ExpiringComponent.class).exclude(WarningComponent.class).get();

    private static final ComponentMapper<ExpiringComponent> expiringtM = ComponentMapper.getFor(ExpiringComponent.class);

    public ExpiringSystem() {
        super(expiringFamily);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ExpiringComponent exp = expiringtM.get(entity);
        exp.duration-=deltaTime;
        if (exp.duration <= 0) {
            this.getEngine().removeEntity(entity);
        }
    }
}
