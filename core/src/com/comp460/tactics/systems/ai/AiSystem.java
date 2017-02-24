package com.comp460.tactics.systems.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.tactics.components.unit.AIControlledComponent;
import com.comp460.tactics.components.unit.ReadyToMoveComponent;

/**
 * Created by matth on 2/22/2017.
 */
public class AiSystem extends IteratingSystem {

    private static final Family readyAiUnitsFamily = Family.all(AIControlledComponent.class, ReadyToMoveComponent.class).get();

    public AiSystem() {
        super(readyAiUnitsFamily);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entity.remove(ReadyToMoveComponent.class);
    }
}
