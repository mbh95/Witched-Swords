package com.comp460.common.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.comp460.common.components.CameraTargetComponent;
import com.comp460.common.components.TransformComponent;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class CameraTrackingSystem extends IteratingSystem{

    private static final Family cameraTargetFamily = Family.all(CameraTargetComponent.class, TransformComponent.class).get();

    private static final ComponentMapper<CameraTargetComponent> cameraTargetM = ComponentMapper.getFor(CameraTargetComponent.class);
    private static final ComponentMapper<TransformComponent> transformM = ComponentMapper.getFor(TransformComponent.class);

    public CameraTrackingSystem() {
        super(cameraTargetFamily);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CameraTargetComponent cameraTargetComponent = cameraTargetM.get(entity);
        TransformComponent transformComponent = transformM.get(entity);

        cameraTargetComponent.camera.position.slerp(new Vector3(transformComponent.pos.x, transformComponent.pos.y, cameraTargetComponent.camera.position.z), cameraTargetComponent.alpha);
    }
}
