package com.comp460.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.comp460.Assets;
import com.comp460.tactics.map.components.*;
import com.comp460.tactics.map.systems.*;
import com.comp460.tactics.map.systems.input.KeyboardMapMovementSystem;
import com.comp460.tactics.map.systems.input.KeyboardMapSelectionSystem;
import com.comp460.tactics.map.systems.rendering.MapRenderingSystem;
import com.comp460.tactics.map.systems.rendering.MovesRenderingSystem;
import com.comp460.tactics.map.systems.rendering.SelectionRenderingSystem;
import com.comp460.tactics.map.systems.rendering.SpriteRenderingSystem;
import com.comp460.tactics.map.TacticsMap;

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

    public TacticsScreenECS(int width, int height, SpriteBatch batch, TiledMap tiledMap) {
        this.batch = batch;
        this.camera = new OrthographicCamera(width, height);
        engine = new PooledEngine();
        map = new TacticsMap(tiledMap);

        engine.addSystem(new KeyboardMapMovementSystem());
        engine.addSystem(new KeyboardMapSelectionSystem());

        engine.addSystem(new MapRenderingSystem(map, camera));
        engine.addSystem(new MovesRenderingSystem(map, camera));
        engine.addSystem(new SpriteRenderingSystem(batch, map, camera));
        engine.addSystem(new SelectionRenderingSystem(camera));

        engine.addSystem(new CameraTrackingSystem());
        engine.addSystem(new MapToScreenSystem());
        engine.addSystem(new SnapToEntitySystem());
        engine.addSystem(new AnimationSystem());

        map.populate(engine);
        makeCursor();
    }

    public void makeCursor() {
        cursor = engine.createEntity();
        TextureComponent texture = engine.createComponent(TextureComponent.class)
                .populate(new TextureRegion(Assets.Textures.CURSOR));
        CameraTargetComponent cameraTarget = engine.createComponent(CameraTargetComponent.class)
                .populate(camera);
        MapPositionComponent selectedSquare = engine.createComponent(MapPositionComponent.class)
                .populate(map, 0, 0);
        TransformComponent transformComponent = engine.createComponent(TransformComponent.class)
                .populate(0,0,0);
        CursorComponent cursorComponent = engine.createComponent(CursorComponent.class);
        KeyboardMapMovementComponent movement = engine.createComponent(KeyboardMapMovementComponent.class)
                .populate(8);
        cursor.add(texture);
        cursor.add(cameraTarget);
        cursor.add(selectedSquare);
        cursor.add(transformComponent);
        cursor.add(cursorComponent);
        cursor.add(movement);
        engine.addEntity(cursor);
    }

    public void render (float deltaTime) {
        if (curTeam == 0) {
            playerMakeMove();
        } else {
            aiMakeMove();
        }

        engine.update(deltaTime);
        camera.update();
    }

    private void playerMakeMove(){

    }

    private void aiMakeMove() {

    }
}