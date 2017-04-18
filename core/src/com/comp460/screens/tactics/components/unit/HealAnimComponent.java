package com.comp460.screens.tactics.components.unit;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

/**
 * Created by matthewhammond on 4/18/17.
 */
public class HealAnimComponent implements Component {

    public int amt;
    public float duration = 1f;

    public HealAnimComponent(int amt) {
        this.amt = amt;
    }

    private static final ComponentMapper<HealAnimComponent> mapper = ComponentMapper.getFor(HealAnimComponent.class);

    public static HealAnimComponent get(Entity e) {
        return mapper.get(e);
    }
}
