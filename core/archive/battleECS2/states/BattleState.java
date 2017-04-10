package com.comp460.screens.battleECS2.states;

import com.comp460.screens.battleECS2.BattleScreen;

/**
 * Created by matth on 4/9/2017.
 */
public abstract class BattleState {

    public BattleScreen parentScreen;

    public BattleState(BattleScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    public abstract void init();

    public abstract void render(float delta);

    public abstract void dispose();
}
