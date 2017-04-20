package com.comp460.screens.tactics.systems.unit;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.Color;
import com.comp460.common.components.TextureComponent;
import com.comp460.screens.tactics.components.unit.ReadyToMoveComponent;
import com.comp460.screens.tactics.components.unit.UnitStatsComponent;

/**
 * Shades units that do not have a ready move gray.
 */
public class UnitShaderSystem extends EntitySystem implements EntityListener {

    private static final Family readyFamily = Family.all(ReadyToMoveComponent.class, TextureComponent.class).get();
    private static final Family unitFamily = Family.all(UnitStatsComponent.class, TextureComponent.class).get();

    private ComponentMapper<TextureComponent> textureM = ComponentMapper.getFor(TextureComponent.class);

    public UnitShaderSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(readyFamily, this);
    }

    @Override
    public void entityAdded(Entity entity) {
        // Reset newly ready units to white shading.
        TextureComponent texture = textureM.get(entity);
        texture.tint = Color.WHITE;
    }

    @Override
    public void entityRemoved(Entity entity) {
        // Make sure non-ready units still have a texture to shade.
        if (!unitFamily.matches(entity)) {
            return;
        }
        // Set this non-ready unit to gray shading.
        TextureComponent texture = textureM.get(entity);
        texture.tint = Color.GRAY;
    }

    /**
     * Iterate over all units in the engine and set their shading to white.
     */
    public void clearAllShading() {
        getEngine().getEntitiesFor(unitFamily).forEach(e -> {
            textureM.get(e).tint = Color.WHITE;
        });
    }
}
