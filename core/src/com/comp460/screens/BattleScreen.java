package com.comp460.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.Assets;
import com.comp460.BattleAttack;
import com.comp460.BattleUnit;
import com.comp460.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Belinda on 1/15/17.
 */
public class BattleScreen extends ScreenAdapter {

    private int DISP_WIDTH = 400;
    private int DISP_HEIGHT = 240;

    private Main game;
    private OrthographicCamera camera;

    private BattleUnit bulba;
    private BattleUnit mega;

    private int bounceDelay = 120;
    private int cursorDelay = 10;

    private List<BattleAttack> attacks = new ArrayList<BattleAttack>();

    public BattleScreen(Main parentGame) {
        this.game = parentGame;
        this.camera = new OrthographicCamera(DISP_WIDTH, DISP_HEIGHT);
        this.camera.position.set(DISP_WIDTH/2, DISP_HEIGHT/2, 0);

        bulba = new BattleUnit(Assets.bulbaMacro);
        bulba.col = 0; bulba.row = 0;
        bulba.maxHP = 100;
        bulba.currHP = 100;

        mega = new BattleUnit(Assets.mega);
        mega.player = true;
        mega.col = 0; bulba.row = 0;
        mega.maxHP = 10;
        mega.currHP = 10;
    }

    private void update(float delta) {
        camera.update();

        // move/attack with mega! <3
        if (cursorDelay == 0) {
            int oldRow = mega.row;
            int oldCol = mega.col;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) mega.col--;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) mega.col++;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) mega.row++;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) mega.row--;

            if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                // ATTACK WAAAAAAAAAA
                for (int i = 0; i < 3; i++)
                    attacks.add(new BattleAttack(mega.row, i, 10, 2, Assets.scratch));
            }

            if (mega.row != oldRow || mega.col != oldCol) {
                cursorDelay = 8;
            }
        }
        if (cursorDelay > 0)
            cursorDelay--;

        if(mega.row < 0) mega.row = 0;
        if(mega.row >= 2) mega.row = 3-1;
        if(mega.col < 0) mega.col = 0;
        if(mega.col >= 2) mega.col = 3-1;

        List<BattleAttack> toDelete = new ArrayList<BattleAttack>();
        for (BattleAttack att : attacks) {
            if (att.row == bulba.row && att.col == bulba.col) {
                bulba.currHP-=att.damage;
            }
            att.update();
            if (att.duration == 0) {
                toDelete.add(att);
            }
        }
        for (BattleAttack del : toDelete) {
            attacks.remove(del);
        }
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

    private void drawHP(BattleUnit unit) {
        int x, y;
        if (unit.player) {
            x = 4; y = DISP_HEIGHT-20;
        } else {
            x = DISP_WIDTH-64-4; y = DISP_HEIGHT-20;
        }
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(Assets.hpBar, x, y);
        game.batch.end();

        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        double percentHP = 1.0*unit.currHP/unit.maxHP;
        if (percentHP > .45)
            sr.setColor(Color.GREEN);
        else if (percentHP > .25)
            sr.setColor(Color.GOLDENROD);
        else
            sr.setColor(Color.SCARLET);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        if (percentHP > 0)
            sr.rect(x+9, y+3, (int) (52 * percentHP), 4);
        sr.end();
    }

    @Override
    public void render(float delta) {
        update(delta);
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        drawMap();
        game.batch.end();
        drawMask();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // bounce bulba! <3
        int bulbaHeight = bulba.row*40 + 29;
        int megaHeight = mega.row*40 + 29;
        if (bounceDelay <= 60) {
            bulbaHeight += 3;
            megaHeight += 3;
        }
        if (bounceDelay == 0) bounceDelay = 120;
        bounceDelay--;
        game.batch.draw(bulba.sprite, DISP_WIDTH/2 + bulba.col*40, bulbaHeight);
        game.batch.draw(mega.sprite, DISP_WIDTH/2 - 40*3 + mega.col*40, megaHeight);

        for (BattleAttack attack : attacks) {
            game.batch.draw(attack.sprite, DISP_WIDTH/2 + attack.col*40, attack.row*40 + 29);
        }
        game.batch.end();

        drawHP(bulba);

        drawHP(mega);
    }
}
