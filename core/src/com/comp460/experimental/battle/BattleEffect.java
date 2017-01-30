package com.comp460.experimental.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by matthewhammond on 1/30/17.
 */
public abstract class BattleEffect {

    protected int numTicks = 0;
    protected int row, col;
    protected BattleGrid grid;
    protected BattleUnit owner;

    public BattleEffect(BattleGrid grid, int row, int col, BattleUnit owner) {
        this.grid = grid;
        this.row = row;
        this.col = col;
        this.owner = owner;
    }

    public void update() {
        numTicks++;
        tick();
    }

    public abstract void tick();

    public abstract void render(SpriteBatch batch);

    public static BattleEffect buildWarning(int duration, float r, float g, float b, float a, BattleEffect replacement) {
        BattleGrid grid = replacement.grid;
        int row = replacement.row;
        int col = replacement.col;
        if (!grid.isOnGrid(row, col)) {
            return null;
        }
        return new BattleEffect(grid, row, col, replacement.owner) {
            @Override
            public void tick() {
                if (numTicks > duration) {
                    grid.removeEffect(this);
                    grid.addEffect(replacement);
                }
            }

            @Override
            public void render(SpriteBatch batch) {
                boolean needRestart = false;
                if (batch.isDrawing()) {
                    batch.end();
                    needRestart = true;
                }
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                ShapeRenderer sr = new ShapeRenderer();
                sr.setProjectionMatrix(batch.getProjectionMatrix());
                sr.begin(ShapeRenderer.ShapeType.Filled);
                BattleTile tile = grid.getTile(row, col);
                sr.setColor(r, g, b, a);
                sr.rect(tile.getScreenX(), tile.getScreenY(), tile.getWidth(), tile.getHeight());
                sr.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);

                if (needRestart) {
                    batch.begin();
                }
            }
        };
    }
}
