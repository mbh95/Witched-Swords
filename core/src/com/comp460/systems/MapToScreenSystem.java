package com.comp460.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.comp460.components.MapPositionComponent;
import com.comp460.components.TextureComponent;
import com.comp460.components.TransformComponent;
import com.comp460.tactics.TacticsMap;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class MapToScreenSystem extends IteratingSystem {

    private ComponentMapper<MapPositionComponent> mapPosM;
    private ComponentMapper<TransformComponent> transformM;

    public MapToScreenSystem() {
        super(Family.all(MapPositionComponent.class, TransformComponent.class).get());
        mapPosM = ComponentMapper.getFor(MapPositionComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MapPositionComponent mapPos = mapPosM.get(entity);
        TransformComponent transform = transformM.get(entity);
        transform.pos.slerp(new Vector3(mapPos.col * mapPos.map.getTileWidth(), mapPos.row *  mapPos.map.getTileHeight(), transform.pos.z), 0.3f);
    }
}
