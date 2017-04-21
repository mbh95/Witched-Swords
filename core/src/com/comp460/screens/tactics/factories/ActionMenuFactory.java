package com.comp460.screens.tactics.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.cursor.ActionMenuComponent;
import com.comp460.screens.tactics.components.cursor.MapCursorSelectionComponent;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.unit.AIControlledComponent;
import com.comp460.screens.tactics.components.unit.CanHealComponent;
import com.comp460.screens.tactics.components.unit.PlayerControlledComponent;
import com.comp460.screens.tactics.components.unit.UnitStatsComponent;
import com.comp460.screens.tactics.systems.cursor.ActionMenuSystem;
import com.comp460.screens.tactics.systems.cursor.MapCursorSelectionSystem;


/**
 * Created by matth on 3/22/2017.
 */
public class ActionMenuFactory {

    private static final Family aiControlledFamily = Family.all(AIControlledComponent.class).get();
    private static final Family playerControlledFamily = Family.all(PlayerControlledComponent.class, UnitStatsComponent.class).get();

    public static ActionMenuComponent makeActionMenu(Entity cursor, TacticsScreen screen) {

        ActionMenuComponent moveComponent = new ActionMenuComponent();

        MapCursorSelectionComponent selectionComponent = cursor.getComponent(MapCursorSelectionComponent.class);
        Entity selection = selectionComponent.selected;

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

        if (CanHealComponent.get(selection) != null) {

            if (UnitStatsComponent.get(selection).base.curHP < UnitStatsComponent.get(selection).base.maxHP) {
                moveComponent.actions.add(ActionMenuSystem.Action.HEAL_SELF);
            }

            unit = screen.getMap().getUnitAt(cursorPos.row + 1, cursorPos.col);
            if (unit != null && unit != selection && playerControlledFamily.matches(unit) && UnitStatsComponent.get(unit).base.curHP < UnitStatsComponent.get(unit).base.maxHP) {
                moveComponent.actions.add(ActionMenuSystem.Action.HEAL_UP);
            }
            unit = screen.getMap().getUnitAt(cursorPos.row, cursorPos.col + 1);
            if (unit != null && unit != selection && playerControlledFamily.matches(unit) && UnitStatsComponent.get(unit).base.curHP < UnitStatsComponent.get(unit).base.maxHP) {
                moveComponent.actions.add(ActionMenuSystem.Action.HEAL_RIGHT);
            }
            unit = screen.getMap().getUnitAt(cursorPos.row - 1, cursorPos.col);
            if (unit != null && unit != selection && playerControlledFamily.matches(unit) && UnitStatsComponent.get(unit).base.curHP < UnitStatsComponent.get(unit).base.maxHP) {
                moveComponent.actions.add(ActionMenuSystem.Action.HEAL_DOWN);
            }
            unit = screen.getMap().getUnitAt(cursorPos.row, cursorPos.col - 1);
            if (unit != null && unit != selection && playerControlledFamily.matches(unit) && UnitStatsComponent.get(unit).base.curHP < UnitStatsComponent.get(unit).base.maxHP) {
                moveComponent.actions.add(ActionMenuSystem.Action.HEAL_LEFT);
            }
        }

        moveComponent.selectedAction = moveComponent.actions.size()-1;
        return moveComponent;
    }
}
