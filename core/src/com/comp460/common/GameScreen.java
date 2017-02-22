package com.comp460.common;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.comp460.MainGame;
import com.comp460.Settings;

/**
 * Created by matthewhammond on 2/11/17.
 */
public abstract class GameScreen extends ScreenAdapter {

    public MainGame game;
    public SpriteBatch batch;
    public SpriteBatch uiBatch;
    public OrthographicCamera camera;
    public OrthographicCamera uiCamera;
    public GameScreen prevScreen;

    public final int width = Settings.INTERNAL_WIDTH;
    public final int height = Settings.INTERNAL_HEIGHT;

    public GameScreen(MainGame game, GameScreen prevScreen) {
        super();
        this.game = game;
        this.batch = new SpriteBatch();
        this.uiBatch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT);

        this.uiCamera = new OrthographicCamera();
        this.uiCamera.setToOrtho(false, Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT);

        this.prevScreen = prevScreen;

        this.uiCamera.update();
        this.uiBatch.setProjectionMatrix(uiCamera.combined);
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
