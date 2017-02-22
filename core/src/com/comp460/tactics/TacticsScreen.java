package com.comp460.tactics;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.comp460.MainGame;
import com.comp460.common.GameScreen;
import com.comp460.tactics.factories.CursorFactory;
import com.comp460.tactics.systems.ai.AiSystem;
import com.comp460.tactics.systems.core.CameraTrackingSystem;
import com.comp460.tactics.systems.core.SnapToParentSystem;
import com.comp460.tactics.systems.core.SpriteAnimationSystem;
import com.comp460.tactics.systems.core.SpriteRenderingSystem;
import com.comp460.tactics.components.unit.AIControlledComponent;
import com.comp460.tactics.components.unit.PlayerControlledComponent;
import com.comp460.tactics.components.unit.ReadyToMoveComponent;
import com.comp460.tactics.systems.ui.HoverRenderingSystem;
import com.comp460.tactics.systems.cursor.MapCursorMovementSystem;
import com.comp460.tactics.systems.unit.TurnManagementSystem;
import com.comp460.tactics.systems.unit.UnitColorizerListener;
import com.comp460.tactics.systems.map.MapRenderingSystem;
import com.comp460.tactics.systems.map.MapToScreenSystem;
import com.comp460.tactics.systems.cursor.MapCursorSelectionSystem;
import com.comp460.tactics.systems.map.MovesRenderingSystem;
import com.comp460.tactics.systems.map.SelectionRenderingSystem;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class TacticsScreen extends GameScreen {

    enum TacticsState {PLAYER_TURN, AI_TURN};

    private static final Family playerUnitsFamily = Family.all(PlayerControlledComponent.class).get();
    private static final Family aiUnitsFamily = Family.all(AIControlledComponent.class).get();

    private PooledEngine engine;

    private TacticsMap map;
    public int turn;

    public TacticsScreen(MainGame game, GameScreen prevScreen, TiledMap tiledMap) {
        super(game, prevScreen);

        this.engine = new PooledEngine();

        this.map = new TacticsMap(tiledMap);

        engine.addSystem(new MapRenderingSystem(this));

        engine.addSystem(new SpriteRenderingSystem(batch, camera));
        engine.addSystem(new SpriteAnimationSystem());
        engine.addSystem(new CameraTrackingSystem());
        engine.addSystem(new SnapToParentSystem());

        engine.addSystem(new MapCursorSelectionSystem(this));
        engine.addSystem(new MapCursorMovementSystem(this));
        engine.addSystem(new MapToScreenSystem(this));
        engine.addSystem(new MovesRenderingSystem(this));
        engine.addSystem(new SelectionRenderingSystem(this));
        engine.addSystem(new HoverRenderingSystem(this));

        engine.addSystem(new TurnManagementSystem(this));

        engine.addSystem(new AiSystem());


        this.map.populate(engine);

        engine.addEntity(CursorFactory.makeCursor(this));

        startPlayerTurn();
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

    public void startPlayerTurn() {
        engine.getEntitiesFor(playerUnitsFamily).forEach(e->{
            e.add(new ReadyToMoveComponent());
        });
    }

    public void startAiTurn() {
        engine.getEntitiesFor(aiUnitsFamily).forEach(e->{
            e.add(new ReadyToMoveComponent());
        });
    }
}