package com.comp460.screens.tactics.components.cursor;

import com.badlogic.ashley.core.Component;
import com.comp460.screens.tactics.components.map.MapPositionComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewhammond on 3/1/17.
 */
public class MovementPathComponent implements Component {
    public List<MapPositionComponent> positions = new ArrayList<>();
}
