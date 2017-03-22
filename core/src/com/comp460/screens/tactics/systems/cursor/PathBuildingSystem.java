package com.comp460.screens.tactics.systems.cursor;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.common.GameUnit;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.cursor.MapCursorComponent;
import com.comp460.screens.tactics.components.cursor.MapCursorSelectionComponent;
import com.comp460.screens.tactics.components.cursor.MovementPathComponent;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.unit.PlayerControlledComponent;
import com.comp460.screens.tactics.components.unit.ReadyToMoveComponent;
import com.comp460.screens.tactics.components.unit.SelectedComponent;
import com.comp460.screens.tactics.components.unit.UnitStatsComponent;
import com.comp460.screens.tactics.systems.map.ValidMoveManagementSystem;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by matthewhammond on 3/1/17.
 */
public class PathBuildingSystem extends IteratingSystem {

    private static final Family selectedCursorFamily = Family.all(MapCursorComponent.class, MapPositionComponent.class, MapCursorSelectionComponent.class).get();
    private static final Family pathingCursorFamily = Family.all(MapCursorComponent.class, MapPositionComponent.class, MapCursorSelectionComponent.class, MovementPathComponent.class).get();
    private static final Family readyPlayerControlledFamily = Family.all(ReadyToMoveComponent.class, PlayerControlledComponent.class).get();

    private static final ComponentMapper<MapCursorSelectionComponent> selectionM = ComponentMapper.getFor(MapCursorSelectionComponent.class);
    private static final ComponentMapper<MapPositionComponent> posM = ComponentMapper.getFor(MapPositionComponent.class);
    private static final ComponentMapper<UnitStatsComponent> statsM = ComponentMapper.getFor(UnitStatsComponent.class);
    private static final ComponentMapper<MovementPathComponent> pathM = ComponentMapper.getFor(MovementPathComponent.class);

    private TacticsScreen screen;

    public PathBuildingSystem(TacticsScreen screen) {
        super(pathingCursorFamily);
        this.screen = screen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        Entity selection = selectionM.get(entity).selected;
        MapPositionComponent cursorPos = posM.get(entity);
        MovementPathComponent path = pathM.get(entity);
        ValidMoveManagementSystem moveManager = getEngine().getSystem(ValidMoveManagementSystem.class);
        Set<MapPositionComponent> selectionValidMoves = moveManager.getValidMoves(selection);

        // If the cursor is outside the moveable area
        if (!selectionValidMoves.contains(cursorPos)) {
            return;
        }

        // If the cursor is already at the end of the path then we don't need to update
//            if (path.positions.get(path.positions.size() - 1).equals(cursorPos)) {
//                return;
//            }

        // If the path is running into itself:
        if (path.positions.contains(cursorPos)) {
            // Truncate the path at the cursor position
            truncate(path.positions, cursorPos);
            return;
        }

        // If the cursor moved to an adjacent square:
        if (path.positions.get(path.positions.size() - 1).equals(new MapPositionComponent(cursorPos.row - 1, cursorPos.col)) ||
                path.positions.get(path.positions.size() - 1).equals(new MapPositionComponent(cursorPos.row + 1, cursorPos.col)) ||
                path.positions.get(path.positions.size() - 1).equals(new MapPositionComponent(cursorPos.row, cursorPos.col - 1)) ||
                path.positions.get(path.positions.size() - 1).equals(new MapPositionComponent(cursorPos.row, cursorPos.col + 1))) {
            if (path.positions.size() - 1 >= statsM.get(selection).base.moveDist && false) {
                Entity dummy = new Entity();
                dummy.add(cursorPos);
                GameUnit dummyUnit = new GameUnit();
                dummyUnit.moveDist = statsM.get(selection).base.moveDist;
                dummy.add(new UnitStatsComponent(-1, dummyUnit));

                Map<MapPositionComponent, Integer> shortestPaths = screen.getMap().shortestPaths(dummy);

                path.positions.clear();

                MapPositionComponent unitPos = posM.get(selection);

                path.positions.add(new MapPositionComponent(unitPos.row, unitPos.col));

                MapPositionComponent start = path.positions.get(path.positions.size() - 1);
                while (!start.equals(cursorPos)) {
                    int cost = shortestPaths.get(start);
                    MapPositionComponent pos = start;
                    if (shortestPaths.getOrDefault(new MapPositionComponent(pos.row + 1, pos.col), -1) == cost + 1) {
                        path.positions.add(new MapPositionComponent(pos.row + 1, pos.col));
                    } else if (shortestPaths.getOrDefault(new MapPositionComponent(pos.row - 1, pos.col), -1) == cost + 1) {
                        path.positions.add(new MapPositionComponent(pos.row - 1, pos.col));
                    } else if (shortestPaths.getOrDefault(new MapPositionComponent(pos.row, pos.col + 1), -1) == cost + 1) {
                        path.positions.add(new MapPositionComponent(pos.row, pos.col + 1));
                    } else if (shortestPaths.getOrDefault(new MapPositionComponent(pos.row, pos.col - 1), -1) == cost + 1) {
                        path.positions.add(new MapPositionComponent(pos.row, pos.col - 1));
                    }
                    start = path.positions.get(path.positions.size() - 1);
                }
            } else {
                path.positions.add(new MapPositionComponent(cursorPos.row, cursorPos.col));
            }
        } else {
            Entity dummy = new Entity();
            dummy.add(cursorPos);
            GameUnit dummyUnit = new GameUnit();
            dummyUnit.moveDist = statsM.get(selection).base.moveDist;
            dummy.add(new UnitStatsComponent(-1, dummyUnit));

            Map<MapPositionComponent, Integer> shortestPaths = screen.getMap().shortestPaths(dummy);

            path.positions.clear();

            MapPositionComponent unitPos = posM.get(selection);

            path.positions.add(new MapPositionComponent(unitPos.row, unitPos.col));

            MapPositionComponent start = path.positions.get(path.positions.size() - 1);
            while (!start.equals(cursorPos)) {
                int cost = shortestPaths.get(start);
                MapPositionComponent pos = start;
                if (shortestPaths.getOrDefault(new MapPositionComponent(pos.row + 1, pos.col), -1) == cost + 1) {
                    path.positions.add(new MapPositionComponent(pos.row + 1, pos.col));
                } else if (shortestPaths.getOrDefault(new MapPositionComponent(pos.row - 1, pos.col), -1) == cost + 1) {
                    path.positions.add(new MapPositionComponent(pos.row - 1, pos.col));
                } else if (shortestPaths.getOrDefault(new MapPositionComponent(pos.row, pos.col + 1), -1) == cost + 1) {
                    path.positions.add(new MapPositionComponent(pos.row, pos.col + 1));
                } else if (shortestPaths.getOrDefault(new MapPositionComponent(pos.row, pos.col - 1), -1) == cost + 1) {
                    path.positions.add(new MapPositionComponent(pos.row, pos.col - 1));
                }
                start = path.positions.get(path.positions.size() - 1);
            }

//                for (int i = path.positions.size() - 1; i >= 0; i--) {
//                    if (shortestPaths.containsKey(path.positions.get(i))) {
//                        truncate(path.positions, path.positions.get(i));
//                        connectToPath(path.positions, shortestPaths, cursorPos);
//                        return;
//                    }
//                }
        }
    }

    public void truncate(List<MapPositionComponent> path, MapPositionComponent pos) {
        int collisionIndex = path.indexOf(pos);
        if (collisionIndex < 0) {
            return;
        }
        while (path.size() - 1 > collisionIndex) {
            path.remove(path.size() - 1);
        }
        return;
    }
}
