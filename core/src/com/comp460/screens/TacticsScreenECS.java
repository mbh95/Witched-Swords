package com.comp460.screens;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.comp460.Assets;
import com.comp460.components.*;
import com.comp460.systems.*;
import com.comp460.tactics.TacticsMap;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class TacticsScreenECS extends ScreenAdapter {

    enum TacticsState {NOTHING_SELECTED, FRIENDLY_SELECTED, ENEMY_SELECTED, MOVE_SELECTED, EMPTY_SPACE_SELECTED};
    int curTeam = 0;



    TacticsState curState = TacticsState.NOTHING_SELECTED;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private PooledEngine engine;
    private TacticsMap map;

    private Entity cursor;
    private Entity selection = null;


    ComponentMapper<MapPositionComponent> mapPosM = ComponentMapper.getFor(MapPositionComponent.class);
    ComponentMapper<UnitStatsComponent> statsM = ComponentMapper.getFor(UnitStatsComponent.class);

    public TacticsScreenECS(int width, int height, SpriteBatch batch, TiledMap tiledMap) {
        this.batch = batch;
        this.camera = new OrthographicCamera(width, height);
        engine = new PooledEngine();
        map = new TacticsMap(tiledMap, engine);
        engine.addSystem(new MapToScreenSystem());
        engine.addSystem(new CameraTrackingSystem());
        engine.addSystem(new KeyboardMapMovementSystem());
        engine.addSystem(new RenderingSystem(batch, map, camera));
        engine.addSystem(new SnapToEntitySystem());

        makeCursor();
    }

    public void makeCursor() {
        cursor = engine.createEntity();

        TextureComponent texture = engine.createComponent(TextureComponent.class)
                                    .populate(new TextureRegion(Assets.cursor));
        CameraTargetComponent cameraTarget = engine.createComponent(CameraTargetComponent.class)
                                    .populate(camera);
        MapPositionComponent selectedSquare = engine.createComponent(MapPositionComponent.class)
                                    .populate(map, 0, 0);
        TransformComponent transformComponent = engine.createComponent(TransformComponent.class)
                                    .populate(0,0,0);
        KeyboardMapMovementComponent kbdComponent = engine.createComponent(KeyboardMapMovementComponent.class)
                                    .populate(8);

        cursor.add(texture);
        cursor.add(cameraTarget);
        cursor.add(selectedSquare);
        cursor.add(transformComponent);
        cursor.add(kbdComponent);

        engine.addEntity(cursor);
    }

    public void render (float deltaTime) {
        if (curTeam == 0) {
            switch(curState) {
                case NOTHING_SELECTED:
                    map.render(camera);
                    map.renderGridLines(camera);
                    map.renderGridLines(camera);
                    if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                        MapPositionComponent cursorPos = mapPosM.get(cursor);
                        if (cursorPos != null) {
                            if ((selection = map.getAt(cursorPos.row, cursorPos.col)) != null) {
                                UnitStatsComponent stats = statsM.get(selection);
                                if (stats.team == curTeam) {
                                    curState = TacticsState.FRIENDLY_SELECTED;
                                } else {
                                    curState = TacticsState.ENEMY_SELECTED;
                                }
                            }
                        }
                    }
                    break;
                case FRIENDLY_SELECTED:
                    map.render(camera);
                    map.renderGridLines(camera);
                    map.renderLegalMoves(selection, camera, 0.0f, 0.0f, 1.0f, 0.3f);
                    if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                        MapPositionComponent cursorPos = mapPosM.get(cursor);
                        if (cursorPos != null) {
                            if (map.getAt(cursorPos.row, cursorPos.col) != null) {
                                selection = map.getAt(cursorPos.row, cursorPos.col);
                                UnitStatsComponent stats = statsM.get(selection);
                                if (stats.team == curTeam) {
                                    curState = TacticsState.FRIENDLY_SELECTED;
                                    selection.add(engine.createComponent(SnapToComponent.class).populate(cursor));
                                } else {
                                    curState = TacticsState.ENEMY_SELECTED;
                                }
                            } else {
                                curState = TacticsState.MOVE_SELECTED;
                                cursor.remove(KeyboardMapMovementComponent.class);
                            }
                        }
                    }
                    break;
                case MOVE_SELECTED:
                    map.render(camera);
                    map.renderGridLines(camera);
                    map.renderLegalMoves(selection, camera, 0.0f, 0.0f, 1.0f, 0.3f);

                case ENEMY_SELECTED:
                    map.render(camera);
                    map.renderGridLines(camera);
                    map.renderLegalMoves(selection, camera, 1.0f, 0.0f, 0.0f, 0.3f);
                    if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                        MapPositionComponent cursorPos = mapPosM.get(cursor);
                        if (cursorPos != null) {
                            if (map.getAt(cursorPos.row, cursorPos.col) != null) {
                                selection = map.getAt(cursorPos.row, cursorPos.col);
                                curState = TacticsState.FRIENDLY_SELECTED;
                            } else {
                                map.moveTo(selection, cursorPos.row, cursorPos.col);
                                curState = TacticsState.NOTHING_SELECTED;
                            }
                        }
                    }
                    break;
            }
        }
        engine.update(deltaTime);
        camera.update();
    }
}