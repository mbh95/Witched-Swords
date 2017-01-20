package com.comp460.tactics.map.components.units;

import com.badlogic.ashley.core.Component;
import com.comp460.tactics.map.MapPosition;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by matthewhammond on 1/19/17.
 */
public class ValidMovesComponent implements Component {
    public Set<MapPosition> validMoves = new HashSet<>();
}
