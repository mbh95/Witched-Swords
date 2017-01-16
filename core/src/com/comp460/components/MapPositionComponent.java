package com.comp460.components;

import com.badlogic.ashley.core.Component;
import com.comp460.tactics.TacticsMap;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class MapPositionComponent implements Component{
    public TacticsMap map;
    public int row, col;

    public MapPositionComponent populate(TacticsMap map, int row, int col) {
        this.map = map;
        this.row = row;
        this.col = col;
        return this;
    }
}
