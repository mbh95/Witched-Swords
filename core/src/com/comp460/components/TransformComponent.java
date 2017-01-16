package com.comp460.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.comp460.tactics.TacticsMap;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class TransformComponent implements Component {
    public final Vector3 pos = new Vector3();

    public TransformComponent populate(float x, float y, float z) {
        pos.x = x;
        pos.y = y;
        pos.z = z;
        return this;
    }
}
