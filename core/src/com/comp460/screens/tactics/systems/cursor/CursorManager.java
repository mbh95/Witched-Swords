package com.comp460.screens.tactics.systems.cursor;

import com.badlogic.ashley.core.Entity;
import com.comp460.screens.tactics.components.cursor.MapCursorSelectionComponent;
import com.comp460.screens.tactics.components.cursor.MovementPathComponent;
import com.comp460.screens.tactics.components.unit.SelectedComponent;
import com.comp460.screens.tactics.components.unit.ShowValidMovesComponent;

/**
 * Created by matthewhammond on 3/29/17.
 */
public class CursorManager {

    public static void deselect(Entity cursor) {
        MapCursorSelectionComponent selectionComponent = MapCursorSelectionComponent.get(cursor);
        if (selectionComponent != null) {
            Entity selection = selectionComponent.selected;
            selection.remove(SelectedComponent.class);
            selection.remove(ShowValidMovesComponent.class);
            cursor.remove(MovementPathComponent.class);
            cursor.remove(MapCursorSelectionComponent.class);
        }
    }

    public static void select(Entity cursor, Entity newSelection) {
        deselect(cursor);
        cursor.add(new MapCursorSelectionComponent(newSelection));
        newSelection.add(new SelectedComponent());
        newSelection.add(new ShowValidMovesComponent());
    }
}
