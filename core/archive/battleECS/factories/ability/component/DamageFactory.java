package com.comp460.screens.battleECS.factories.ability.component;

import com.badlogic.ashley.core.Entity;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battleECS.components.DamageComponent;
import com.comp460.screens.battle.factories.ability.AbilityComponentFactory;

/**
 * Created by matth on 2/15/2017.
 */
public class DamageFactory implements AbilityComponentFactory {
    public int amount;
    public int lifeSteal;
    public boolean destroyOnHit = true;

    @Override
    public void addToEntity(Entity base, BattleScreen screen, Entity owner) {
        base.add(new DamageComponent(amount, lifeSteal, destroyOnHit));
    }
}
