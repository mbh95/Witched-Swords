package com.comp460.common.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Attached to entities that also have a texture component to add animation.
 */
public class AnimationComponent implements Component {
    public Animation<TextureRegion> animation;
    public float timer;

    public AnimationComponent(Animation<TextureRegion> animation) {
        this.animation = animation;
        this.timer = 0f;
    }
}
