package com.comp460.tactics.map.components;

import com.badlogic.ashley.core.Component;
import com.comp460.tactics.map.MapPosition;
import com.comp460.tactics.map.TacticsMap;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class MapPositionComponent implements Component {
    public MapPosition mapPos = new MapPosition();

    public MapPositionComponent populate(TacticsMap map, int row, int col) {
        this.mapPos.map = map;
        this.mapPos.row = row;
        this.mapPos.col = col;
        return this;
    }
}
