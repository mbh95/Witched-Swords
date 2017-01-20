package com.comp460.tactics.map.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.comp460.Mappers;
import com.comp460.tactics.map.components.CameraTargetComponent;
import com.comp460.tactics.map.components.TransformComponent;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class CameraTrackingSystem extends IteratingSystem{


    public CameraTrackingSystem() {
        super(Family.all(CameraTargetComponent.class, TransformComponent.class).get());
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CameraTargetComponent cameraTargetComponent = Mappers.cameraTargetM.get(entity);
        TransformComponent transformComponent = Mappers.transformM.get(entity);

        cameraTargetComponent.camera.position.slerp(new Vector3(transformComponent.pos.x, transformComponent.pos.y, cameraTargetComponent.camera.position.z), 0.1f);
    }
}
