package com.comp460.battle.buffs;

/**
 * Created by matthewhammond on 2/15/17.
 */
public interface BattleBuff {
    void tick(float delta);
    boolean isDone();
    void post();
}
