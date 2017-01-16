package com.comp460.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.Assets;
import com.comp460.BattleUnit;
import com.comp460.Main;

/**
 * Created by Belinda on 1/15/17.
 */
public class BattleScreen extends ScreenAdapter {

    private int DISP_WIDTH = 400;
    private int DISP_HEIGHT = 240;

    private Main game;
    private OrthographicCamera camera;

    private BattleUnit bulba;

    private int cursorDelay = 10;

    public BattleScreen(Main parentGame) {
        this.game = parentGame;
        this.camera = new OrthographicCamera(DISP_WIDTH, DISP_HEIGHT);
        this.camera.position.set(DISP_WIDTH/2, DISP_HEIGHT/2, 0);

        bulba = new BattleUnit(Assets.bulbaMacro);
        bulba.col = 0; bulba.row = 0;
    }

    private void update(float delta) {
        camera.update();

        // move bulba! <3
        if (cursorDelay == 0) {
            int oldRow = bulba.row;
            int oldCol = bulba.col;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bulba.col--;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bulba.col++;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) bulba.row++;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) bulba.row--;
            if (bulba.row != oldRow || bulba.col != oldCol) {
                cursorDelay = 8;
            }
        }
        if (cursorDelay > 0)
            cursorDelay--;

        if(bulba.row < 0) bulba.row = 0;
        if(bulba.row >= 2) bulba.row = 3-1;
        if(bulba.col < 0) bulba.col = 0;
        if(bulba.col >= 2) bulba.col = 3-1;
    }

    private void drawMap() {
        // draw battle tiles
        for (int i = 2; i >= 0; i--) {
            for (int j = 2; j >= 0; j--) {
                game.batch.draw(Assets.battleTile, DISP_WIDTH/2 - (i+1)*40, j*40 + 20);
                game.batch.draw(Assets.battleTile, DISP_WIDTH/2 + i*40, j*40 + 20);
            }
        }
    }

    private void drawMask() {
        // draw battle tile mask
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        sr.setColor(0.0f, 0.0f, 1.0f, 0.1f);
        sr.rect(DISP_WIDTH/2 - 40*3, 20, 40*3, 40*3+9);
        sr.setColor(1.0f, 0.0f, 0.0f, 0.1f);
        sr.rect(DISP_WIDTH/2, 20, 40*3, 40*3+9);
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void render(float delta) {
        update(delta);
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        drawMap();

        // bounce bulba! <3
        int bulbaHeight = bulba.row*40 + 29;
        if (bulba.animationDelay <= 60) {
            bulbaHeight += 3;
        }
        if (bulba.animationDelay == 0) bulba.animationDelay = 120;
        bulba.animationDelay--;
        game.batch.draw(bulba.sprite, DISP_WIDTH/2 + bulba.col*40, bulbaHeight);

        game.batch.end();

        drawMask();
    }
}
