package com.comp460.tactics.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by matthewhammond on 1/16/17.
 */
public class UnitStatsComponent implements Component {
    public int team;
    public int moveDist;

    public UnitStatsComponent populate(int team, int moveDist) {
        this.team = team;
        this.moveDist = moveDist;
        return this;
    }
}
