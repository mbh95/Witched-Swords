package com.comp460.archive.battle2.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.archive.battle2.BattleScreen;
import com.comp460.archive.battle2.BattleUnit;
import com.comp460.archive.battle2.components.DamageComponent;
import com.comp460.archive.battle2.components.LocationComponent;
import com.comp460.archive.battle2.components.OwnerComponent;
import com.comp460.archive.battle2.components.WarningComponent;

/**
 * Created by matth on 2/13/2017.
 */
public class DamageSystem extends IteratingSystem {

    private static final Family damagingFamily = Family.all(DamageComponent.class, LocationComponent.class).exclude(WarningComponent.class).get();

    private static final ComponentMapper<DamageComponent> damageM = ComponentMapper.getFor(DamageComponent.class);
    private static final ComponentMapper<LocationComponent> locM = ComponentMapper.getFor(LocationComponent.class);
    private static final ComponentMapper<OwnerComponent> ownerM = ComponentMapper.getFor(OwnerComponent.class);


    private BattleScreen screen;

    public DamageSystem(BattleScreen screen) {
        super(damagingFamily);
        this.screen = screen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        LocationComponent loc = locM.get(entity);
        DamageComponent damage = damageM.get(entity);
        OwnerComponent owner = ownerM.get(entity);

        for (BattleUnit u : screen.grid.units) {
            if (loc.row == u.getGridRow() && loc.col == u.getGridCol()) {
                if (owner == null || (owner != null && owner.owner != u)) {
                    u.hurt(damage.amount);
                    if (damage.removeOnContact) {
                        entity.remove(DamageComponent.class);
                    }
                    if (damage.lifesteal != 0 && owner != null) {
                        owner.owner.heal(damage.lifesteal);
                    }
                }
            }
        }
    }
}
