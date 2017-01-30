package com.comp460.tactics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.comp460.Assets;
import com.comp460.common.components.CameraTargetComponent;
import com.comp460.common.components.TextureComponent;
import com.comp460.common.components.TransformComponent;
import com.comp460.common.systems.CameraTrackingSystem;
import com.comp460.common.systems.SnapToParentSystem;
import com.comp460.common.systems.SpriteAnimationSystem;
import com.comp460.common.systems.SpriteRenderingSystem;
import com.comp460.tactics.components.*;
import com.comp460.tactics.map.TacticsMap;
import com.comp460.tactics.systems.input.KeyboardMapCursorSystem;
import com.comp460.tactics.systems.rendering.MapRenderingSystem;
import com.comp460.tactics.systems.rendering.MapToScreenSystem;
import com.comp460.tactics.systems.rendering.MovesRenderingSystem;
import com.comp460.tactics.systems.rendering.SelectionRenderingSystem;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class TacticsScreen extends ScreenAdapter {

    private static final Family readyPlayerUnitsFamily = Family.all(PlayerControlledComponent.class, ReadyToMoveComponent.class).get();
    private static final Family waitingPlayerUnitsFamily = Family.all(PlayerControlledComponent.class).exclude(ReadyToMoveComponent.class).get();

    private static final Family readyAiUnitsFamily = Family.all(AIControlledComponent.class, ReadyToMoveComponent.class).get();
    private static final Family waitingAiUnitsFamily = Family.all(AIControlledComponent.class).exclude(ReadyToMoveComponent.class).get();

    private SpriteBatch batch;
    private PooledEngine engine;
    private OrthographicCamera camera;

    private TacticsMap map;

    public TacticsScreen(int width, int height, TiledMap tiledMap) {

        this.batch = new SpriteBatch();
        this.engine = new PooledEngine();

        this.camera = new OrthographicCamera(width, height);

        this.map = new TacticsMap(tiledMap);
        this.map.populate(engine);

        makeCursor();

        engine.addSystem(new KeyboardMapCursorSystem(this));
        engine.addSystem(new MapRenderingSystem(this));
        engine.addSystem(new MapToScreenSystem(this));
        engine.addSystem(new MovesRenderingSystem(this));
        engine.addSystem(new SelectionRenderingSystem(this));

        engine.addSystem(new SpriteRenderingSystem(batch, camera));
        engine.addSystem(new SpriteAnimationSystem());
        engine.addSystem(new CameraTrackingSystem());
        engine.addSystem(new SnapToParentSystem());

        engine.addEntityListener(readyPlayerUnitsFamily, new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {

            }

            @Override
            public void entityRemoved(Entity entity) {
                if (engine.getEntitiesFor(readyPlayerUnitsFamily).size() == 0) {
                    engine.getEntitiesFor(waitingAiUnitsFamily).forEach(e->{
                        e.add(new ReadyToMoveComponent());
                    });
                    engine.getSystem(KeyboardMapCursorSystem.class).setProcessing(false);
                }
            }
        });

        engine.addEntityListener(readyAiUnitsFamily, new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {

            }

            @Override
            public void entityRemoved(Entity entity) {
                if (engine.getEntitiesFor(readyAiUnitsFamily).size() == 0) {
                    engine.getEntitiesFor(waitingPlayerUnitsFamily).forEach(e->{
                        e.add(new ReadyToMoveComponent());
                    });
                    engine.getSystem(KeyboardMapCursorSystem.class).setProcessing(true);
                }
            }
        });
    }

    public TacticsMap getMap() {
        return this.map;
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public void makeCursor() {
        Entity cursor = engine.createEntity();
        TextureComponent texture = engine.createComponent(TextureComponent.class)
                .populate(new TextureRegion(Assets.Textures.CURSOR));
        CameraTargetComponent cameraTarget = engine.createComponent(CameraTargetComponent.class)
                .populate(camera, 0.3f);
        MapPositionComponent selectedSquare = engine.createComponent(MapPositionComponent.class)
                .populate(0, 0);
        TransformComponent transformComponent = engine.createComponent(TransformComponent.class)
                .populate(0,0,0);
        MapCursorComponent cursorComponent = engine.createComponent(MapCursorComponent.class).populate(8);

        cursor.add(texture);
        cursor.add(cameraTarget);
        cursor.add(selectedSquare);
        cursor.add(transformComponent);
        cursor.add(cursorComponent);
        engine.addEntity(cursor);
    }

    public void render (float deltaTime) {
        engine.update(deltaTime);
        camera.update();
    }
}