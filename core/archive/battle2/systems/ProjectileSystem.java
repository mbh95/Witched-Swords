package com.comp460.archive.battle2.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.archive.battle2.BattleScreen;
import com.comp460.archive.battle2.components.LocationComponent;
import com.comp460.archive.battle2.components.ProjectileComponent;
import com.comp460.archive.battle2.components.WarningComponent;

/**
 * Created by matth on 2/13/2017.
 */
public class ProjectileSystem extends IteratingSystem {
    private static final Family projectileFamily = Family.all(ProjectileComponent.class, LocationComponent.class).exclude(WarningComponent.class).get();

    private static final ComponentMapper<ProjectileComponent> projectileM = ComponentMapper.getFor(ProjectileComponent.class);
    private static final ComponentMapper<LocationComponent> locM = ComponentMapper.getFor(LocationComponent.class);

    private BattleScreen screen;

    public ProjectileSystem(BattleScreen screen) {
        super(projectileFamily);
        this.screen = screen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ProjectileComponent proj = projectileM.get(entity);
        proj.countdown -= deltaTime;
        if (proj.countdown <= 0) {
            LocationComponent loc = locM.get(entity);
            proj.countdown = proj.delay;
            loc.row += proj.dr;
            loc.col += proj.dc;
            if (!screen.grid.isOnGrid(loc.row, loc.col)) {
                this.getEngine().removeEntity(entity);
            }
        }
    }
}
