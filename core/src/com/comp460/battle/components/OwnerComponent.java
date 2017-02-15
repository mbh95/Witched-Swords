package com.comp460.battle.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by matth on 2/14/2017.
 */
public class OwnerComponent implements Component {
    public Entity owner;

    public OwnerComponent(Entity owner) {
        this.owner = owner;
    }
}
