package com.comp460.battle.factories.ability;

import com.badlogic.ashley.core.Entity;
import com.comp460.battle.BattleScreen;
import com.comp460.battle.components.OwnerComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matth on 2/14/2017.
 */
public class AbilityEntityFactory {
    public List<AbilityComponentFactory> components = new ArrayList<>();

    public void addToScreen(BattleScreen screen, Entity owner) {
        Entity entity = new Entity();
        entity.add(new OwnerComponent(owner));
        for (AbilityComponentFactory component : components) {
            component.addToEntity(entity, screen, owner);
        }
        screen.engine.addEntity(entity);
    }
}
