package com.comp460.common.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.common.components.ChildComponent;
import com.comp460.common.components.TransformComponent;

/**
 * For each entity with a parent-child relationship and transform component, snaps childrens' positions to their
 * parent's position.
 */
public class SnapToParentSystem extends IteratingSystem {

    private static final Family childFamily = Family.all(ChildComponent.class, TransformComponent.class).get();

    private static final ComponentMapper<ChildComponent> childM = ComponentMapper.getFor(ChildComponent.class);
    private static final ComponentMapper<TransformComponent> transformM = ComponentMapper.getFor(TransformComponent.class);

    public SnapToParentSystem() {
        super(childFamily);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ChildComponent childRelation = childM.get(entity);
        TransformComponent transform = transformM.get(entity);
        TransformComponent targetTransform = transformM.get(childRelation.parent);
        if (targetTransform == null) {
            return;
        }
        transform.pos.set(targetTransform.pos);
    }
}
