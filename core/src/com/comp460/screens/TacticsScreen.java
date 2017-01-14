package com.comp460.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.comp460.Assets;
import com.comp460.Main;
import com.comp460.Settings;

/**
 * Created by matthewhammond on 1/12/17.
 */
public class TacticsScreen extends ScreenAdapter {

    private int DISP_WIDTH = 400;
    private int DISP_HEIGHT = 240;

    private Main game;
    private OrthographicCamera camera;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    private boolean[][] collisionMask;
    private Entity[][] mapContents;

    public TacticsScreen(Main parentGame, TiledMap tiledMap) {
        this.game = parentGame;
        this.camera = new OrthographicCamera(DISP_WIDTH, DISP_HEIGHT);
        //this.camera = new OrthographicCamera(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);

        this.tiledMap = tiledMap;
        this.mapRenderer = new OrthogonalTiledMapRenderer(Assets.testMap);

        int width = tiledMap.getProperties().get("width", Integer.class);
        int height = tiledMap.getProperties().get("height", Integer.class);
        this.collisionMask = new boolean[height][width];
        this.mapContents = new Entity[height][width];

        for (MapLayer ml : Assets.testMap.getLayers()) {
            TiledMapTileLayer tl = (TiledMapTileLayer)ml;

            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {
                    TiledMapTileLayer.Cell cell = tl.getCell(c, r);
                    if (cell == null || cell.getTile() == null) {
                        continue;
                    }
                    this.collisionMask[r][c] = cell.getTile().getProperties().get("traversable", Boolean.class);
                }
            }
        }
    }

    private void update(float delta) {

        float cameraSpeed = 3.0f;
        this.camera.translate(cameraSpeed * ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)? 1.0f : 0.0f) +
                                (Gdx.input.isKeyPressed(Input.Keys.LEFT)? -1.0f : 0.0f)),
                                cameraSpeed * ((Gdx.input.isKeyPressed(Input.Keys.UP)? 1.0f : 0.0f) +
                                (Gdx.input.isKeyPressed(Input.Keys.DOWN)? -1.0f : 0.0f)));
        this.camera.update();

    }

    private void drawMap() {
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
        drawMap();
        //this.game.batch.begin();
    }
}
