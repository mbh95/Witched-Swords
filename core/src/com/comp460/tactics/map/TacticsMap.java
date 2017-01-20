package com.comp460.tactics.map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.comp460.tactics.map.components.MapPositionComponent;
import com.comp460.tactics.map.components.TextureComponent;
import com.comp460.tactics.map.components.TransformComponent;
import com.comp460.tactics.map.components.units.UnitStatsComponent;
import com.comp460.tactics.map.components.units.ValidMovesComponent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by matthewhammond on 1/18/17.
 */
public class TacticsMap {

    private TiledMap tiledMap;
    public int[] visibleLayers;

    private int width, height; // width and height of the map in tiles
    private int tileWidth, tileHeight; // width and height of one tile in pixels

    private MapTile[][] tiles;
    private Entity[][] units;

    private Map<Integer, Set<Entity>> teamToUnits;

    //Family unit =

    public TacticsMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;

        width = tiledMap.getProperties().get("width", Integer.class);
        height = tiledMap.getProperties().get("height", Integer.class);

        tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);

        this.tiles = new MapTile[this.width][this.height];
        this.units = new Entity[this.width][this.height];

        int layer = 0;
        Set<Integer> visible = new HashSet<>();
        for (MapLayer ml : tiledMap.getLayers()) {
            if (!ml.getName().equals("units")) {
                visible.add(layer);
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
            }
            layer++;
        }
        this.visibleLayers = new int[visible.size()];
        int i = 0;
        for (int l : visible) {
            this.visibleLayers[i] = l;
            i++;
        }
        System.out.println(Arrays.toString(visibleLayers));
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
                        TextureComponent texture = engine.createComponent(TextureComponent.class)
                                .populate(cell.getTile().getTextureRegion());
                        TransformComponent transformComponent = engine.createComponent(TransformComponent.class)
                                .populate(tileWidth * c, tileHeight*r, 0);
                        UnitStatsComponent stats = engine.createComponent(UnitStatsComponent.class).populate(cell.getTile().getProperties().get("team", Integer.class), 5);
                        ValidMovesComponent validMoves = engine.createComponent(ValidMovesComponent.class);

                        unit.add(mapPos);
                        unit.add(texture);
                        unit.add(transformComponent);
                        unit.add(stats);
                        unit.add(validMoves);
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

//    public Set<MapPosition> computeValidMoves(Entity e) {
//
//    }
}
