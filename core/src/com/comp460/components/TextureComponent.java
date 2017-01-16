package com.comp460.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.tactics.TacticsMap;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class TextureComponent implements Component {
    public TextureRegion texture = null;

    public TextureComponent populate(TextureRegion texture) {
        this.texture = texture;
        return this;
    }
}
