package com.comp460.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class CameraTargetComponent implements Component {
    public OrthographicCamera camera;

    public CameraTargetComponent populate(OrthographicCamera camera) {
        this.camera = camera;
        return this;
    }
}
