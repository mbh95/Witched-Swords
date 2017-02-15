package com.comp460.battle.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.battle.AnimationManager;
import com.comp460.battle.Mappers;
import com.comp460.battle.components.UnitComponent;
import com.comp460.common.components.AnimationComponent;

/**
 * Created by matth on 2/15/2017.
 */
public class UnitAnimationSystem extends IteratingSystem {

    private static final Family animatedUnitFamily = Family.all(UnitComponent.class, AnimationComponent.class).get();

    public UnitAnimationSystem() {
        super(animatedUnitFamily);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent animationComponent = Mappers.animM.get(entity);
        if (animationComponent.animation.isAnimationFinished(animationComponent.timer)) {
            UnitComponent unitComponent = Mappers.unitM.get(entity);
            animationComponent.animation = AnimationManager.getUnitAnimation(unitComponent.unitID, AnimationManager.defaultAnimID);
            animationComponent.timer = 0f;
        }
    }
}
