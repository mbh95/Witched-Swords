package com.comp460.screens.tactics.components.unit;

import com.badlogic.ashley.core.Component;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.systems.game.MoveActionSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matth on 3/21/2017.
 */
public class QueuedMoveComponent implements Component {
    public List<MapPositionComponent> path = new ArrayList<>();
    public List<MoveActionSystem.Action> actions = new ArrayList<>();
    public int selectedAction;
}
