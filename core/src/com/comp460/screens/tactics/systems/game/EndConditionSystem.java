package com.comp460.screens.tactics.systems.game;

import com.badlogic.ashley.core.*;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.common.components.TextureComponent;
import com.comp460.screens.tactics.components.unit.AIControlledComponent;
import com.comp460.screens.tactics.components.unit.PlayerControlledComponent;

/**
 * Created by matth on 2/22/2017.
 */
public class EndConditionSystem extends EntitySystem implements EntityListener {

    private static final Family unitFamily = Family.one(PlayerControlledComponent.class, AIControlledComponent.class).get();
    private static final Family playerFamily = Family.all(PlayerControlledComponent.class).get();
    private static final Family aiFamily = Family.all(AIControlledComponent.class).get();

    private static final ComponentMapper<TextureComponent> textureM = ComponentMapper.getFor(TextureComponent.class);

    private TacticsScreen screen;

    public EndConditionSystem(TacticsScreen screen) {
        this.screen = screen;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(unitFamily, this);
    }

    @Override
    public void entityAdded(Entity entity) {

    }

    @Override
    public void entityRemoved(Entity entity) {
        if (this.getEngine().getEntitiesFor(playerFamily).size() == 0) {
            this.screen.aiWins();
        } else if (this.getEngine().getEntitiesFor(aiFamily).size() == 0) {
            this.screen.playerWins();
        }
    }
}
