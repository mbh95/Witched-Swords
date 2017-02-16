package com.comp460.battle.factories.ability.component;

import com.badlogic.ashley.core.Entity;
import com.comp460.battle.BattleScreen;
import com.comp460.battle.components.GridPositionComponent;
import com.comp460.battle.factories.ability.AbilityComponentFactory;
import com.comp460.common.components.TransformComponent;

/**
 * Created by matth on 2/14/2017.
 */
public class AbsolutePositionFactory implements AbilityComponentFactory {

    public int row;
    public int col;

    @Override
    public void addToEntity(Entity base, BattleScreen screen, Entity owner) {
        base.add(new GridPositionComponent(row, col));
        base.add(new TransformComponent().populate(screen.colToScreenX(col), screen.rowToScreenY(row), 0f));
    }
}
