package com.comp460.tactics.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by matthewhammond on 1/16/17.
 */
public class UnitStatsComponent implements Component {
    public String id;
    public int team;
    public int moveDist;
    public int maxHp;
    public int curHp;
    public int moveA;
    public int moveB;

    public UnitStatsComponent populate(String id, int team, int moveDist) {
        this.id = id;
        this.team = team;
        this.moveDist = moveDist;
        return this;
    }
}
