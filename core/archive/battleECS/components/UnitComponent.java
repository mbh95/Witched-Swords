package com.comp460.screens.battleECS.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by matth on 2/15/2017.
 */
public class UnitComponent implements Component {
    public String unitID;

    public UnitComponent(String unitID) {
        this.unitID = unitID;
    }
}
