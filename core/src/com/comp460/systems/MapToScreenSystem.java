package com.comp460.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.comp460.Mappers;
import com.comp460.components.MapPositionComponent;
import com.comp460.components.SnapToComponent;
import com.comp460.components.TransformComponent;
import com.comp460.tactics.map.MapPosition;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class MapToScreenSystem extends IteratingSystem {

    public MapToScreenSystem() {
        super(Family.all(MapPositionComponent.class, TransformComponent.class).exclude(SnapToComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MapPosition mapPos = Mappers.mapPosM.get(entity).mapPos;
        TransformComponent transform = Mappers.transformM.get(entity);
        transform.pos.slerp(new Vector3(mapPos.col * mapPos.map.getTileWidth(), mapPos.row *  mapPos.map.getTileHeight(), transform.pos.z), 0.3f);
    }
}
