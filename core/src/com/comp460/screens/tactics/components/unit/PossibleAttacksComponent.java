package com.comp460.screens.tactics.components.unit;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.comp460.screens.tactics.components.map.MapPositionComponent;

import java.util.ArrayList;

/**
 * Created by matthewhammond on 3/22/17.
 */
public class PossibleAttacksComponent implements Component {
    public ArrayList<MapPositionComponent> positions = new ArrayList<>();

    private static final ComponentMapper<PossibleAttacksComponent> mapper = ComponentMapper.getFor(PossibleAttacksComponent.class);

    public static PossibleAttacksComponent get(Entity e) {
        return mapper.get(e);
    }
}
