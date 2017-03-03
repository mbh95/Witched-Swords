package com.comp460.screens.tactics.systems.cursor;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.screens.tactics.components.cursor.MapCursorComponent;
import com.comp460.screens.tactics.components.cursor.MovementPathComponent;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.systems.map.ValidMoveManagementSystem;

import java.util.Set;

/**
 * Created by matthewhammond on 3/1/17.
 */
public class PathBuildingSystem extends IteratingSystem {

    private static final Family cursorFamily = Family.all(MapCursorComponent.class, MapPositionComponent.class).get();
    private static final Family pathingUnitFamily = Family.all(MovementPathComponent.class).get();

    private static final ComponentMapper<MapCursorComponent> cursorM = ComponentMapper.getFor(MapCursorComponent.class);
    private static final ComponentMapper<MapPositionComponent> posM = ComponentMapper.getFor(MapPositionComponent.class);
    private static final ComponentMapper<MovementPathComponent> pathM = ComponentMapper.getFor(MovementPathComponent.class);

    public PathBuildingSystem() {
        super(cursorFamily);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MapCursorComponent cursor = cursorM.get(entity);
        // If you have a unit that is currently building a path selected:
        if (cursor.selection!= null && pathingUnitFamily.matches(cursor.selection)) {
            MapPositionComponent cursorPos = posM.get(entity);
            MovementPathComponent path = pathM.get(cursor.selection);
            // If the cursor is already at the end of the path then we don't need to update
            if (path.positions.get(path.positions.size() - 1).equals(cursorPos)) {
                return;
            }
            ValidMoveManagementSystem moveManager = getEngine().getSystem(ValidMoveManagementSystem.class);
            Set<MapPositionComponent> selectionValidMoves = moveManager.getValidMoves(cursor.selection);

            // If the path is running into itself:
            if (selectionValidMoves.contains(cursorPos)){
//                sel
//                if ()
            }
        }
    }
}
