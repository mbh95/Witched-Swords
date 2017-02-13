package com.comp460.battle.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.comp460.battle.BattleUnit;

/**
 * Created by matth on 2/13/2017.
 */
public class OwnerComponent implements Component {
    public transient BattleUnit owner;

    public OwnerComponent() {

    }

    public OwnerComponent(BattleUnit owner, OwnerComponent template) {
        this.owner = owner;
    }
}
