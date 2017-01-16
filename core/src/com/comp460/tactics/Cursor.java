package com.comp460.tactics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.comp460.Assets;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class Cursor {

    private TacticsMap map;

    private final int delayThreshold = 8;
    private int delay = 0;

    private int row;
    private int col;

    public Cursor(TacticsMap map) {
        this.map = map;
        this.row = 0;
        this.col = 0;
    }

    public void update(boolean up, boolean down, boolean left, boolean right) {
        int oldRow = row;
        int oldCol = col;
        if (delay == 0) {
            if (left) col--;
            if (right) col++;
            if (up) row--;
            if (down) row++;
            if (row != oldRow || col != oldCol) {
                delay = 8;
            }
        }
        if (delay > 0)
            delay--;
        clampTo(map.getWidth() - 1, map.getHeight() - 1);

        if (this.row != oldRow || this.col != oldCol) {
            this.delay = delayThreshold;
        }
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void clampTo(int widthIncl, int heightIncl) {
        this.row = Math.max(0, this.row);
        this.row = Math.min(this.row, heightIncl);
        this.col = Math.max(0, this.col);
        this.col = Math.min(this.col, widthIncl);
    }

    public void render(SpriteBatch batch, Camera camera) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(Assets.cursor, col * map.getTileWidth(), (map.getHeight() - 1 - row) * map.getTileHeight());
        batch.end();
    }
}
