package com.comp460.archive.battle2.components;

import com.badlogic.ashley.core.Component;
import com.comp460.archive.battle2.BattleUnit;

/**
 * Created by matth on 2/12/2017.
 */
public class LocationComponent implements Component, Cloneable {
    public boolean relative;
    public int row, col;

    public LocationComponent() {

    }

    public LocationComponent(BattleUnit owner, LocationComponent template) {
        this.relative = template.relative;
        this.row = template.row;
        this.col = template.col;
        if (relative) {
            this.row += owner.getGridRow();
            this.col += owner.getGridCol();
        }
    }
}
