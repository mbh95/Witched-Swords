package com.comp460.screens.tactics.systems.map;

import com.badlogic.ashley.core.*;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.unit.ReadyToMoveComponent;
import com.comp460.screens.tactics.components.unit.UnitStatsComponent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by matthewhammond on 2/27/17.
 */
public class ValidMoveManagementSystem extends EntitySystem implements EntityListener {

    private static final Family readyUnitFamily = Family.all(ReadyToMoveComponent.class).get();

    private static final Family unitFamily = Family.all(UnitStatsComponent.class, MapPositionComponent.class).get();

    private Map<Entity, Set<MapPositionComponent>> unitToMoves = new HashMap<>();

    public TacticsScreen screen;

    public ValidMoveManagementSystem(TacticsScreen screen) {
        this.screen = screen;
    }

    public void rebuildMoves() {
        this.getEngine().getEntitiesFor(unitFamily).forEach(e->{
            this.unitToMoves.put(e, this.screen.getMap().computeValidMoves(e));
        });
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(readyUnitFamily, this);
        engine.addEntityListener(unitFamily, this);
        this.rebuildMoves();
    }

    @Override
    public void entityAdded(Entity entity) {
        this.rebuildMoves();
    }

    @Override
    public void entityRemoved(Entity entity) {
        this.rebuildMoves();
    }

    public Set<MapPositionComponent> getValidMoves(Entity entity) {
        if (unitToMoves.containsKey(entity)) {
            return unitToMoves.get(entity);
        } else {
            return new HashSet<>();
        }
    }
}
