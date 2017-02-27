package com.comp460.screens.battleECS.factories.ability;

import com.badlogic.ashley.core.Entity;
import com.comp460.screens.battle.BattleScreen;

/**
 * Created by matth on 2/14/2017.
 */
public interface AbilityComponentFactory {

    void addToEntity(Entity base, BattleScreen screen, Entity owner);
}
