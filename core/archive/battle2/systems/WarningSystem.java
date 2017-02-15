package com.comp460.archive.battle2.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.archive.battle2.components.WarningComponent;

/**
 * Created by matth on 2/13/2017.
 */
public class WarningSystem extends IteratingSystem {
    private static final Family warningFamily = Family.all(WarningComponent.class).get();

    private static final ComponentMapper<WarningComponent> warningM = ComponentMapper.getFor(WarningComponent.class);

    public WarningSystem() {
        super(warningFamily);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        WarningComponent warning = warningM.get(entity);
        warning.duration-=deltaTime;
        if (warning.duration <= 0) {
            entity.remove(WarningComponent.class);
        }
    }
}
