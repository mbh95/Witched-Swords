package com.comp460.tactics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.comp460.Assets;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class TacticsMap {

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;

    private int width, height; // width and height of the map in tiles
    private int tileWidth, tileHeight; // width and height of one tile in pixels

    private boolean[][] traversableMask;
    private Unit[][] units;

    public TacticsMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        this.renderer = new OrthogonalTiledMapRenderer(tiledMap);

        width = tiledMap.getProperties().get("width", Integer.class);
        height = tiledMap.getProperties().get("height", Integer.class);

        tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);

        this.traversableMask = new boolean[this.width][this.height];
        this.units = new Unit[this.width][this.height];

        for (MapLayer ml : Assets.testMap.getLayers()) {
            if (ml.getName().equals("units")) {
                // Process units
                continue;
            } else {
                // Process terrain
                TiledMapTileLayer tl = (TiledMapTileLayer) ml;
                for (int r = 0; r < height; r++) {
                    for (int c = 0; c < width; c++) {
                        TiledMapTileLayer.Cell cell = tl.getCell(c, r);
                        if (cell == null || cell.getTile() == null) {
                            continue;
                        }
                        this.traversableMask[r][c] = cell.getTile().getProperties().get("traversable", Boolean.class);
                    }
                }
            }
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTileWidth() {
        return this.tileWidth;
    }

    public int getTileHeight() {
        return this.tileHeight;
    }

    public void render(OrthographicCamera camera) {
        this.renderer.setView(camera);
        this.renderer.render();
    }

    public void renderGridLines(Camera camera) {
        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        sr.setColor(Color.BLACK);
        sr.begin(ShapeRenderer.ShapeType.Line);
        for(int x = 0; x < width * tileWidth; x += tileWidth)
            sr.line(x, 0, x, height * tileHeight);
        for(int y = 0; y < height * tileHeight; y += tileHeight)
            sr.line(0, y, width * tileWidth, y);
        sr.end();
        sr.dispose();
    }

    public void renderTraversableMask(Camera camera) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (this.traversableMask[r][c]) {
                    sr.setColor(0.0f, 0.0f, 1.0f, 0.3f);
                } else {
                    sr.setColor(1.0f, 0.0f, 0.0f, 0.3f);
                }
                sr.rect(c * tileWidth, r * tileHeight, tileWidth, tileHeight);
            }
        }
        sr.end();
        sr.dispose();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
