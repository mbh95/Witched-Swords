package com.comp460.common.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Attached to entities to make the given camera center on that entity smoothly.
 */
public class CameraTargetComponent implements Component {

    public OrthographicCamera camera;
    public float alpha;

    public CameraTargetComponent(OrthographicCamera camera, float alpha) {
        this.camera = camera;
        this.alpha = alpha;
    }

    private static final ComponentMapper<CameraTargetComponent> mapper = ComponentMapper.getFor(CameraTargetComponent.class);
    public static CameraTargetComponent get(Entity e) {
        return mapper.get(e);
    }
}
