package com.comp460.tactics.components.map;

import com.badlogic.ashley.core.Component;
import com.comp460.tactics.map.MapPosition;
import com.comp460.tactics.map.TacticsMap;

/**
 * Attached to any entities which live on the map. Encodes a row and a column.
 */
public class MapPositionComponent implements Component {
    public int row, col;

    public MapPositionComponent(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
