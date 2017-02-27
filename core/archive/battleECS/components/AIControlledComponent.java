package com.comp460.screens.battleECS.components;

import com.badlogic.ashley.core.Component;
import com.comp460.screens.battleECS.ai.AI;
import com.comp460.screens.battleECS.ai.BulbaAI;
import com.comp460.screens.battleECS.ai.GhastAI;

/**
 * Created by matth on 2/15/2017.
 */
public class AIControlledComponent implements Component{
    public AI strategy;

    public AIControlledComponent(AI strategy) {
        this.strategy = strategy;
    }

    public AIControlledComponent(String unitID) {
        switch (unitID) {
            case "bulba":
                strategy = new BulbaAI();
                break;
            case "ghast":
                strategy = new GhastAI();
                break;
            default:
                strategy = new BulbaAI();
        }
    }
}
