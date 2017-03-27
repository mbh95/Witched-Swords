package com.comp460.screens.tactics.systems.cursor;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.cursor.MapCursorSelectionComponent;
import com.comp460.screens.tactics.components.cursor.MovementPathComponent;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.cursor.MapCursorComponent;
import com.comp460.screens.tactics.components.unit.*;
import com.comp460.screens.tactics.factories.ActionMenuFactory;
import com.comp460.screens.tactics.systems.map.ValidMoveManagementSystem;

import java.util.Set;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class MapCursorSelectionSystem extends IteratingSystem {

    private static final Family mapCursorFamily = Family.all(MapCursorComponent.class, MapPositionComponent.class).exclude(MovementPathComponent.class).get();
    private static final Family playerControlledFamily = Family.all(PlayerControlledComponent.class).get();

    private TacticsScreen parentScreen;

    public MapCursorSelectionSystem(TacticsScreen tacticsScreen) {
        super(mapCursorFamily);
        this.parentScreen = tacticsScreen;
    }

    @Override
    protected void processEntity(Entity cursor, float deltaTime) {
        MapPositionComponent cursorPos = MapPositionComponent.get(cursor);

        if (parentScreen.game.controller.button1JustPressedDestructive()) {
            Entity newSelection = parentScreen.getMap().getUnitAt(cursorPos.row, cursorPos.col);

            if (newSelection == null) {
                return;
            }
            parentScreen.clearSelections();

            MapCursorSelectionComponent newSelectionComponent = new MapCursorSelectionComponent();
            newSelectionComponent.selected = newSelection;

            newSelection.add(new SelectedComponent());
            newSelection.add(new ShowValidMovesComponent());

            cursor.add(newSelectionComponent);

            if (playerControlledFamily.matches(newSelection)) {
                MovementPathComponent path = new MovementPathComponent();
                path.positions.add(new MapPositionComponent(cursorPos.row, cursorPos.col));
                cursor.add(path);
            }
        }
    }
}
