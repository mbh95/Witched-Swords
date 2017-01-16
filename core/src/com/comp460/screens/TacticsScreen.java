package com.comp460.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.comp460.tactics.Cursor;
import com.comp460.tactics.TacticsMap;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class TacticsScreen extends ScreenAdapter {

    private SpriteBatch batch;

    private int screenWidth, screenHeight;

    private OrthographicCamera camera;

    private TacticsMap map;

    private Cursor cursor;

    public TacticsScreen(SpriteBatch batch, int screenWidth, int screenHeight, TiledMap map) {
        this.batch = batch;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        this.camera = new OrthographicCamera(this.screenWidth, this.screenHeight);

        this.map = new TacticsMap(map, null);

        this.cursor = new Cursor(this.map);
    }

    private void update(float delta) {

        cursor.update(Gdx.input.isKeyPressed(Input.Keys.UP),
                        Gdx.input.isKeyPressed(Input.Keys.DOWN),
                        Gdx.input.isKeyPressed(Input.Keys.LEFT),
                        Gdx.input.isKeyPressed(Input.Keys.RIGHT));

        float cameraSpeed = 3.0f;
        this.camera.translate(cameraSpeed * ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)? 1.0f : 0.0f) +
                        (Gdx.input.isKeyPressed(Input.Keys.LEFT)? -1.0f : 0.0f)),
                cameraSpeed * ((Gdx.input.isKeyPressed(Input.Keys.UP)? 1.0f : 0.0f) +
                        (Gdx.input.isKeyPressed(Input.Keys.DOWN)? -1.0f : 0.0f)));
        this.camera.position.slerp(new Vector3(cursor.getCol() * 16 + 8, (map.getHeight() - 1 - cursor.getRow()) * 16 + 8, 0), 0.1f);
        this.camera.update();
    }

    @Override
    public void render(float delta) {
        update(delta);
        map.render(camera);
        map.renderTraversableMask(camera);
        map.renderGridLines(camera);

        cursor.render(batch, camera);
    }
}
