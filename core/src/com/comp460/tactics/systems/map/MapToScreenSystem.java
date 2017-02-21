package com.comp460.tactics.systems.map;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.comp460.tactics.TacticsScreen;
import com.comp460.tactics.components.map.MapPositionComponent;
import com.comp460.tactics.components.core.ChildComponent;
import com.comp460.tactics.components.core.TransformComponent;

/**
 * For each entity with both a map position and a transform which is not already tied to a parent entity
 */
public class MapToScreenSystem extends IteratingSystem {

    private static final Family trackingToMapFamily = Family.all(MapPositionComponent.class, TransformComponent.class).exclude(ChildComponent.class).get();

    private static final ComponentMapper<MapPositionComponent> mapPosM = ComponentMapper.getFor(MapPositionComponent.class);
    private static final ComponentMapper<TransformComponent> transformM = ComponentMapper.getFor(TransformComponent.class);

    private TacticsScreen parentScreen;

    public MapToScreenSystem(TacticsScreen tacticsScreen) {
        super(trackingToMapFamily);
        this.parentScreen = tacticsScreen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MapPositionComponent mapPos = mapPosM.get(entity);
        TransformComponent transform = transformM.get(entity);
        transform.pos.lerp(new Vector3(mapPos.col * parentScreen.getMap().getTileWidth(), mapPos.row *  parentScreen.getMap().getTileHeight(), transform.pos.z), 0.3f);
    }
}
