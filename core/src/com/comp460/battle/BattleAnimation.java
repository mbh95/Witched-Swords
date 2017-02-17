package com.comp460.battle;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by matth on 2/17/2017.
 */
public class BattleAnimation implements BattleObject {

    public Animation<TextureRegion> animation;
    public float animTimer;

    public Vector3 transform;

    public float duration;


    public BattleAnimation(Animation<TextureRegion> animation, float x, float y) {
        this(animation, x, y, animation.getAnimationDuration());
    }

    public BattleAnimation(Animation<TextureRegion> animation, float x, float y, float duration) {
        this.animation = animation;
        this.animTimer = 0f;

        this.transform = new Vector3(x, y, 0);

        this.duration = duration;
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        batch.draw(animation.getKeyFrame(animTimer), transform.x, transform.y);
    }

    @Override
    public void update(float delta) {
        animTimer += delta;
        duration -= delta;
    }
}
