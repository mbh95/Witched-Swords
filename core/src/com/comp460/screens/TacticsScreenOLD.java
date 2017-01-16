package com.comp460.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.comp460.Assets;
import com.comp460.Main;

/**
 * Created by matthewhammond on 1/12/17.
 */
public class TacticsScreenOLD extends ScreenAdapter {

    private int DISP_WIDTH = 400;
    private int DISP_HEIGHT = 240;

    private Main game;
    private OrthographicCamera camera;

    private int mapWidth;
    private int mapHeight;

    private int tileWidth;
    private int tileHeight;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Rectangle bulba;
    private int cursorDelay = 10;
    private int cursorRow, cursorCol;

    private boolean[][] traversableMask;
    private Entity[][] mapContents;

    public TacticsScreenOLD(Main parentGame, TiledMap tiledMap) {
        this.game = parentGame;
        this.camera = new OrthographicCamera(DISP_WIDTH, DISP_HEIGHT);

        this.tiledMap = tiledMap;
        this.mapRenderer = new OrthogonalTiledMapRenderer(Assets.testMap);

        mapWidth = tiledMap.getProperties().get("width", Integer.class);
        mapHeight = tiledMap.getProperties().get("height", Integer.class);

        tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);

        this.traversableMask = new boolean[mapHeight][mapWidth];
        this.mapContents = new Entity[mapHeight][mapWidth];

        // Iterate over map layers and create a collision mask for the map taking collision data from the topmost layer.
        for (MapLayer ml : Assets.testMap.getLayers()) {
            TiledMapTileLayer tl = (TiledMapTileLayer)ml;
            for (int r = 0; r < mapHeight; r++) {
                for (int c = 0; c < mapWidth; c++) {
                    TiledMapTileLayer.Cell cell = tl.getCell(c, r);
                    if (cell == null || cell.getTile() == null) {
                        continue;
                    }
                    this.traversableMask[r][c] = cell.getTile().getProperties().get("traversable", Boolean.class);
                }
            }
        }

        // make things! <3
        bulba = new Rectangle();
        bulba.x = 0;//1280 / 2 - 16 / 2;
        bulba.y = 0;
        bulba.width = 16;
        bulba.height = 16;

        cursorRow = 0;
        cursorCol = 0;
    }

    private void update(float delta) {

        // move camera and cursor
        float cameraSpeed = 3.0f;
        this.camera.translate(cameraSpeed * ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)? 1.0f : 0.0f) +
                                (Gdx.input.isKeyPressed(Input.Keys.LEFT)? -1.0f : 0.0f)),
                                cameraSpeed * ((Gdx.input.isKeyPressed(Input.Keys.UP)? 1.0f : 0.0f) +
                                (Gdx.input.isKeyPressed(Input.Keys.DOWN)? -1.0f : 0.0f)));


        if (cursorDelay == 0) {
            int oldRow = cursorRow;
            int oldCol = cursorCol;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) cursorCol--;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) cursorCol++;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) cursorRow++;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) cursorRow--;
            if (cursorRow != oldRow || cursorCol != oldCol) {
                cursorDelay = 8;
            }
        }
        if (cursorDelay > 0)
            cursorDelay--;

        if(cursorRow < 0) cursorRow = 0;
        if(cursorRow >= 30) cursorRow = 29;
        if(cursorCol < 0) cursorCol = 0;
        if(cursorCol >= 30) cursorCol = 29;

        this.camera.position.slerp(new Vector3(cursorCol * 16 + 8, cursorRow * 16 + 8, 0), 0.1f);

        this.camera.update();
    }

    private void drawMap() {
        this.mapRenderer.setView(this.camera);
        this.mapRenderer.render();
    }

    private void drawGridLines() {
        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        sr.setColor(Color.GRAY);
        sr.begin(ShapeRenderer.ShapeType.Line);
        for(int x = 0; x < mapWidth * tileWidth; x += tileWidth)
            sr.line(x, 0, x, mapHeight * tileHeight);
        for(int y = 0; y < mapHeight * tileHeight; y += tileHeight)
            sr.line(0, y, mapWidth * tileWidth, y);
        sr.end();
        sr.dispose();
    }

    private void drawCollisionMask() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (int r = 0; r < mapHeight; r++) {
            for (int c = 0; c < mapWidth; c++) {
                if (this.traversableMask[r][c]) {
                    sr.setColor(0.0f, 0.0f, 1.0f, 0.3f);
                } else {
                    sr.setColor(1.0f, 0.0f, 0.0f, 0.3f);
                }
                sr.rect(c * tileWidth, r * tileHeight, tileWidth, tileHeight);
            }
        }
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void render(float delta) {
        update(delta);
        drawMap();
        drawCollisionMask();
        drawGridLines();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(Assets.bulbaImage, bulba.x, bulba.y);
        game.batch.draw(Assets.cursor, cursorCol * 16, cursorRow * 16);
        game.batch.end();
        //this.game.batch.begin();
    }
}
