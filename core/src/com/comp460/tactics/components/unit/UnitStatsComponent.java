package com.comp460.tactics.components.unit;

import com.badlogic.ashley.core.Component;
import com.comp460.common.GameUnit;

/**
 * Created by matthewhammond on 1/16/17.
 */
public class UnitStatsComponent implements Component {
    public int team;
    public GameUnit base;

    public UnitStatsComponent populate(int team, GameUnit base) {
        this.base = base;
        this.team = team;
        return this;
    }
}
