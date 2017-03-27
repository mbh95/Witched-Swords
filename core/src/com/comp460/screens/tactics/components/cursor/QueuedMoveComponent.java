package com.comp460.screens.tactics.components.cursor;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.comp460.screens.tactics.systems.cursor.ActionMenuSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matth on 3/21/2017.
 */
public class QueuedMoveComponent implements Component {
    public List<ActionMenuSystem.Action> actions = new ArrayList<>();
    public int selectedAction;

    private static final ComponentMapper<QueuedMoveComponent> mapper = ComponentMapper.getFor(QueuedMoveComponent.class);

    public static QueuedMoveComponent get(Entity e) {
        return mapper.get(e);
    }
}
