package com.comp460.battle.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by matth on 2/14/2017.
 */
public class EnergyComponent implements Component {
    public int maxEnergy;
    public int curEnergy;

    public EnergyComponent(int maxEnergy) {
        this(maxEnergy, maxEnergy);
    }

    public EnergyComponent(int maxEnergy, int curEnergy) {
        this.maxEnergy = maxEnergy;
        this.curEnergy = curEnergy;
    }
}
