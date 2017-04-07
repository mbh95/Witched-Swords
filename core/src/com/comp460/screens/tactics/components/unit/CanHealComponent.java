package com.comp460.screens.tactics.components.unit;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

/**
 * Created by Belinda on 4/3/17.
 */
public class CanHealComponent implements Component {

    private static final ComponentMapper<CanHealComponent> mapper = ComponentMapper.getFor(CanHealComponent.class);

    public static CanHealComponent get(Entity e) {
        return mapper.get(e);
    }
}
