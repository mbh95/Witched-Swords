package com.comp460.tactics.systems.unit;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.Color;
import com.comp460.tactics.components.map.MapPositionComponent;
import com.comp460.tactics.components.core.TextureComponent;
import com.comp460.tactics.components.unit.ReadyToMoveComponent;
import com.comp460.tactics.components.unit.UnitStatsComponent;

/**
 * Created by matthewhammond on 2/20/17.
 */
public class UnitColorizerListener extends EntitySystem implements EntityListener {

    private static final Family waitingUnitsFamily = Family.all(MapPositionComponent.class, UnitStatsComponent.class, TextureComponent.class).exclude(ReadyToMoveComponent.class).get();

    private static final ComponentMapper<TextureComponent> textureM = ComponentMapper.getFor(TextureComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        engine.addEntityListener(waitingUnitsFamily, this);
    }

    @Override
    public void entityAdded(Entity entity) {
        TextureComponent textureComponent = textureM.get(entity);
        textureComponent.tint = Color.GRAY;
        System.out.println(entity.getComponent(UnitStatsComponent.class).base.curHP);
    }

    @Override
    public void entityRemoved(Entity entity) {
        TextureComponent textureComponent = textureM.get(entity);
        textureComponent.tint = Color.WHITE;
    }
}
