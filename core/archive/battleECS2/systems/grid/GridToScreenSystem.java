package com.comp460.screens.battleECS2.systems.grid;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.common.components.ChildComponent;
import com.comp460.common.components.TransformComponent;
import com.comp460.screens.battleECS2.BattleScreen;
import com.comp460.screens.battleECS2.component.grid.GridPositionComponent;
import com.comp460.screens.tactics.components.map.MapPositionComponent;

/**
 * Created by matth on 4/9/2017.
 */
public class GridToScreenSystem extends IteratingSystem {

    private static final Family gridToScreenFamily = Family.all(GridPositionComponent.class, TransformComponent.class).exclude(ChildComponent.class).get();

    BattleScreen screen;

    public GridToScreenSystem(BattleScreen screen) {
        super(gridToScreenFamily);

        this.screen = screen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        screen.
        GridPositionComponent.get(entity).
    }
}
