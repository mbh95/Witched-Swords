package com.comp460.tactics.systems.cursor;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.comp460.tactics.TacticsScreen;
import com.comp460.tactics.components.cursor.MapCursorComponent;
import com.comp460.tactics.components.map.MapPositionComponent;

/**
 * Created by matth on 2/20/2017.
 */
public class MapCursorMovementSystem extends IteratingSystem {
    private static final Family mapCursorFamily = Family.all(MapCursorComponent.class, MapPositionComponent.class).get();

    private static final ComponentMapper<MapCursorComponent> cursorM = ComponentMapper.getFor(MapCursorComponent.class);
    private static final ComponentMapper<MapPositionComponent> mapPosM = ComponentMapper.getFor(MapPositionComponent.class);

    private TacticsScreen parentScreen;

    public MapCursorMovementSystem(TacticsScreen parentScreen) {
        super(mapCursorFamily);

        this.parentScreen = parentScreen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MapCursorComponent cursor = cursorM.get(entity);
        MapPositionComponent cursorPos = mapPosM.get(entity);

        cursor.countdown -= deltaTime;

        if (cursor.countdown <= 0) {
            cursor.countdown = cursor.delay;
            int oldRow = cursorPos.row;
            int oldCol = cursorPos.col;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) cursorPos.col--;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) cursorPos.col++;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) cursorPos.row++;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) cursorPos.row--;
            if (cursorPos.row < 0) cursorPos.row = 0;
            else if (cursorPos.row >= parentScreen.getMap().getHeight())
                cursorPos.row = parentScreen.getMap().getHeight() - 1;
            if (cursorPos.col < 0) cursorPos.col = 0;
            else if (cursorPos.col >= parentScreen.getMap().getWidth())
                cursorPos.col = parentScreen.getMap().getWidth() - 1;
            if (cursorPos.row != oldRow || cursorPos.col != oldCol) {
                cursor.countdown = cursor.delay;
            }
        }
    }
}
