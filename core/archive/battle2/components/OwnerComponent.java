package com.comp460.archive.battle2.components;

import com.badlogic.ashley.core.Component;
import com.comp460.archive.battle2.BattleUnit;

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
