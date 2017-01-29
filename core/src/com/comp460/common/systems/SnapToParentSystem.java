package com.comp460.common.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.common.components.ChildComponent;
import com.comp460.common.components.TransformComponent;

/**
 * Created by matthewhammond on 1/16/17.
 */
public class SnapToParentSystem extends IteratingSystem{

    private static final Family childFamily = Family.all(ChildComponent.class, TransformComponent.class).get();

    private static final ComponentMapper<ChildComponent> childM = ComponentMapper.getFor(ChildComponent.class);
    private static final ComponentMapper<TransformComponent> transformM = ComponentMapper.getFor(TransformComponent.class);

    public SnapToParentSystem() {
        super(Family.all(ChildComponent.class, TransformComponent.class).get());
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
