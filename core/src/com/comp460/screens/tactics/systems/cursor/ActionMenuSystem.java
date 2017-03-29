package com.comp460.screens.tactics.systems.cursor;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.common.input.Controller;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.cursor.LockedComponent;
import com.comp460.screens.tactics.components.cursor.MapCursorSelectionComponent;
import com.comp460.screens.tactics.components.cursor.MovementPathComponent;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.cursor.ActionMenuComponent;
import com.comp460.screens.tactics.components.unit.UnitStatsComponent;

/**
 * Created by matth on 3/27/2017.
 */
public class ActionMenuSystem extends IteratingSystem {

    private static final Family actionMenuFamily = Family.all(ActionMenuComponent.class, MovementPathComponent.class, MapCursorSelectionComponent.class).get();

    public TacticsScreen screen;

    public ActionMenuSystem(TacticsScreen screen) {
        super(actionMenuFamily);
        this.screen = screen;
    }

    public enum Action {
        WAIT("Wait"),
        ATTACK_UP("Attack Up"),
        ATTACK_DOWN("Attack Down"),
        ATTACK_LEFT("Attack Left"),
        ATTACK_RIGHT("Attack Right"),
        CANCEL("Cancel");

        private final String name;

        Action(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    protected void processEntity(Entity cursor, float deltaTime) {
        if (screen.curState == TacticsScreen.TacticsState.MENU) {
            return;
        }
        Controller controller = screen.game.controller;
        ActionMenuComponent actionMenu = ActionMenuComponent.get(cursor);
        MovementPathComponent path = MovementPathComponent.get(cursor);
        MapCursorSelectionComponent selectionComponent = MapCursorSelectionComponent.get(cursor);

        if (controller.upJustPressed() && actionMenu.selectedAction < actionMenu.actions.size() - 1) {
            actionMenu.selectedAction++;
        }
        if (controller.downJustPressed() && actionMenu.selectedAction > 0) {
            actionMenu.selectedAction--;
        }
        if (controller.button1JustPressedDestructive()) {
            MapPositionComponent goal = path.positions.get(path.positions.size() - 1);
            UnitStatsComponent playerStats = UnitStatsComponent.get(selectionComponent.selected);
            UnitStatsComponent aiStats;
            switch (actionMenu.actions.get(actionMenu.selectedAction)) {
                case WAIT:
                    screen.getMap().move(selectionComponent.selected, goal.row, goal.col);
                    break;
                case ATTACK_UP:
                    screen.getMap().move(selectionComponent.selected, goal.row, goal.col);
                    screen.transitionToBattleView(selectionComponent.selected, screen.getMap().getUnitAt(goal.row + 1, goal.col), true);
                    break;
                case ATTACK_DOWN:
                    screen.getMap().move(selectionComponent.selected, goal.row, goal.col);
                    screen.transitionToBattleView(selectionComponent.selected, screen.getMap().getUnitAt(goal.row - 1, goal.col), true);
                    break;
                case ATTACK_LEFT:
                    screen.getMap().move(selectionComponent.selected, goal.row, goal.col);
                    screen.transitionToBattleView(selectionComponent.selected, screen.getMap().getUnitAt(goal.row, goal.col - 1), true);

                    break;
                case ATTACK_RIGHT:
                    screen.getMap().move(selectionComponent.selected, goal.row, goal.col);
                    screen.transitionToBattleView(selectionComponent.selected, screen.getMap().getUnitAt(goal.row, goal.col + 1), true);
                    break;
                case CANCEL:
                    break;
            }
            CursorManager.deselect(cursor);
            cursor.remove(ActionMenuComponent.class);
            cursor.remove(LockedComponent.class);
        }
        if (controller.button2JustPressedDestructive()) {
            cursor.remove(ActionMenuComponent.class);
            cursor.remove(LockedComponent.class);
        }
    }
}