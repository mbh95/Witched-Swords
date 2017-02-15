package com.comp460.battle.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.comp460.battle.BattleScreen;
import com.comp460.battle.Mappers;
import com.comp460.battle.components.GridPositionComponent;
import com.comp460.battle.components.ProjectileComponent;
import com.comp460.common.components.TransformComponent;

/**
 * Created by matth on 2/13/2017.
 */
public class GridToScreenSystem extends IteratingSystem {

    private static final Family gridToScreenFamily = Family.all(GridPositionComponent.class, TransformComponent.class).exclude(ProjectileComponent.class).get();

    private BattleScreen screen;

    public GridToScreenSystem(BattleScreen screen) {
        super(gridToScreenFamily);

        this.screen = screen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        GridPositionComponent gridPosition = Mappers.gridPosM.get(entity);
        TransformComponent transform = Mappers.transformM.get(entity);
        transform.pos.slerp(new Vector3(screen.colToScreenX(gridPosition.col), screen.rowToScreenY(gridPosition.row), transform.pos.z), 0.2f);
    }
}
