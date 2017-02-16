package com.comp460.battle.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.battle.Mappers;
import com.comp460.battle.components.StunComponent;

/**
 * Created by matth on 2/15/2017.
 */
public class StunSystem extends IteratingSystem {
    private static final Family stunnedFamily = Family.all(StunComponent.class).get();

    public StunSystem() {
        super(stunnedFamily);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StunComponent stun = Mappers.stunM.get(entity);
        stun.duration -= deltaTime;
        if (stun.duration <= 0) {
            entity.remove(StunComponent.class);
        }
    }
}
