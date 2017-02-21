package com.comp460.tactics.components.core;

import com.badlogic.ashley.core.Component;
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
}
