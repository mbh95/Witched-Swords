package com.comp460.screens.tactics.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.cursor.QueuedMoveComponent;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.unit.AIControlledComponent;
import com.comp460.screens.tactics.systems.cursor.ActionMenuSystem;


/**
 * Created by matth on 3/22/2017.
 */
public class ActionMenuFactory {

    private static final Family aiControlledFamily = Family.all(AIControlledComponent.class).get();

    public static QueuedMoveComponent makeActionMenu(Entity cursor, TacticsScreen screen) {

        QueuedMoveComponent moveComponent = new QueuedMoveComponent();

        moveComponent.actions.add(ActionMenuSystem.Action.CANCEL);
        moveComponent.actions.add(ActionMenuSystem.Action.WAIT);

        MapPositionComponent cursorPos = MapPositionComponent.get(cursor);
        Entity unit = screen.getMap().getUnitAt(cursorPos.row + 1, cursorPos.col);
        if (unit != null && aiControlledFamily.matches(unit)) {
            moveComponent.actions.add(ActionMenuSystem.Action.ATTACK_UP);
        }
        unit = screen.getMap().getUnitAt(cursorPos.row, cursorPos.col + 1);
        if (unit != null && aiControlledFamily.matches(unit)) {
            moveComponent.actions.add(ActionMenuSystem.Action.ATTACK_RIGHT);
        }
        unit = screen.getMap().getUnitAt(cursorPos.row - 1, cursorPos.col);
        if (unit != null && aiControlledFamily.matches(unit)) {
            moveComponent.actions.add(ActionMenuSystem.Action.ATTACK_DOWN);
        }
        unit = screen.getMap().getUnitAt(cursorPos.row, cursorPos.col - 1);
        if (unit != null && aiControlledFamily.matches(unit)) {
            moveComponent.actions.add(ActionMenuSystem.Action.ATTACK_LEFT);
        }
        return moveComponent;
    }
}
