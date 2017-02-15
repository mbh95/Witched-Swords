package com.comp460.archive.battle2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by matthewhammond on 1/30/17.
 */
public abstract class BattleEffect implements Comparable<BattleEffect> {

    protected int numTicks = 0;
    protected int row, col;
    protected BattleUnit owner;
    protected int priority = 0;
    public BattleEffect(BattleUnit owner, int row, int col, int priority) {
        this.row = row;
        this.col = col;
        this.owner = owner;
        this.priority = priority;
    }

    public boolean update() {
        numTicks++;
        return tick();
    }

    public abstract boolean tick();

    public abstract void render(SpriteBatch batch);

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(BattleEffect o) {
        return Integer.compare(o.getPriority(), this.getPriority());
    }
}
