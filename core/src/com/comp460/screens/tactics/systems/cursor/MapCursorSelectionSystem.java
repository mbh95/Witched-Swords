package com.comp460.screens.tactics.systems.cursor;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.cursor.ActionMenuComponent;
import com.comp460.screens.tactics.components.cursor.MovementPathComponent;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.cursor.MapCursorComponent;
import com.comp460.screens.tactics.components.unit.*;

import static com.comp460.assets.SoundManager.selectionSound;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class MapCursorSelectionSystem extends IteratingSystem {

    private static final Family mapCursorFamily = Family.all(MapCursorComponent.class, MapPositionComponent.class).exclude(MovementPathComponent.class, ActionMenuComponent.class).get();
    private static final Family readyPlayerControlledFamily = Family.all(PlayerControlledComponent.class, ReadyToMoveComponent.class).get();

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
            CursorManager.select(cursor, newSelection);
            selectionSound.play(0.7f);

            if (readyPlayerControlledFamily.matches(newSelection)) {
                cursor.add(new MovementPathComponent(cursorPos.row, cursorPos.col));
            }
        }

        if (parentScreen.game.controller.button2JustPressedDestructive()) {
            CursorManager.deselect(cursor);
        }
    }
}
