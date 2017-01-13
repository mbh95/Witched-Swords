package com.comp460.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.comp460.Assets;
import com.comp460.Settings;

/**
 * Created by matthewhammond on 1/12/17.
 */
public class TacticsScreen extends ScreenAdapter {

    private Game game;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;

    public TacticsScreen(Game parentGame) {
        this.game = parentGame;
        this.camera = new OrthographicCamera(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
        this.camera.translate(Settings.WINDOW_WIDTH / 2, (Integer)Assets.testMap.getProperties().get("height") * (Integer)Assets.testMap.getProperties().get("tileheight") - Settings.WINDOW_HEIGHT / 2);
        this.mapRenderer = new OrthogonalTiledMapRenderer(Assets.testMap);
    }

    private void update(float delta) {

        float cameraSpeed = 4.0f;
        this.camera.translate(cameraSpeed * ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)? 1.0f : 0.0f) +
                                (Gdx.input.isKeyPressed(Input.Keys.LEFT)? -1.0f : 0.0f)),
                                cameraSpeed * ((Gdx.input.isKeyPressed(Input.Keys.UP)? 1.0f : 0.0f) +
                                (Gdx.input.isKeyPressed(Input.Keys.DOWN)? -1.0f : 0.0f)));
        this.camera.update();

    }

    private void drawGrid() {
        this.mapRenderer.setView(this.camera);
        this.mapRenderer.render();

        ShapeRenderer sr = new ShapeRenderer();
        TiledMap map = mapRenderer.getMap();
        int tileWidth = map.getProperties().get("tilewidth", Integer.class), tileHeight = map.getProperties().get("tileheight", Integer.class);
        int mapWidth = map.getProperties().get("width", Integer.class) * tileWidth, mapHeight = map.getProperties().get("height", Integer.class) * tileHeight;
        sr.setProjectionMatrix(camera.combined);
        sr.setColor(Color.GRAY);
        sr.begin(ShapeRenderer.ShapeType.Line);
        for(int x = 0; x < mapWidth; x += tileWidth)
            sr.line(x, 0, x, mapHeight);
        for(int y = 0; y < mapHeight; y += tileHeight)
            sr.line(0, y, mapWidth, y);
        sr.end();
    }

    @Override
    public void render(float delta) {
        update(delta);
        drawGrid();
    }
}
