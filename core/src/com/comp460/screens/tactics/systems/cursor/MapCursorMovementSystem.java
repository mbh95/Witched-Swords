package com.comp460.screens.tactics.systems.cursor;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.comp460.common.input.Controller;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.cursor.LockedComponent;
import com.comp460.screens.tactics.components.cursor.MapCursorComponent;
import com.comp460.screens.tactics.components.map.MapPositionComponent;

import static com.comp460.assets.SoundManager.click;

/**
 * Created by matth on 2/20/2017.
 */
public class MapCursorMovementSystem extends IteratingSystem {
    private static final Family mapCursorFamily = Family.all(MapCursorComponent.class, MapPositionComponent.class).exclude(LockedComponent.class).get();

    private TacticsScreen parentScreen;

    public MapCursorMovementSystem(TacticsScreen parentScreen) {
        super(mapCursorFamily);

        this.parentScreen = parentScreen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MapCursorComponent cursor = MapCursorComponent.get(entity);
        MapPositionComponent cursorPos = MapPositionComponent.get(entity);
        Controller controller = this.parentScreen.game.controller;
        if (cursor.countdown <= 0) {
            int oldRow = cursorPos.row;
            int oldCol = cursorPos.col;
            if ((controller.leftPressed() && cursor.countdown <= 0) || controller.leftJustPressed()) cursorPos.col--;
            if ((controller.rightPressed() && cursor.countdown <= 0) || controller.rightJustPressed()) cursorPos.col++;
            if ((controller.upPressed() && cursor.countdown <= 0) || controller.upJustPressed()) cursorPos.row++;
            if ((controller.downPressed() && cursor.countdown <= 0) || controller.downJustPressed()) cursorPos.row--;
;
            if (cursorPos.row < 0) cursorPos.row = 0;
            else if (cursorPos.row >= parentScreen.getMap().getHeight())
                cursorPos.row = parentScreen.getMap().getHeight() - 1;

            if (cursorPos.col < 0) cursorPos.col = 0;
            else if (cursorPos.col >= parentScreen.getMap().getWidth())
                cursorPos.col = parentScreen.getMap().getWidth() - 1;

            if (cursorPos.row != oldRow || cursorPos.col != oldCol) {
                cursor.countdown = cursor.delay;
                click.play(1.0f);
            }
        } else {
            cursor.countdown -= deltaTime;
        }
    }
}
