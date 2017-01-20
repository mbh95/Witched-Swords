package com.comp460.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.comp460.Mappers;
import com.comp460.components.CameraTargetComponent;
import com.comp460.components.SnapToComponent;
import com.comp460.components.TransformComponent;

/**
 * Created by matthewhammond on 1/16/17.
 */
public class SnapToEntitySystem extends IteratingSystem{

    public SnapToEntitySystem() {
        super(Family.all(SnapToComponent.class, TransformComponent.class).get());
   }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SnapToComponent snapToComponent = Mappers.snapToM.get(entity);
        TransformComponent transformComponent = Mappers.transformM.get(entity);
        TransformComponent targetTransform = Mappers.transformM.get(snapToComponent.other);
        if (targetTransform == null) {
            return;
        }

        transformComponent.pos.set(targetTransform.pos);
    }
}
