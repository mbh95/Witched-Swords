package com.comp460.tactics.map.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.Mappers;
import com.comp460.tactics.map.components.AnimationComponent;
import com.comp460.tactics.map.components.TextureComponent;

/**
 * Created by matthewhammond on 1/22/17.
 */
public class AnimationSystem extends IteratingSystem {

    public AnimationSystem() {
        super(Family.all(AnimationComponent.class, TextureComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent anim = Mappers.animationM.get(entity);
        TextureComponent texture = Mappers.textureM.get(entity);
        if (anim.countdown == 0) {
            anim.currentFrame = (anim.currentFrame + 1) % anim.frames.length;
            texture.populate(anim.frames[anim.currentFrame]);
            anim.countdown = anim.delay;
        }
        anim.countdown--;
    }
}
