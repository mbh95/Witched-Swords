package com.comp460.screens.battleECS.components;

import com.badlogic.ashley.core.Component;
import com.comp460.screens.battle.factories.ability.AbilityFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matth on 2/14/2017.
 */
public class AbilitiesComponent implements Component {
    public List<AbilityFactory> moves = new ArrayList<>();

    public AbilitiesComponent(AbilityFactory... moves) {
        for (AbilityFactory move : moves) {
            this.moves.add(move);
        }
    }
}
