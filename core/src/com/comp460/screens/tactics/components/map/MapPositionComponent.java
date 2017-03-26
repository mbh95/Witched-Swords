package com.comp460.screens.tactics.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

/**
 * Attached to any entities which live on the map. Encodes a row and a column.
 */
public class MapPositionComponent implements Component {
    public static final int MAX_MAP_WIDTH = 10000;
    public int row;
    public int col;

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

    private static final ComponentMapper<MapPositionComponent> mapper = ComponentMapper.getFor(MapPositionComponent.class);

    public static MapPositionComponent get(Entity e) {
        return mapper.get(e);
    }
}
