package com.comp460.screens.tactics.systems.game;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.common.input.Controller;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.cursor.MapCursorSelectionComponent;
import com.comp460.screens.tactics.components.cursor.MovementPathComponent;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.unit.QueuedMoveComponent;
import com.comp460.screens.tactics.components.unit.SelectedComponent;
import com.comp460.screens.tactics.components.unit.ShowValidMovesComponent;
import com.comp460.screens.tactics.systems.cursor.PathBuildingSystem;

/**
 * Created by matth on 3/21/2017.
 */
public class MoveActionSystem extends IteratingSystem {

    private static final Family actionMenuFamily = Family.all(QueuedMoveComponent.class).get();

    private static ComponentMapper<QueuedMoveComponent> actionM = ComponentMapper.getFor(QueuedMoveComponent.class);
    private static ComponentMapper<MovementPathComponent> pathM = ComponentMapper.getFor(MovementPathComponent.class);

    public TacticsScreen screen;

    public MoveActionSystem(TacticsScreen screen) {
        super(actionMenuFamily);
        this.screen = screen;
    }

    public enum Action {
        WAIT("Wait"),
        ATTACK("Attack");


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
    protected void processEntity(Entity entity, float deltaTime) {
        Controller controller = screen.game.controller;
        QueuedMoveComponent actionMenu = actionM.get(entity);
        if (controller.upJustPressed() && actionMenu.selectedAction < actionMenu.actions.size() - 1) {
            actionMenu.selectedAction++;
        }
        if (controller.downJustPressed() && actionMenu.selectedAction > 0) {
            actionMenu.selectedAction--;
        }
        if (controller.button1JustPressed() && actionMenu.selectedAction > 0) {
            switch (actionMenu.actions.get(actionMenu.selectedAction)) {
                case WAIT:
                    MovementPathComponent path = pathM.get(entity);
                    MapPositionComponent goal = path.positions.get(path.positions.size() - 1);
                    screen.getMap().move(entity, goal.row, goal.col);
                    entity.remove(QueuedMoveComponent.class);
                    entity.remove(MovementPathComponent.class);
                    screen.clearSelections();
                    break;
            }
        }
        if (controller.button2JustPressed()) {
            entity.remove(QueuedMoveComponent.class);
            entity.remove(MovementPathComponent.class);
        }
    }
}
