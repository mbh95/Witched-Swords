package com.comp460.tactics;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.comp460.tactics.components.map.MapPositionComponent;
import com.comp460.tactics.components.unit.ReadyToMoveComponent;
import com.comp460.tactics.components.unit.UnitStatsComponent;
import com.comp460.tactics.factories.UnitFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by matthewhammond on 1/18/17.
 */
public class TacticsMap {

    private static final Family mapUnitsFamily = Family.all(UnitStatsComponent.class, MapPositionComponent.class).get();
    private static final Family readyFamily = Family.all(ReadyToMoveComponent.class).get();

    private static final ComponentMapper<MapPositionComponent> mapPosM = ComponentMapper.getFor(MapPositionComponent.class);
    private static final ComponentMapper<UnitStatsComponent> statsM = ComponentMapper.getFor(UnitStatsComponent.class);

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

                        int team = cell.getTile().getProperties().get("team", Integer.class);
                        String id = cell.getTile().getProperties().get("id", String.class);

                        Entity unit = UnitFactory.makeUnit(this, id, team, r, c);

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

    public float getTileX(int row, int col) {
        return tileWidth * col;
    }

    public float getTileY(int row, int col) {
        return tileHeight * row;
    }

    public boolean isOnMap(int row, int col) {
        return (row >= 0 && row < this.height && col >= 0 && col < this.width);
    }
    public Entity getUnitAt(int row, int col) {
        if (!isOnMap(row, col)) {
            return null;
        }
        return this.units[row][col];
    }

    public boolean canMoveTo(Entity unit, int row, int col) {
        if (!isOnMap(row, col)) {
            return false;
        }
        return computeValidMoves(unit).contains(new MapPositionComponent(row, col));
    }

    public Set<MapPositionComponent> computeValidMoves(Entity e) {
        if (!mapUnitsFamily.matches(e)) {
            return null;
        }
        Map<MapPositionComponent, Integer> validMoves = new HashMap<>();
        validMovesHelper(e, validMoves, mapPosM.get(e).row, mapPosM.get(e).col, statsM.get(e).base.moveDist);
        Set<MapPositionComponent> finalMoves = validMoves.keySet();
//        finalMoves.removeIf(pos->units[pos.getRow()][pos.getCol()] != null);
        return finalMoves;
    }

    private void validMovesHelper(Entity e, Map<MapPositionComponent, Integer> bestSoFar, int r, int c, int countdown) {
        if (countdown < 0 || r < 0 || r >= this.height || c < 0 || c >= this.width || !this.tiles[r][c].isTraversable()) {
            return;
        }

        // You can't move through enemies:
//        if (this.units[r][c] != null && statsM.get(this.units[r][c]).team != statsM.get(e).team) {
//            return;
//        }
        MapPositionComponent newPos = new MapPositionComponent(r, c);
        if (bestSoFar.containsKey(newPos)) {
            if (bestSoFar.get(newPos) >= countdown) {
                return;
            }
        }
        bestSoFar.put(new MapPositionComponent(r, c), countdown);

        validMovesHelper(e, bestSoFar, r + 1, c, countdown - 1);
        validMovesHelper(e, bestSoFar, r - 1, c, countdown - 1);
        validMovesHelper(e, bestSoFar, r, c + 1, countdown - 1);
        validMovesHelper(e, bestSoFar, r, c - 1, countdown - 1);
    }

    public Entity move(Entity selection, int row, int col) {
        if (!isOnMap(row, col)) {
            return null;
        }
        MapPositionComponent selectionPos = mapPosM.get(selection);
        if (isOnMap(selectionPos.row, selectionPos.col) && selection == this.units[selectionPos.row][selectionPos.col]) {
            this.units[selectionPos.row][selectionPos.col] = null;
        }
        selectionPos.row = row;
        selectionPos.col = col;
        Entity prevUnit = this.units[row][col];
        this.units[row][col] = selection;

        return prevUnit;
    }

    class MapTile {
        private boolean traversable;

        public MapTile(boolean traversable) {
            this.traversable = traversable;
        }

        public boolean isTraversable() {
            return this.traversable;
        }
    }
}
