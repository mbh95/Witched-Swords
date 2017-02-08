package com.comp460.launcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.Settings;

/**
 * Created by matthewhammond on 2/7/17.
 */
public class SplashScreen extends ScreenAdapter {

    private Game game;
    private Batch batch;
    private OrthographicCamera camera;

    private TextureRegion TEXTURE_S = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/S.png")));
    private TextureRegion TEXTURE_WITCHED_WORDS = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/witched-words.png")));

    private float timer;

    public SplashScreen(Game game) {

        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT);
        camera.update();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(TEXTURE_S, 0.0f, 0.0f);
        batch.setColor(Color.ORANGE);
        batch.end();
        timer+=delta;
    }
}
