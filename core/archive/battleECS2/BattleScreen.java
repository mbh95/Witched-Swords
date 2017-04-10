package com.comp460.screens.battleECS2;

import com.badlogic.ashley.core.Engine;
import com.comp460.MainGame;
import com.comp460.common.GameScreen;
import com.comp460.common.GameUnit;
import com.comp460.screens.battleECS2.states.BattleState;
import com.comp460.screens.battleECS2.states.CountOffState;

/**
 * Created by matthewhammond on 2/25/17.
 */
public class BattleScreen extends GameScreen {

    public Engine engine;
    public BattleState curState;

    public GameUnit playerUnit;
    public GameUnit aiUnit;
    public GameScreen prevScreen;

    public BattleScreen(MainGame game, GameScreen prevScreen, GameUnit playerUnit, GameUnit aiUnit, float duration) {
        super(game);

        this.prevScreen = prevScreen;

        this.playerUnit = playerUnit;
        this.aiUnit = aiUnit;

        this.setState(new CountOffState(this, duration));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        engine.update(delta);
        if (this.curState != null) {
            this.curState.render(delta);
        }
    }

    public void setState(BattleState state) {
        if (this.curState != null) {
            this.curState.dispose();
        }
        this.curState = state;
        this.curState.init();
    }

    public void exitToPrevScreen() {
        this.game.setScreen(prevScreen);
    }

    public float rowToScreenY(int row) {
        return (tileHeight * row) + gridOffsetY;
    }

    public float colToScreenX(int col) {
        return ((this.width / 2f) + (col - numCols / 2f) * tileWidth) + gridOffsetX;
    }

    public boolean isOnGrid(int row, int col) {
        return (row >= 0 && row < numRows) && (col >= 0 && col < numCols);
    }

    public boolean isOnLHS(int row, int col) {
        return isOnGrid(row, col) && (col < numCols / 2);
    }

}
