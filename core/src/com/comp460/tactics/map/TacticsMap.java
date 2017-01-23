package com.comp460.tactics.map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.comp460.Assets;
import com.comp460.Mappers;
import com.comp460.tactics.map.components.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by matthewhammond on 1/18/17.
 */
public class TacticsMap {

    Family mapUnits = Family.all(UnitStatsComponent.class, MapPositionComponent.class).get();
    private TiledMap tiledMap;

    private int width, height; // width and height of the map in tiles
    private int tileWidth, tileHeight; // width and height of one tile in pixels

    private MapTile[][] tiles;
    private Entity[][] units;

    private Map<Integer, Set<Entity>> teamToUnits;

    public TacticsMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;

        width = tiledMap.getProperties().get("width", Integer.class);
        height = tiledMap.getProperties().get("height", Integer.class);

        tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);

        this.tiles = new MapTile[this.height][this.width];
        this.units = new Entity[this.height][this.width];

        for (MapLayer ml : tiledMap.getLayers()) {
            if (!ml.getName().equals("units")) {
                // Process terrain
                TiledMapTileLayer tl = (TiledMapTileLayer) ml;
                for (int r = 0; r < height; r++) {
                    for (int c = 0; c < width; c++) {
                        TiledMapTileLayer.Cell cell = tl.getCell(c, r);
                        if (cell == null || cell.getTile() == null) {
                            continue;
                        }
                        this.tiles[r][c] = new MapTile(cell.getTile().getProperties().get("traversable", Boolean.class));
                    }
                }
            } else {
                ml.setVisible(false);
            }
        }
    }

    public void populate(PooledEngine engine) {
        for (MapLayer ml : tiledMap.getLayers()) {
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
                        TextureComponent texture = engine.createComponent(TextureComponent.class);

                        String animName = "";
                        if (cell.getTile().getProperties().containsKey("sprite")) {
                            animName = cell.getTile().getProperties().get("sprite", String.class);
                        }
                        if (Assets.animLookup.containsKey(animName)) {
                            AnimationComponent anim = new AnimationComponent().populate(Assets.animLookup.get(animName), 30);
                            texture.populate(anim.frames[anim.currentFrame]);
                            unit.add(anim);
                        } else {
                            texture.populate(cell.getTile().getTextureRegion());
                        }
                        TransformComponent transformComponent = engine.createComponent(TransformComponent.class)
                                .populate(tileWidth * c, tileHeight*r, 0);
                        UnitStatsComponent stats = engine.createComponent(UnitStatsComponent.class).populate(cell.getTile().getProperties().get("team", Integer.class), 5);

                        unit.add(mapPos);
                        unit.add(texture);
                        unit.add(transformComponent);
                        unit.add(stats);
                        engine.addEntity(unit);

                        this.units[r][c] = unit;
                    }
                }
            }
        }
    }

    public TiledMap getTiledMap() {
        return this.tiledMap;
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

    public Entity getUnitAt(int row, int col) {
        if (row < 0 || row >= this.height || col < 0 || col >= this.width) {
            return null;
        }
        return this.units[row][col];
    }

    public Set<MapPosition> computeValidMoves(Entity e) {
        if (!mapUnits.matches(e)) {
            return null;
        }
        Map<MapPosition, Integer> validMoves = new HashMap<>();
        validMovesHelper(e, validMoves, Mappers.mapPosM.get(e).mapPos.row, Mappers.mapPosM.get(e).mapPos.col, Mappers.statsM.get(e).moveDist);
        return validMoves.keySet();
    }

    private void validMovesHelper(Entity e, Map<MapPosition, Integer> bestSoFar, int r, int c, int countdown) {
        if (countdown < 0 || r < 0 || r >= this.height || c < 0 || c >= this.width || (this.units[r][c] != null && this.units[r][c] != e) || !this.tiles[r][c].isTraversable()) {
            return;
        }
        MapPosition newPos = new MapPosition(this, r, c);
        if (bestSoFar.containsKey(newPos)) {
            if (bestSoFar.get(newPos) >= countdown) {
                return;
            }
        }
        bestSoFar.put(new MapPosition(this, r, c), countdown);

        validMovesHelper(e, bestSoFar, r + 1, c, countdown - 1);
        validMovesHelper(e, bestSoFar, r - 1, c, countdown - 1);
        validMovesHelper(e, bestSoFar, r, c + 1, countdown - 1);
        validMovesHelper(e, bestSoFar, r, c - 1, countdown - 1);
    }

    public void move(Entity selection, int row, int col) {
        if (row < 0 || row >= this.height || col < 0 || col >= this.width) {
            return;
        }
        if (!computeValidMoves(selection).contains(new MapPosition(this, row, col))) {
            return;
        }
        MapPosition selectionPos = Mappers.mapPosM.get(selection).mapPos;
        if (selection == this.units[selectionPos.row][selectionPos.col]) {
            this.units[selectionPos.row][selectionPos.col] = null;
        }
        selectionPos.row = row;
        selectionPos.col = col;
        this.units[row][col] = selection;
    }
}
