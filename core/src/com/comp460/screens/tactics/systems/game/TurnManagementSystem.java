package com.comp460.screens.tactics.systems.game;

import com.badlogic.ashley.core.*;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.unit.ReadyToMoveComponent;

/**
 * Created by matthewhammond on 2/20/17.
 */
public class TurnManagementSystem extends EntitySystem implements EntityListener {

    private static final Family readyFamily = Family.all(ReadyToMoveComponent.class).get();

    private TacticsScreen screen;

    public TurnManagementSystem(TacticsScreen screen) {
        this.screen = screen;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(readyFamily, this);
    }

    @Override
    public void entityAdded(Entity entity) {
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (this.getEngine().getEntitiesFor(readyFamily).size() == 0) {
            if (screen.curState == TacticsScreen.TacticsState.AI_TURN) {
                screen.startTransitionToPlayerTurn();
            } else {
                screen.startTransitionToAiTurn();
            }
        }
    }

    public void endTurn() {
        for (Entity entity : this.getEngine().getEntities()) {
            if (readyFamily.matches(entity)) {
                entity.remove(ReadyToMoveComponent.class);
            }
        }
    }
}
