package com.comp460.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;

/**
 * Created by matthewhammond on 1/12/17.
 */
public class TacticsScreen extends ScreenAdapter {

    private Game game;

    public TacticsScreen(Game parentGame) {
        this.game = parentGame;
    }

    private void update(float delta) {

    }

    private void drawGrid() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        drawGrid();
    }
}
