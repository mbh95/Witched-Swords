package com.comp460.screens.tactics.systems.cursor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.cursor.*;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.unit.PlayerControlledComponent;
import com.comp460.screens.tactics.components.unit.ReadyToMoveComponent;
import com.comp460.screens.tactics.factories.ActionMenuFactory;
import com.comp460.screens.tactics.systems.map.ValidMoveManagementSystem;

import java.util.Set;

import static com.comp460.assets.SoundManager.selectionSound;

/**
 * Created by matth on 3/26/2017.
 */
public class MapCursorPathingSystem extends IteratingSystem {
    public static final Family pathingCursorFamily = Family.all(MapCursorComponent.class, MapPositionComponent.class, MovementPathComponent.class, MapCursorSelectionComponent.class).exclude(ActionMenuComponent.class).get();
    public static final Family readyPlayerControlledFamily = Family.all(PlayerControlledComponent.class, ReadyToMoveComponent.class).get();

    private TacticsScreen parentScreen;

    public MapCursorPathingSystem(TacticsScreen parentScreen) {
        super(pathingCursorFamily);
        this.parentScreen = parentScreen;
    }

    @Override
    protected void processEntity(Entity cursor, float deltaTime) {
//        if (parentScreen.curState == TacticsScreen.TacticsState.MENU) {
//            return;
//        }
        MovementPathComponent pathComponent = MovementPathComponent.get(cursor);
        MapPositionComponent cursorPos = MapPositionComponent.get(cursor);
        MapCursorSelectionComponent selectionComponent = MapCursorSelectionComponent.get(cursor);

        Entity oldSelection = selectionComponent.selected;
        Entity newSelection = parentScreen.getMap().getUnitAt(cursorPos.row, cursorPos.col);


        MapPositionComponent selectionPos = MapPositionComponent.get(oldSelection);

        Set<MapPositionComponent> validPositions = getEngine().getSystem(ValidMoveManagementSystem.class).getValidMoves(selectionComponent.selected);

        // Recalculate the path if the cursor moved and is still in range of the selected unit
        if (!cursorPos.equals(pathComponent.positions.get(pathComponent.positions.size() - 1)) && (validPositions.contains(cursorPos) || cursorPos.equals(selectionPos))) {
            pathComponent.positions = parentScreen.getMap().shortestPath(selectionComponent.selected, cursorPos);
        }

        if (parentScreen.game.controller.button1JustPressedDestructive()) {
            if (cursorPos.equals(MapPositionComponent.get(selectionComponent.selected)) || validPositions.contains(cursorPos)) { // Clicked on the selected unit or within the unit's range
                cursor.add(new LockedComponent());
                selectionSound.play(0.7f);
                cursor.add(ActionMenuFactory.makeActionMenu(cursor, parentScreen));

            } else if (newSelection != null) { // Clicked on another unit

                CursorManager.select(cursor, newSelection);

                cursor.remove(MovementPathComponent.class);

                if (readyPlayerControlledFamily.matches(newSelection)) {
                    cursor.add(new MovementPathComponent(cursorPos.row, cursorPos.col));
                }
            } else { // Clicked on an empty space outside the selected unit's range

            }
        }

        if (parentScreen.game.controller.button2JustPressedDestructive()) {
            CursorManager.deselect(cursor);

            cursor.remove(MovementPathComponent.class);
        }
    }
}
