package com.comp460.screens.tactics.systems.cursor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.cursor.LockedComponent;
import com.comp460.screens.tactics.components.cursor.MapCursorComponent;
import com.comp460.screens.tactics.components.cursor.MapCursorSelectionComponent;
import com.comp460.screens.tactics.components.cursor.MovementPathComponent;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.unit.PlayerControlledComponent;
import com.comp460.screens.tactics.components.unit.SelectedComponent;
import com.comp460.screens.tactics.components.unit.ShowValidMovesComponent;
import com.comp460.screens.tactics.factories.ActionMenuFactory;
import com.comp460.screens.tactics.systems.map.ValidMoveManagementSystem;

import java.util.Set;

/**
 * Created by matth on 3/26/2017.
 */
public class MapCursorPathingSystem extends IteratingSystem {
    private static final Family mapCursorFamily = Family.all(MapCursorComponent.class, MapPositionComponent.class, MovementPathComponent.class, MapCursorSelectionComponent.class).exclude(LockedComponent.class).get();
    private static final Family playerControlledFamily = Family.all(PlayerControlledComponent.class).get();

    private TacticsScreen parentScreen;

    public MapCursorPathingSystem(TacticsScreen parentScreen) {
        super(mapCursorFamily);
        this.parentScreen = parentScreen;
    }

    @Override
    protected void processEntity(Entity cursor, float deltaTime) {

        MovementPathComponent pathComponent = MovementPathComponent.get(cursor);
        MapPositionComponent cursorPos = MapPositionComponent.get(cursor);
        MapCursorSelectionComponent selectionComponent = MapCursorSelectionComponent.get(cursor);

        Set<MapPositionComponent> validPositions = getEngine().getSystem(ValidMoveManagementSystem.class).getValidMoves(selectionComponent.selected);

        if (validPositions.contains(cursorPos) || cursorPos.equals(MapPositionComponent.get(selectionComponent.selected))) {
            if (!cursorPos.equals(pathComponent.positions.get(pathComponent.positions.size() - 1))) {
                pathComponent.positions = parentScreen.getMap().shortestPath(selectionComponent.selected, cursorPos);
            }
            if (parentScreen.game.controller.button1JustPressedDestructive()) {
                cursor.add(new LockedComponent());
                cursor.add(ActionMenuFactory.makeActionMenu(cursor, parentScreen));
            }
        } else {
            if (parentScreen.game.controller.button1JustPressedDestructive() && parentScreen.getMap().getUnitAt(cursorPos.row, cursorPos.col) != null) {
                Entity newSelection = parentScreen.getMap().getUnitAt(cursorPos.row, cursorPos.col);
                parentScreen.clearSelections();

                MapCursorSelectionComponent newSelectionComponent = new MapCursorSelectionComponent();
                newSelectionComponent.selected = newSelection;

                newSelection.add(new SelectedComponent());
                newSelection.add(new ShowValidMovesComponent());

                cursor.add(newSelectionComponent);
                cursor.remove(MovementPathComponent.class);

                if (playerControlledFamily.matches(newSelection)) {
                    MovementPathComponent path = new MovementPathComponent();
//                    path.positions.add(new MapPositionComponent(cursorPos.row, cursorPos.col));
                    cursor.add(path);
                }
            }
        }

        if (parentScreen.game.controller.button2JustPressedDestructive()) {
            parentScreen.clearSelections();
            cursor.remove(MapCursorSelectionComponent.class);
            cursor.remove(MovementPathComponent.class);
        }
    }
}
