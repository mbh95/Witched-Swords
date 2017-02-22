package com.comp460.tactics.systems.unit;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.Color;
import com.comp460.tactics.TacticsScreen;
import com.comp460.tactics.components.core.TextureComponent;
import com.comp460.tactics.components.unit.AIControlledComponent;
import com.comp460.tactics.components.unit.PlayerControlledComponent;
import com.comp460.tactics.components.unit.ReadyToMoveComponent;
import com.comp460.tactics.components.unit.UnitStatsComponent;

/**
 * Created by matthewhammond on 2/20/17.
 */
public class TurnManagementSystem extends EntitySystem implements EntityListener {

    private static final Family readyFamily = Family.all(ReadyToMoveComponent.class).get();

    private static final Family readyPlayerFamily = Family.all(PlayerControlledComponent.class, ReadyToMoveComponent.class).get();
    private static final Family readyAiFamily = Family.all(AIControlledComponent.class, ReadyToMoveComponent.class).get();

    private static final ComponentMapper<TextureComponent> textureM = ComponentMapper.getFor(TextureComponent.class);

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
        TextureComponent textureComponent = textureM.get(entity);
        if (textureComponent != null) {
            textureComponent.tint = Color.WHITE;
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (this.getEngine().getEntitiesFor(readyFamily).size() == 0) {
            if (screen.turn == 0) {
                screen.startPlayerTurn();
            } else {
                screen.startAiTurn();
            }
        }
        TextureComponent textureComponent = textureM.get(entity);
        if (textureComponent != null) {
            textureComponent.tint = Color.GRAY;
        }
    }
}
