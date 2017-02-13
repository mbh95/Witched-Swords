package com.comp460;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by matthewhammond on 2/11/17.
 */
public abstract class GameScreen extends ScreenAdapter {

    public Game game;
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public GameScreen prevScreen;

    public GameScreen(Game game, GameScreen prevScreen) {
        super();
        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT);
        this.prevScreen = prevScreen;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    public void previousScreen() {
        this.game.setScreen(prevScreen);
        this.dispose();
    }
}
