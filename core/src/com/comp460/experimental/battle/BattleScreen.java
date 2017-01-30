package com.comp460.experimental.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.Settings;
import com.comp460.experimental.AssetManager;
import com.comp460.experimental.GameUnit;

/**
 * Created by matthewhammond on 1/29/17.
 */
public class BattleScreen extends ScreenAdapter {

    private BattleUnit playerUnit;

    private BattleUnit aiUnit;

    private BattleGrid grid;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    public BattleScreen(GameUnit basePlayerUnit, GameUnit baseAiUnit) {

        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera(Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT);
        this.camera.position.set(Settings.INTERNAL_WIDTH / 2, Settings.INTERNAL_HEIGHT / 2, 0);
        grid = new BattleGrid(Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT, 3, 3);

        playerUnit = new BattleUnit(grid, basePlayerUnit, 0, 0);
        aiUnit = new BattleUnit(grid, baseAiUnit, 0, 5);
    }

    @Override
    public void render(float delta) {
        camera.update();
        takeInput();
        updateAI();
        grid.update(delta);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(AssetManager.Textures.BATTLE_BG, 0,0);
        grid.render(batch);
        renderUI(batch);
        batch.end();
    }

    private void renderUI(SpriteBatch batch) {
        batch.draw(AssetManager.Textures.HP_BAR, 4, 215);
        batch.draw(AssetManager.Textures.HP_BAR, 332, 215);

        batch.end();
        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        double percentHP = 1.0*playerUnit.getCurHP()/ playerUnit.getMaxHP();
        if (percentHP > .45)
            sr.setColor(Color.GREEN);
        else if (percentHP > .25)
            sr.setColor(Color.GOLDENROD);
        else
            sr.setColor(Color.SCARLET);
        if (percentHP > 0)
            sr.rect(4+9, 215+8, (int) (52 * percentHP), 4);

        percentHP = 1.0*aiUnit.getCurHP()/ aiUnit.getMaxHP();
        if (percentHP > .45)
            sr.setColor(Color.GREEN);
        else if (percentHP > .25)
            sr.setColor(Color.GOLDENROD);
        else
            sr.setColor(Color.SCARLET);
        if (percentHP > 0)
            sr.rect(332+9, 215+8, (int) (52 * percentHP), 4);
        sr.end();

        batch.begin();
        for (int i = playerUnit.getEnergy(); i > 0 ; i--)
            batch.draw(AssetManager.Textures.ENERGY, 51 + 4 - (i-1)*11, 215+2);
        for (int i = aiUnit.getEnergy(); i > 0 ; i--)
            batch.draw(AssetManager.Textures.ENERGY, 51 + 332 - (i-1)*11, 215+2);
    }

    private void takeInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) playerUnit.move(0, -1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) playerUnit.move(0, 1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) playerUnit.move(1, 0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) playerUnit.move(-1, 0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) playerUnit.action1();
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) playerUnit.action2();
    }

    private void updateAI() {

    }
}
