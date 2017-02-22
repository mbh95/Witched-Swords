package com.comp460.tactics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.comp460.MainGame;
import com.comp460.common.GameScreen;
import com.comp460.tactics.components.core.CameraTargetComponent;
import com.comp460.tactics.components.map.MapPositionComponent;
import com.comp460.tactics.components.core.TextureComponent;
import com.comp460.tactics.components.core.TransformComponent;
import com.comp460.tactics.components.cursor.MapCursorComponent;
import com.comp460.tactics.factories.CursorFactory;
import com.comp460.tactics.systems.core.CameraTrackingSystem;
import com.comp460.tactics.systems.core.SnapToParentSystem;
import com.comp460.tactics.systems.core.SpriteAnimationSystem;
import com.comp460.tactics.systems.core.SpriteRenderingSystem;
import com.comp460.tactics.components.unit.AIControlledComponent;
import com.comp460.tactics.components.unit.PlayerControlledComponent;
import com.comp460.tactics.components.unit.ReadyToMoveComponent;
import com.comp460.tactics.systems.ui.HoverRenderingSystem;
import com.comp460.tactics.systems.cursor.MapCursorMovementSystem;
import com.comp460.tactics.systems.unit.UnitColorizerListener;
import com.comp460.tactics.systems.map.MapRenderingSystem;
import com.comp460.tactics.systems.map.MapToScreenSystem;
import com.comp460.tactics.systems.cursor.KeyboardMapCursorSystem;
import com.comp460.tactics.systems.map.MovesRenderingSystem;
import com.comp460.tactics.systems.map.SelectionRenderingSystem;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class TacticsScreen extends GameScreen {

    private static final Family readyPlayerUnitsFamily = Family.all(PlayerControlledComponent.class, ReadyToMoveComponent.class).get();
    private static final Family waitingPlayerUnitsFamily = Family.all(PlayerControlledComponent.class).exclude(ReadyToMoveComponent.class).get();

    private static final Family readyAiUnitsFamily = Family.all(AIControlledComponent.class, ReadyToMoveComponent.class).get();
    private static final Family waitingAiUnitsFamily = Family.all(AIControlledComponent.class).exclude(ReadyToMoveComponent.class).get();

    private PooledEngine engine;

    private TacticsMap map;

    public TacticsScreen(MainGame game, GameScreen prevScreen, TiledMap tiledMap) {
        super(game, prevScreen);

        this.engine = new PooledEngine();

        this.map = new TacticsMap(tiledMap);
        this.map.populate(engine);

        engine.addEntity(CursorFactory.makeCursor(this));

        engine.addSystem(new MapRenderingSystem(this));

        engine.addSystem(new SpriteRenderingSystem(batch, camera));
        engine.addSystem(new SpriteAnimationSystem());
        engine.addSystem(new CameraTrackingSystem());
        engine.addSystem(new SnapToParentSystem());

        engine.addSystem(new KeyboardMapCursorSystem(this));
        engine.addSystem(new MapCursorMovementSystem(this));
        engine.addSystem(new MapToScreenSystem(this));
        engine.addSystem(new MovesRenderingSystem(this));
        engine.addSystem(new SelectionRenderingSystem(this));
        engine.addSystem(new HoverRenderingSystem(this));

        engine.addEntityListener(new UnitColorizerListener());

        // For now just skip the enemy turn:
        engine.addEntityListener(readyPlayerUnitsFamily, new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {

            }

            @Override
            public void entityRemoved(Entity entity) {
                if (engine.getEntitiesFor(readyPlayerUnitsFamily).size() == 0) {
                    engine.getEntitiesFor(waitingPlayerUnitsFamily).forEach(e -> {
                        e.add(new ReadyToMoveComponent());
                    });
                }
            }
        });

        // In the future uncomment this to enable the ai to take a turn
//        engine.addEntityListener(readyPlayerUnitsFamily, new EntityListener() {
//            @Override
//            public void entityAdded(Entity entity) {
//
//            }
//
//            @Override
//            public void entityRemoved(Entity entity) {
//                if (engine.getEntitiesFor(readyPlayerUnitsFamily).size() == 0) {
//                    engine.getEntitiesFor(waitingAiUnitsFamily).forEach(e->{
//                        e.add(new ReadyToMoveComponent());
//                    });
//                    engine.getSystem(KeyboardMapCursorSystem.class).setProcessing(false);
//                }
//            }
//        });
//
//        engine.addEntityListener(readyAiUnitsFamily, new EntityListener() {
//            @Override
//            public void entityAdded(Entity entity) {
//
//            }
//
//            @Override
//            public void entityRemoved(Entity entity) {
//                if (engine.getEntitiesFor(readyAiUnitsFamily).size() == 0) {
//                    engine.getEntitiesFor(waitingPlayerUnitsFamily).forEach(e->{
//                        e.add(new ReadyToMoveComponent());
//                    });
//                    engine.getSystem(KeyboardMapCursorSystem.class).setProcessing(true);
//                }
//            }
//        });
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



    public void render(float deltaTime) {
        engine.update(deltaTime);
        camera.update();
    }
}