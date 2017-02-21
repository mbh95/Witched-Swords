package com.comp460.tactics.components.unit;

import com.badlogic.ashley.core.Component;
import com.comp460.common.GameUnit;

/**
 * Attached to all units. Contains a base game unit and an integer representing the team that the unit is on.
 *
 * 0 is the player's team
 * 1 is the ai's team
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
