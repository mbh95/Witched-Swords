package com.comp460.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.comp460.components.KeyboardMapMovementComponent;
import com.comp460.components.MapPositionComponent;
import com.comp460.tactics.TacticsMap;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class KeyboardMapMovementSystem extends IteratingSystem {

    private ComponentMapper<KeyboardMapMovementComponent> kbdM;
    private ComponentMapper<MapPositionComponent> mapPosM;

    public KeyboardMapMovementSystem() {
        super(Family.all(KeyboardMapMovementComponent.class, MapPositionComponent.class).get());
        kbdM = ComponentMapper.getFor(KeyboardMapMovementComponent.class);
        mapPosM = ComponentMapper.getFor(MapPositionComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        KeyboardMapMovementComponent kbd = kbdM.get(entity);
        MapPositionComponent mapPos = mapPosM.get(entity);

        if (kbd.delay == 0) {
            int oldRow = mapPos.row;
            int oldCol = mapPos.col;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) mapPos.col--;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) mapPos.col++;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) mapPos.row++;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) mapPos.row--;
            if(mapPos.row < 0) mapPos.row = 0;
            else if(mapPos.row >= mapPos.map.getHeight()) mapPos.row = mapPos.map.getHeight() - 1;
            if(mapPos.col < 0) mapPos.col = 0;
            else if(mapPos.col >= mapPos.map.getWidth()) mapPos.col = mapPos.map.getWidth() - 1;
            if (mapPos.row != oldRow || mapPos.col != oldCol) {
                kbd.delay = kbd.maxDelay;
            }
        } else if (kbd.delay > 0)
            kbd.delay--;
    }
}
