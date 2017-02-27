package com.comp460.screens.battleECS.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.Mappers;
import com.comp460.screens.battleECS.components.GridPositionComponent;
import com.comp460.screens.battleECS.components.ProjectileComponent;
import com.comp460.screens.battleECS.components.WarningComponent;

/**
 * Created by matth on 2/13/2017.
 */
public class ProjectileSystem extends IteratingSystem {
    private static final Family projectileFamily = Family.all(ProjectileComponent.class, GridPositionComponent.class).exclude(WarningComponent.class).get();

    private BattleScreen screen;

    public ProjectileSystem(BattleScreen screen) {
        super(projectileFamily);
        this.screen = screen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ProjectileComponent proj = Mappers.projectileM.get(entity);
        proj.countdown -= deltaTime;

        if (proj.countdown <= 0) {
            GridPositionComponent loc = Mappers.gridPosM.get(entity);
            proj.countdown = proj.delay;
            loc.row += proj.dr;
            loc.col += proj.dc;
            if (!screen.isOnGrid(loc.row, loc.col)) {
                this.getEngine().removeEntity(entity);
            }
        }
    }
}
