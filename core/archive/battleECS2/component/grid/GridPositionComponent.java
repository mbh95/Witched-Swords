package com.comp460.screens.battleECS2.component.grid;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

/**
 * Created by matth on 4/9/2017.
 */
public class GridPositionComponent implements Component {

    public int row;
    public int col;

    private static final ComponentMapper<GridPositionComponent> mapper = ComponentMapper.getFor(GridPositionComponent.class);

    public static GridPositionComponent get(Entity e) {
        return mapper.get(e);
    }
}
