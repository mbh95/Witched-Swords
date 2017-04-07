package com.comp460.common;

/**
 * Created by matthewhammond on 2/25/17.
 */
public abstract class GameState {

    private GameScreen parentScreen;
    private GameState prevState;

    public abstract void init();

    public abstract void update(float delta);
    public abstract void render();

    public abstract void dispose();

    public void prevState() {
        parentScreen.setState(prevState);
        this.dispose();
    }
}
