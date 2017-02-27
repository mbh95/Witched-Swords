package com.comp460.screens.tactics.systems.cursor;

import com.badlogic.ashley.core.*;
import com.comp460.screens.tactics.components.cursor.MapCursorComponent;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.unit.UnitStatsComponent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by matth on 2/22/2017.
 */
public class CursorManagementSystem extends EntitySystem implements EntityListener {
    private static final Family cursorFamily = Family.all(MapCursorComponent.class).get();
    private static final Family mapUnitsFamily = Family.all(UnitStatsComponent.class, MapPositionComponent.class).get();

    private static final ComponentMapper<MapCursorComponent> cursorM = ComponentMapper.getFor(MapCursorComponent.class);

    Set<Entity> cursors = new HashSet<>();

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(mapUnitsFamily, this);
        engine.addEntityListener(cursorFamily, new CursorRegistrationSystem(this));
    }

    @Override
    public void entityAdded(Entity entity) {
    }

    @Override
    public void entityRemoved(Entity entity) {
        for (Iterator<Entity> iter = cursors.iterator(); iter.hasNext();) {
            Entity cursorEntity = iter.next();
            MapCursorComponent cursor = cursorM.get(cursorEntity);
            if (cursor.hovered == entity) {
                cursor.hovered = null;
            }
            if (cursor.selection == entity) {
                cursor.selection = null;
            }
        }
    }

    class CursorRegistrationSystem implements EntityListener {

        private CursorManagementSystem parent;

        public CursorRegistrationSystem(CursorManagementSystem parent) {
            this.parent = parent;
        }

        @Override
        public void entityAdded(Entity entity) {
            this.parent.cursors.add(entity);
        }

        @Override
        public void entityRemoved(Entity entity) {
            this.parent.cursors.remove(entity);
        }
    }
}
