package com.comp460.tactics.components.map;

import com.badlogic.ashley.core.Component;

/**
 * Attached to any entities which live on the map. Encodes a row and a column.
 */
public class MapPositionComponent implements Component {
    public static final int MAX_MAP_WIDTH = 10000;
    public int row, col;

    public MapPositionComponent(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public int hashCode() {
        return (MAX_MAP_WIDTH * row + col);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MapPositionComponent)) {
            return false;
        } else {
            MapPositionComponent other = (MapPositionComponent) o;
            return (other.row == this.row && other.col == this.col);
        }
    }
}
