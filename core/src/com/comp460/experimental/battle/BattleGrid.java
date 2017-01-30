package com.comp460.experimental.battle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.experimental.AssetManager;
import com.sun.scenario.effect.Effect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewhammond on 1/29/17.
 */
public class BattleGrid {

    private int numRows;
    private int numCols;

    private BattleTile[][] grid;

    private List<BattleUnit> units;
    private List<BattleEffect> effects;
    private List<BattleEffect> deadEffects;
    private List<BattleEffect> newEffects;

    public BattleGrid(int screenWidth, int screenHeight, int numRows, int halfNumCols) {
        this.numRows = numRows;
        this.numCols = halfNumCols * 2;
        this.grid = new BattleTile[this.numRows][this.numCols];

        TextureRegion tile = AssetManager.Textures.BATTLE_TILE_BLUE;
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                float x = (screenWidth/2) + (c - halfNumCols) * tile.getRegionWidth();
                float y = tile.getRegionHeight() * r;
                grid[r][c] = new BattleTile(x, y, c < numCols/2?AssetManager.Textures.BATTLE_TILE_BLUE:AssetManager.Textures.BATTLE_TILE_RED);
            }
        }

        units = new ArrayList<>();
        effects = new ArrayList<>();
        deadEffects = new ArrayList<>();
        newEffects = new ArrayList<>();
    }

    public BattleTile getTile(int row, int col) {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            return null;
        }
        return grid[row][col];
    }

    public void render(SpriteBatch batch) {

       for (int r = 0; r < numRows; r++) {
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

    public void update(float delta) {
        for (BattleUnit unit : units) {
            unit.update(delta);
        }

        effects.addAll(newEffects);
        newEffects.clear();
        for (BattleEffect effect : effects) {
            effect.update();
        }
        effects.removeAll(deadEffects);
        deadEffects.clear();
    }

    public void addEffect(BattleEffect effect) {
        if (effect != null && isOnGrid(effect.row, effect.col)) {
            this.newEffects.add(effect);
        }
    }

    public void removeEffect(BattleEffect effect) {
        this.deadEffects.add(effect);
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
