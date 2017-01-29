package com.comp460.tactics.components;

import com.badlogic.ashley.core.Component;
import com.comp460.tactics.map.MapPosition;
import com.comp460.tactics.map.TacticsMap;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class MapPositionComponent implements Component {
    public int row, col;

    public MapPositionComponent populate(int row, int col) {
        this.row = row;
        this.col = col;
        return this;
    }
}
