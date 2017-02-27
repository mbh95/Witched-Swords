package com.comp460.screens.tactics.systems.unit;

import com.badlogic.ashley.core.*;
import com.comp460.screens.tactics.TacticsMap;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.unit.UnitStatsComponent;

/**
 * Created by matth on 2/22/2017.
 */
public class MapManagementSystem extends EntitySystem implements EntityListener {

    private static final Family mapUnitsFamily = Family.all(UnitStatsComponent.class, MapPositionComponent.class).get();

    private static final ComponentMapper<MapPositionComponent> mapPosM = ComponentMapper.getFor(MapPositionComponent.class);

    private TacticsMap map;

    public MapManagementSystem(TacticsScreen screen) {
        this.map = screen.getMap();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(mapUnitsFamily, this);
    }

    @Override
    public void entityAdded(Entity entity) {
        MapPositionComponent mapPos = mapPosM.get(entity);
        map.move(entity, mapPos.row, mapPos.col);
    }

    @Override
    public void entityRemoved(Entity entity) {
        map.remove(entity);
    }
}
