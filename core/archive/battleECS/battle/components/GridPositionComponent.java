package com.comp460.battle.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by matth on 2/13/2017.
 */
public class GridPositionComponent implements Component {
    public int row;
    public int col;

    public GridPositionComponent(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
