package com.comp460.screens.battleECS2;

import com.badlogic.ashley.core.Engine;
import com.comp460.MainGame;
import com.comp460.common.GameScreen;
import com.comp460.common.GameState;
import com.comp460.screens.battleECS2.states.CountOffState;

/**
 * Created by matthewhammond on 2/25/17.
 */
public class BattleScreen extends GameScreen {

    private GameState curState;

    public Engine engine;

    public BattleScreen(MainGame game, GameScreen prevScreen) {
        super(game, prevScreen);

        this.curState = new CountOffState(this);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        curState.update(delta);
        curState.render(batch, uiBatch);

    }

    public void setState(GameState state) {
        this.curState = state;
        this.curState.init();
    }
}
