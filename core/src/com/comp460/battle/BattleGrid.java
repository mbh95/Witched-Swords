package com.comp460.battle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.AssetMgr;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by matthewhammond on 1/29/17.
 */
public class BattleGrid {

    private int numRows;
    private int numCols;

    private BattleTile[][] grid;

    public List<BattleUnit> units = new ArrayList<>();
    private Queue<BattleEffect> effects = new PriorityQueue<>();
    private Queue<BattleEffect> nextEffects = new PriorityQueue<>();

    private int screenWidth;
    private int screenHeight;
    public BattleGrid(int screenWidth, int screenHeight, int numRows, int halfNumCols) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.numRows = numRows;
        this.numCols = halfNumCols * 2;
        this.grid = new BattleTile[this.numRows][this.numCols];

        TextureRegion tile = AssetMgr.Textures.BATTLE_TILE_BLUE;
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                float x = (screenWidth/2) + (c - halfNumCols) * tile.getRegionWidth();
                float y = tile.getRegionHeight() * (r + 1);
                if (c < numCols / 2) {
                    grid[r][c] = new BattleTile(x, y, AssetMgr.Textures.BATTLE_TILE_BLUE, AssetMgr.Textures.BATTLE_TILE_BLUE_SIDE);
                } else {
                    grid[r][c] = new BattleTile(x, y, AssetMgr.Textures.BATTLE_TILE_RED, AssetMgr.Textures.BATTLE_TILE_RED_SIDE);
                }
            }
        }
    }

    public BattleTile getTile(int row, int col) {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            float x = (screenWidth/2) + (col - numCols/2) * AssetMgr.Textures.BATTLE_TILE_BLUE.getRegionWidth();
            float y = AssetMgr.Textures.BATTLE_TILE_BLUE.getRegionHeight() * (row + 1);
            return new BattleTile(x, y, AssetMgr.Textures.BATTLE_TILE_BLUE, AssetMgr.Textures.BATTLE_TILE_BLUE_SIDE);
        }
        return grid[row][col];
    }

    public void render(SpriteBatch batch) {

       for (int r = numRows - 1; r >= 0; r--) {
           for (int c = 0; c < numCols; c++) {
               grid[r][c].render(batch);
           }
       }

       for (BattleUnit unit: units) {
           unit.render(batch);
       }

       for (BattleEffect e : effects) {
           e.render(batch);
       }
    }

    public boolean isOnGrid(int row, int col) {
        return row >= 0 && row < numRows && col >=0 && col < numCols;
    }

    public boolean isOnLHS(int row, int col) {
        return isOnGrid(row, col) && col < numCols / 2;
    }

    public boolean isOnRHS(int row, int col) {
        return isOnGrid(row, col) && col >= numCols / 2;
    }

    public Queue<BattleEffect> getEffects() {
        return this.effects;
    }

    public void update(float delta) {
        for (BattleUnit unit : units) {
            unit.update(delta);
        }

        while (!effects.isEmpty()) {
            BattleEffect e;
            if ((e = effects.poll()).update()) {
                nextEffects.add(e);
            }
        }

        Queue<BattleEffect> temp = effects;
        effects = nextEffects;
        nextEffects = temp;
    }

    public void addEffect(BattleEffect effect) {
        if (effect != null && isOnGrid(effect.row, effect.col)) {
            this.nextEffects.add(effect);
        }
    }

    public void removeEffect(BattleEffect effect) {
        this.nextEffects.remove(effect);
        this.effects.remove(effect);
    }

    public void addUnit(BattleUnit battleUnit) {
        this.units.add(battleUnit);
    }

    public List<BattleUnit> getUnits() {
        return this.units;
    }

    public int getNumRows() {
        return this.numRows;
    }

    public int getNumCols() {
        return this.numCols;
    }
}
