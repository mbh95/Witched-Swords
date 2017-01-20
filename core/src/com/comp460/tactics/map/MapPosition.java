package com.comp460.tactics.map;

/**
 * Created by matthewhammond on 1/19/17.
 */
public class MapPosition {
    public TacticsMap map;
    public int row, col;

    @Override
    public int hashCode() {
        return (map.getWidth() * row + col);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof  MapPosition)) {
            return false;
        } else {
            MapPosition other = (MapPosition)o;
            return (other.map == this.map && other.row == this.row && other.col == this.col);
        }
    }
}
