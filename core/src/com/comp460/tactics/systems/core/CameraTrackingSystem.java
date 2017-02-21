package com.comp460.tactics.systems.core;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.comp460.tactics.components.core.CameraTargetComponent;
import com.comp460.tactics.components.core.TransformComponent;

/**
 * For each entity which has a camera target component and a transform component, attempts to focus their
 * targeted camera on them by lerp-ing the camera towards their position. Note the z component is not affected.
 */
public class CameraTrackingSystem extends IteratingSystem {

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

        cameraTargetComponent.camera.position.lerp(new Vector3(transformComponent.pos.x, transformComponent.pos.y, cameraTargetComponent.camera.position.z), cameraTargetComponent.alpha);
    }
}
