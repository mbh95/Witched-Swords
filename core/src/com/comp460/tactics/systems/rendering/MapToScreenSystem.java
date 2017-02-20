package com.comp460.tactics.systems.rendering;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.comp460.tactics.TacticsScreen;
import com.comp460.tactics.components.MapPositionComponent;
import com.comp460.tactics.components.ChildComponent;
import com.comp460.tactics.components.TransformComponent;

/**
 * Created by matthewhammond on 1/15/17.
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
        transform.pos.slerp(new Vector3(mapPos.col * parentScreen.getMap().getTileWidth(), mapPos.row *  parentScreen.getMap().getTileHeight(), transform.pos.z), 0.3f);
    }
}
