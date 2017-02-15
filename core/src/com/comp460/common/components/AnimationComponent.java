package com.comp460.common.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by matthewhammond on 1/27/17.
 */
public class AnimationComponent implements Component {
    public Animation<TextureRegion> animation;
    public float timer;

    public AnimationComponent populate(TextureRegion[] frames, int delay) {
        this.animation = new Animation<TextureRegion>(delay, frames);
        this.timer = 0f;
        return this;
    }

    public AnimationComponent populate(Animation<TextureRegion> animation) {
        this.animation = animation;
        this.timer = 0f;
        return this;
    }
}
