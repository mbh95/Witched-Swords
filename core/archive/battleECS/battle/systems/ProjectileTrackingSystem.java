package com.comp460.battle.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.battle.BattleScreen;
import com.comp460.battle.Mappers;
import com.comp460.battle.components.GridPositionComponent;
import com.comp460.battle.components.ProjectileComponent;
import com.comp460.common.components.TransformComponent;

/**
 * Created by matth on 2/15/2017.
 */
public class ProjectileTrackingSystem extends IteratingSystem {
    private static final Family gridToScreenFamily = Family.all(ProjectileComponent.class, GridPositionComponent.class, TransformComponent.class).get();

    private BattleScreen screen;

    public ProjectileTrackingSystem(BattleScreen screen) {
        super(gridToScreenFamily);

        this.screen = screen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transform = Mappers.transformM.get(entity);
        GridPositionComponent gridPos = Mappers.gridPosM.get(entity);
        ProjectileComponent projectile = Mappers.projectileM.get(entity);

        float t = (projectile.delay - projectile.countdown) / projectile.delay;
        int offsetX = projectile.dc == 0 ? 0 : -projectile.dc / Math.abs(projectile.dc);
        int offsetY = projectile.dr == 0 ? 0 : -projectile.dr / Math.abs(projectile.dr);
        transform.pos.x = screen.colToScreenX(gridPos.col + offsetX) + t * screen.tileWidth * projectile.dc;
        transform.pos.y = screen.rowToScreenY(gridPos.row + offsetY) + t * screen.tileHeight * projectile.dr;

    }
}
