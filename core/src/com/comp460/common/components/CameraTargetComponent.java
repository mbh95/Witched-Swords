package com.comp460.common.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class CameraTargetComponent implements Component {
    public OrthographicCamera camera;
    public float alpha;

    public CameraTargetComponent populate(OrthographicCamera camera, float alpha) {
        this.camera = camera;
        this.alpha = alpha;
        return this;
    }
}
