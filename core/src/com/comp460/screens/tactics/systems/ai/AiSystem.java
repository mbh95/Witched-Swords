package com.comp460.screens.tactics.systems.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.screens.tactics.components.unit.AIControlledComponent;
import com.comp460.screens.tactics.components.unit.ReadyToMoveComponent;

import java.util.PriorityQueue;

/**
 * Created by matth on 2/22/2017.
 */
public class AiSystem extends IteratingSystem {

    private static final Family readyAiUnitsFamily = Family.all(AIControlledComponent.class, ReadyToMoveComponent.class).get();

    private PriorityQueue<Entity> moveQueue;

    public AiSystem() {
        super(readyAiUnitsFamily);

        moveQueue = new PriorityQueue<>((e1, e2) -> {
            return 1;
        });
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        moveQueue.add(entity);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (!moveQueue.isEmpty()) {

            Entity toMove = moveQueue.poll();

            toMove.remove(ReadyToMoveComponent.class);

            moveQueue.clear();
        }
    }
}
