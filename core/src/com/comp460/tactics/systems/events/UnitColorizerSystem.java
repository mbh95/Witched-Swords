package com.comp460.tactics.systems.events;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Color;
import com.comp460.tactics.components.ColorComponent;
import com.comp460.tactics.components.MapPositionComponent;
import com.comp460.tactics.components.unit.ReadyToMoveComponent;
import com.comp460.tactics.components.unit.UnitStatsComponent;

/**
 * Created by matthewhammond on 2/20/17.
 */
public class UnitColorizerSystem implements EntityListener {

    private static final Family waitingUnitsFamily = Family.all(MapPositionComponent.class, UnitStatsComponent.class).exclude(ReadyToMoveComponent.class).get();

    public static void register(Engine engine) {
        engine.addEntityListener(waitingUnitsFamily, new UnitColorizerSystem());
    }

    @Override
    public void entityAdded(Entity entity) {
        entity.add(new ColorComponent().populate(Color.GRAY));
        System.out.println(entity.getComponent(UnitStatsComponent.class).base.curHP);
    }

    @Override
    public void entityRemoved(Entity entity) {
        entity.remove(ColorComponent.class);
    }
}
