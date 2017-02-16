package com.comp460.battle.factories.ability.entity;

import com.badlogic.ashley.core.Entity;
import com.comp460.battle.BattleScreen;
import com.comp460.battle.components.OwnerComponent;
import com.comp460.battle.factories.ability.AbilityEntityFactory;
import com.comp460.battle.factories.ability.component.AbsolutePositionFactory;

import java.util.Random;

/**
 * Created by matth on 2/15/2017.
 */
public class PuffEntityFactory  extends AbilityEntityFactory {

    @Override
    public void addToScreen(BattleScreen screen, Entity owner) {
        Random rng = new Random();
        int gap = rng.nextInt(screen.numRows);
        for (int i = 0; i < screen.numRows; i++) {
            if (i == gap) {
                continue;
            }
            Entity base = new Entity();
            components.forEach((c)->c.addToEntity(base, screen, owner));
            AbsolutePositionFactory posFac = new AbsolutePositionFactory();
            posFac.row = i;
            posFac.col = screen.numCols - 2;
            posFac.addToEntity(base, screen, owner);
            base.add(new OwnerComponent(owner));
            screen.engine.addEntity(base);
        }
    }
}
