package com.comp460.screens.tactics.components.cursor;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

/**
 * Created by matth on 3/26/2017.
 */
public class LockedComponent implements Component {

    private static final ComponentMapper<LockedComponent> mapper = ComponentMapper.getFor(LockedComponent.class);

    public static LockedComponent get(Entity e) {
        return mapper.get(e);
    }
}
