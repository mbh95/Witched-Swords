package com.comp460.screens.battleECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.Mappers;
import com.comp460.screens.battle.components.*;

/**
 * Created by matth on 2/13/2017.
 */
public class DamageSystem extends IteratingSystem {

    private static final Family damagingFamily = Family.all(DamageComponent.class, GridPositionComponent.class).exclude(WarningComponent.class).get();
    private static final Family damageableFamily = Family.all(GridPositionComponent.class, HealthComponent.class).get();

    private BattleScreen screen;

    public DamageSystem(BattleScreen screen) {
        super(damagingFamily);
        this.screen = screen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        GridPositionComponent gridPos = Mappers.gridPosM.get(entity);
        DamageComponent damage = Mappers.damageM.get(entity);
        OwnerComponent ownerComponent = Mappers.ownerM.get(entity);

        for (Entity u : screen.engine.getEntitiesFor(damageableFamily)) {
            GridPositionComponent uPos = Mappers.gridPosM.get(u);
            if (uPos.row == gridPos.row && uPos.col == gridPos.col) {


                if (ownerComponent == null || ownerComponent.owner != u) {
                    hurt(u, damage.amount);
                    if (damage.destroyOnHit) {
                        entity.remove(DamageComponent.class);
                    }
                    if (damage.lifeSteal != 0 && ownerComponent != null) {
                        heal(ownerComponent.owner, damage.lifeSteal);
                    }
                }
            }
        }
    }

    public void heal(Entity e, int amount) {
        HealthComponent health = Mappers.healthM.get(e);
        if (health != null) {
            health.curHP += amount;
            health.curHP = Math.min(health.curHP, health.maxHP);
        }
    }

    private void hurt(Entity e, int amount) {
        HealthComponent health = Mappers.healthM.get(e);
        if (health != null) {
            health.curHP -= amount;
            health.curHP = Math.max(health.curHP, 0);
        }
    }
}
