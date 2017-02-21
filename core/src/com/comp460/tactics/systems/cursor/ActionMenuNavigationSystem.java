package com.comp460.tactics.systems.cursor;

import com.badlogic.ashley.core.Family;
import com.comp460.tactics.components.cursor.MapCursorComponent;
import com.comp460.tactics.components.map.MapPositionComponent;

/**
 * Created by matthewhammond on 1/25/17.
 */
public class ActionMenuNavigationSystem {
    private static final Family mapCursorFamily = Family.all(MapCursorComponent.class, MapPositionComponent.class).get();

}
