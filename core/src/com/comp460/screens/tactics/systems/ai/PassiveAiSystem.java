package com.comp460.screens.tactics.systems.ai;

import com.badlogic.ashley.core.Entity;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.unit.ReadyToMoveComponent;

/**
 * Created by matth on 4/17/2017.
 */
public class PassiveAiSystem extends AiSystem {
    public PassiveAiSystem(TacticsScreen screen) {
        super(screen);
    }

    @Override
    public void makeMove(Entity toMove) {
        toMove.remove(ReadyToMoveComponent.class);
    }
}
