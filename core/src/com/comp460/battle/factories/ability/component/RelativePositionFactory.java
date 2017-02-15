package com.comp460.battle.factories.ability.component;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.comp460.battle.BattleScreen;
import com.comp460.battle.components.GridPositionComponent;
import com.comp460.common.components.TransformComponent;

/**
 * Created by matth on 2/14/2017.
 */
public class RelativePositionFactory extends AbsolutePositionFactory {

    private static final ComponentMapper<GridPositionComponent> posM = ComponentMapper.getFor(GridPositionComponent.class);

    @Override
    public void addToEntity(Entity base, BattleScreen screen, Entity owner) {
        GridPositionComponent pos = posM.get(owner);
        int finalRow = row;
        int finalCol = col;
        if (pos != null) {
            finalRow += pos.row;
            finalCol += pos.col;
        }
        base.add(new GridPositionComponent(finalRow, finalCol));
        base.add(new TransformComponent().populate(screen.colToScreenX(finalCol), screen.rowToScreenY(finalRow), 0f));

    }
}
