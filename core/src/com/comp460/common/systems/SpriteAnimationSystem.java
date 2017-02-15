package com.comp460.common.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.common.components.AnimationComponent;
import com.comp460.common.components.TextureComponent;

/**
 * Created by matthewhammond on 1/22/17.
 */
public class SpriteAnimationSystem extends IteratingSystem {

    private static final Family animationFamily = Family.all(AnimationComponent.class, TextureComponent.class).get();

    private static final ComponentMapper<AnimationComponent> animationM = ComponentMapper.getFor(AnimationComponent.class);
    private static final ComponentMapper<TextureComponent> textureM = ComponentMapper.getFor(TextureComponent.class);

    public SpriteAnimationSystem() {
        super(animationFamily);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent anim = animationM.get(entity);
        TextureComponent texture = textureM.get(entity);
        anim.timer +=deltaTime;
        texture.texture = anim.animation.getKeyFrame(anim.timer);
    }
}
