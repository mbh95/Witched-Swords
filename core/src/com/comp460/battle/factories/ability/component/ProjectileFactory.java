package com.comp460.battle.factories.ability.component;

import com.badlogic.ashley.core.Entity;
import com.comp460.battle.BattleScreen;
import com.comp460.battle.components.ProjectileComponent;
import com.comp460.battle.factories.ability.AbilityComponentFactory;

/**
 * Created by matth on 2/14/2017.
 */
public class ProjectileFactory implements AbilityComponentFactory {
    public int dr;
    public int dc;
    public float delay;

    @Override
    public void addToEntity(Entity base, BattleScreen screen, Entity owner) {
        base.add(new ProjectileComponent(dr, dc, delay));
    }
}
