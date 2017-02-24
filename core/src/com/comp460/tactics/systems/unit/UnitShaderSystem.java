package com.comp460.tactics.systems.unit;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.Color;
import com.comp460.tactics.components.core.TextureComponent;
import com.comp460.tactics.components.unit.ReadyToMoveComponent;
import com.comp460.tactics.components.unit.UnitStatsComponent;

/**
 * Created by matthewhammond on 2/22/17.
 */
public class UnitShaderSystem extends EntitySystem implements EntityListener {

    private static final Family readyFamily = Family.all(ReadyToMoveComponent.class, TextureComponent.class).get();

    private static final Family unitFamily = Family.all(UnitStatsComponent.class, TextureComponent.class).get();

    private ComponentMapper<TextureComponent> textureM = ComponentMapper.getFor(TextureComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(readyFamily, this);
    }

    @Override
    public void entityAdded(Entity entity) {
        TextureComponent texture = textureM.get(entity);
        if (texture != null) {
            texture.tint = Color.WHITE;
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        TextureComponent texture = textureM.get(entity);
        if (texture != null) {
            texture.tint = Color.GRAY;
        }
    }

    public void clearAllShading() {
        getEngine().getEntitiesFor(unitFamily).forEach(e->{
            textureM.get(e).tint = Color.WHITE;
        });
    }
}
