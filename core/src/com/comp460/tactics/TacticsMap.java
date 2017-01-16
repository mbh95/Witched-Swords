package com.comp460.tactics;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.comp460.Assets;
import com.comp460.components.MapPositionComponent;
import com.comp460.components.TextureComponent;
import com.comp460.components.TransformComponent;
import com.comp460.components.UnitStatsComponent;

import java.awt.*;
import java.util.*;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class TacticsMap {

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;

    private int width, height; // width and height of the map in tiles
    private int tileWidth, tileHeight; // width and height of one tile in pixels

    private boolean[][] traversableMask;
    private Entity[][] units;

    ComponentMapper<UnitStatsComponent> statsM;
    ComponentMapper<MapPositionComponent> posM;

    Map<Entity, Set<MapPosition>> entityToLegalMoves;

    public TacticsMap(TiledMap tiledMap, PooledEngine engine) {
        this.tiledMap = tiledMap;
        this.renderer = new OrthogonalTiledMapRenderer(tiledMap);

        width = tiledMap.getProperties().get("width", Integer.class);
        height = tiledMap.getProperties().get("height", Integer.class);

        tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);

        this.traversableMask = new boolean[this.width][this.height];
        this.units = new Entity[this.width][this.height];
        this.entityToLegalMoves = new HashMap<Entity, Set<MapPosition>>();

        this.statsM = ComponentMapper.getFor(UnitStatsComponent.class);
        this.posM = ComponentMapper.getFor(MapPositionComponent.class);

        for (MapLayer ml : Assets.testMap.getLayers()) {
            if (ml.getName().equals("units")) {
                // Process units
                TiledMapTileLayer tl = (TiledMapTileLayer) ml;
                for (int r = 0; r < height; r++) {
                    for (int c = 0; c < width; c++) {
                        TiledMapTileLayer.Cell cell = tl.getCell(c, r);
                        if (cell == null || cell.getTile() == null) {
                            continue;
                        }
                        Entity unit = engine.createEntity();
                        MapPositionComponent mapPos = engine.createComponent(MapPositionComponent.class)
                                                        .populate(this, r, c);
                        TextureComponent texture = engine.createComponent(TextureComponent.class)
                                                        .populate(cell.getTile().getTextureRegion());
                        TransformComponent transformComponent = engine.createComponent(TransformComponent.class)
                                                        .populate(tileWidth * c,tileHeight*r,0);
                        UnitStatsComponent stats = engine.createComponent(UnitStatsComponent.class).populate(cell.getTile().getProperties().get("team", Integer.class), 5);

                        unit.add(mapPos);
                        unit.add(texture);
                        unit.add(transformComponent);
                        unit.add(stats);
                        engine.addEntity(unit);

                        this.units[r][c] = unit;
                    }
                }
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
        updateLegalMoves();
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

    public Entity getAt(int row, int col) {
        return this.units[row][col];
    }

    public void moveTo(Entity entity, int row, int col) {
        if (this.units[row][col] == null && entityToLegalMoves.containsKey(entity) && entityToLegalMoves.get(entity).contains(new MapPosition(row, col))) {
            this.units[row][col] = entity;
            MapPositionComponent pos = posM.get(entity);
            this.units[pos.row][pos.col] = null;
            pos.row = row;
            pos.col = col;
            updateLegalMoves();
        }
    }

    public void updateLegalMoves() {
        entityToLegalMoves.clear();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (units[r][c] == null) {
                    continue;
                }
                Entity e = units[r][c];
                MapPositionComponent pos = posM.get(e);
                UnitStatsComponent stats = statsM.get(e);
                if (pos == null || stats == null) {
                    continue;
                }
                Set<MapPosition> newMoves = new TreeSet<MapPosition>();
                legalMovesHelper(newMoves, pos.row, pos.col, stats.moveDist);
                entityToLegalMoves.put(e, newMoves);
            }
        }
    }

    public void legalMovesHelper(Set<MapPosition> set, int r, int c, int countdown) {
        if (countdown < 0 || r >= height || c >= width || r < 0 || c < 0 || !this.traversableMask[r][c]) {
            return;
        }
        MapPosition cur = new MapPosition(r, c);
        set.add(cur);
        legalMovesHelper(set, r + 1, c, countdown - 1);
        legalMovesHelper(set, r - 1, c, countdown - 1);
        legalMovesHelper(set, r, c + 1, countdown - 1);
        legalMovesHelper(set, r, c - 1, countdown - 1);
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

    public void renderLegalMoves(Entity e, Camera camera, float r, float g, float b, float a) {
        if (!entityToLegalMoves.containsKey(e)) {
            return;
        }
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(r, g, b, a);

        for (MapPosition pos : entityToLegalMoves.get(e)) {
            sr.rect(pos.col * tileWidth, pos.row * tileHeight, tileWidth, tileHeight);
        }
        sr.end();
        sr.dispose();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private class MapPosition implements Comparable<MapPosition>{
        public int row, col;
        public MapPosition(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int compareTo(MapPosition o) {
            int comp = this.row - o.row;
            if (comp == 0) {
                return this.col - o.col;
            } else {
                return comp;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof MapPosition) {
                MapPosition other = (MapPosition) o;
                return this.row == other.row && this.col == other.col;
            } else return false;
        }
    }
}
