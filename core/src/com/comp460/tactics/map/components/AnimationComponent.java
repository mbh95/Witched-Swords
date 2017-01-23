package com.comp460.tactics.map.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by matthewhammond on 1/22/17.
 */
public class AnimationComponent implements Component {
    public TextureRegion[] frames;
    public int currentFrame;
    public int delay;
    public int countdown;

    public AnimationComponent populate(TextureRegion[] frames, int delay) {
        this.frames = frames;
        this.currentFrame = 0;
        this.delay = delay;
        this.countdown = delay;
        return this;
    }
}
