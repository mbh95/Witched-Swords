package com.comp460.archive.battle2;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.AssetMgr;
import com.comp460.common.FontManager;
import com.comp460.GameScreen;
import com.comp460.archive.battle2.systems.*;
import com.comp460.common.GameUnit;
import com.sun.javafx.binding.StringFormatter;

import java.util.Random;

/**
 * Created by matthewhammond on 1/29/17.
 */
public class BattleScreen extends GameScreen {

    private static BitmapFont greenFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.GREEN, Color.BLACK, 2);
    private static BitmapFont yellowFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.ORANGE, Color.BLACK, 2);
    private static BitmapFont redFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.RED, Color.BLACK, 2);

    private static GlyphLayout readyLayout = new GlyphLayout(redFont, "READY");
    private static GlyphLayout setLayout = new GlyphLayout(yellowFont, "SET");
    private static GlyphLayout fightLayout = new GlyphLayout(greenFont, "FIGHT!");

    private static BitmapFont timerFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.RED);
    private static BitmapFont resultsFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 32, Color.WHITE, Color.BLACK, 2);
    private static BitmapFont continueFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 16, Color.WHITE);

    private static BitmapFont hpFont = FontManager.getFont(FontManager.KEN_PIXEL, 8, Color.WHITE);

    private enum BattleState {COUNTOFF, RUNNING, END_ENERGY, END_DEATH, END_TIME};

    private BattleUnit playerUnit;

    private BattleUnit aiUnit;

    public BattleGrid grid;

    private int width, height;

    private float countOffTimer = 3;
    private float countdownTimer = 30;

    private BattleState curState;

    private float endDelay = 2.0f;

    public Engine engine = new Engine();

    public BattleScreen(Game parent, int width, int height, GameUnit basePlayerUnit, GameUnit baseAiUnit, GameScreen prev) {
        super(parent, prev);

        this.batch = new SpriteBatch();
        this.game = parent;
        this.width = width;
        this.height = height;
        this.camera = new OrthographicCamera(width, height);
        this.camera.position.set(width / 2, height / 2, 0);
        int gridWidth = 3;
        int gridHeight = 3;
        grid = new BattleGrid(width, height, gridHeight, gridWidth);

        playerUnit = new BattleUnit(this, basePlayerUnit, gridHeight/2, 0);
        aiUnit = new BattleUnit(this, baseAiUnit, gridHeight/2, gridWidth * 2 - 1);

        engine.addSystem(new ExpiringSystem());
        engine.addSystem(new WarningSystem());
        engine.addSystem(new ProjectileSystem(this));

        engine.addSystem(new BlockingSystem());
        engine.addSystem(new DamageSystem(this));
        engine.addSystem(new MoveRenderingSystem(this));
        engine.addSystem(new WarningRenderingSystem(this));
        curState = BattleState.COUNTOFF;
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        renderBackground(batch);
        grid.render(batch);
        engine.update(delta);
        renderUI(batch);

        countdownTimer -= delta;
        grid.update(delta);

        switch (curState) {
            case COUNTOFF:
                renderCountoff(delta);
                break;
            case RUNNING:
                checkEndConditions();
                takeInput();
                updateAI(delta);
                break;
            case END_DEATH:
            case END_ENERGY:
            case END_TIME:
                renderEnd();
                if (endDelay <=0 ) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                        previousScreen();
                    }
                }
                endDelay-=delta;
                break;
        }
    }

    private void renderCountoff(float delta) {
        countOffTimer-=delta;
        int seconds = ((int) countOffTimer) % 60;
        batch.begin();
        switch (seconds) {
            case 2:
                redFont.draw(batch, readyLayout, 400/2-readyLayout.width/2,240/2-readyLayout.height/2);
                break;
            case 1:
                yellowFont.draw(batch, setLayout, 400/2-setLayout.width/2,240/2-setLayout.height/2);
                break;
            case 0:
                greenFont.draw(batch, fightLayout, 400/2-fightLayout.width/2,240/2-fightLayout.height/2);
                break;
        }
        if (countOffTimer <= 0 ) {
            curState = BattleState.RUNNING;
        }
        batch.end();
    }

    private void renderEnd() {
        GlyphLayout layout;
        batch.begin();
        switch (curState) {
            case END_DEATH:
                if (playerUnit.getCurHP() <= 0 && aiUnit.getCurHP() <= 0) {
                    // DRAW
                    layout = new GlyphLayout(resultsFont, "YOU BOTH DIED");
                    resultsFont.draw(batch, "YOU BOTH DIED", width/2 - layout.width/2, 100);
                } else if (playerUnit.getCurHP() <= 0) {
                    // YOU LOSE
                    layout = new GlyphLayout(resultsFont, "YOU DIED");
                    resultsFont.draw(batch, "YOU DIED", width/2 - layout.width/2, 100);

                } else if (aiUnit.getCurHP() <= 0) {
                    // YOU WIN
                    layout = new GlyphLayout(resultsFont, "ENEMY DIED");
                    resultsFont.draw(batch, "ENEMY DIED", width/2 - layout.width/2, 100);
                }
                break;
            case END_TIME:
                // TIME DRAW
                layout = new GlyphLayout(resultsFont, "OUT OF TIME");
                resultsFont.draw(batch, "OUT OF TIME", width/2 - layout.width/2, 100);
                break;
            case END_ENERGY:
                // ENERGY DRAW
                layout = new GlyphLayout(resultsFont, "OUT OF ENERGY");
                resultsFont.draw(batch, "OUT OF ENERGY", width/2 - layout.width/2, 100);
                break;
        }
        if (this.endDelay <= 0) {
            layout = new GlyphLayout(continueFont, "any key to continue");
            continueFont.draw(batch, "any key to continue", width/2 - layout.width/2, 50);
        }
        batch.end();
    }

    private void checkEndConditions() {
        if (aiUnit.getCurHP() == 0 || playerUnit.getCurHP() == 0) {
            curState = BattleState.END_DEATH;
            return;
        } else if (aiUnit.getEnergy() == 0 && playerUnit.getEnergy() == 0 && grid.getEffects().isEmpty()) {
            curState = BattleState.END_ENERGY;
        } else if (countdownTimer <= 0){
            curState = BattleState.END_TIME;
        }
        if (curState != BattleState.RUNNING) {
            engine.getSystem(DamageSystem.class).setProcessing(false);
        }
    }

    private void renderBackground(SpriteBatch batch) {
        batch.begin();
        batch.draw(AssetMgr.Textures.BATTLE_BG, 0,0);
        batch.end();
    }

    private void renderUI(SpriteBatch batch) {

        Texture hpBarTexture = AssetMgr.Textures.HP_BAR;

        renderHealthBar(batch, playerUnit, width/2 - hpBarTexture.getWidth() - 10, 3);
        renderHealthBar(batch, aiUnit, width/2 + 10, 3);

        renderTimer(batch, width/2, 200);
    }

    private void renderHealthBar(SpriteBatch batch, BattleUnit unit, int x, int y) {
        batch.begin();

        batch.draw(AssetMgr.Textures.HP_BAR, x, y);

        for (int i = unit.getEnergy(); i > 0 ; i--)
            batch.draw(AssetMgr.Textures.ENERGY, 51 + x - (i-1)*11, y+2);

        String hpString = String.format("%03d/%03d", unit.getCurHP(), unit.getMaxHP());
        GlyphLayout hpLayout = new GlyphLayout(hpFont, hpString);
        hpFont.draw(batch, hpString, x + AssetMgr.Textures.HP_BAR.getWidth() - hpLayout.width - 2, y+22);
        batch.end();

        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        double percentHP = 1.0*unit.getCurHP()/unit.getMaxHP();
        if (percentHP > .45)
            sr.setColor(Color.GREEN);
        else if (percentHP > .25)
            sr.setColor(Color.GOLDENROD);
        else
            sr.setColor(Color.SCARLET);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        if (percentHP > 0)
            sr.rect(x+9, y+8, (int) (52 * percentHP), 4);
        sr.end();
    }

    private void renderTimer(SpriteBatch batch, int x, int y) {
        int seconds = ((int) countdownTimer) % 60;
        String timerString = StringFormatter.format("%02d", seconds).getValue();
        GlyphLayout timerLayout = new GlyphLayout(timerFont, timerString);
        batch.begin();
        timerFont.draw(batch, timerLayout, x - timerLayout.width/2, y);
        batch.end();
    }

    protected void takeInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) playerUnit.move(0, -1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) playerUnit.move(0, 1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) playerUnit.move(1, 0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) playerUnit.move(-1, 0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) playerUnit.action1();
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) playerUnit.action2();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.previousScreen();
        }
    }

    public enum AiState {OFFENSE, DEFENSE};
    public AiState curAiState = AiState.OFFENSE;
    public int aiDelay = 30;
    public Random rng = new Random();

    public void updateAI(float delta) {
        if (aiUnit.getBase().getId().equals("bulba")) {
            bulbaAI(delta);
        } else if (aiUnit.getBase().getId().equals("ghast")) {
            ghastAI(delta);
        } else {
            bulbaAI(delta);
        }
    }

    private int attackDelay = 2;
    private void ghastAI(float delta) {

        if (aiDelay == 0) {
            aiDelay = 30;

            aiUnit.setGridCol(aiUnit.getGridCol() + 1);
            if (aiUnit.getGridRow() == playerUnit.getGridRow()) {
                aiUnit.move(rng.nextBoolean()?1:-1, 0);
                if (aiUnit.getGridRow() < 0) {
                    aiUnit.move(2, 0);
                } else if (aiUnit.getGridRow() >= 3) {
                    aiUnit.move(-2, 0);
                }
            }

            attackDelay--;
            if (attackDelay == 0) {
                attackDelay = 2;
                aiUnit.action1();
            }
        } else {
            aiDelay--;
        }
    }

    private void bulbaAI(float delta) {
        if (aiDelay == 0) {
            aiDelay = 30;
            if (rng.nextDouble() < .1) {
                if (curAiState == AiState.DEFENSE) {
                    curAiState = AiState.OFFENSE;
                } else {
                    curAiState = AiState.DEFENSE;
                }
            }
            if (playerUnit.getEnergy() < aiUnit.getEnergy() && rng.nextDouble() < .4) {
                curAiState = AiState.OFFENSE;
            }
            if (aiUnit.getEnergy() <= 0) {
                curAiState = AiState.DEFENSE;
            }
//            boolean isEnemyAttacking = false;
//            for (BattleEffect att : grid.getEffects()) {
//                if (att. == playerUnit) {
//                    isEnemyAttacking = true;
//                }
//            }
//            if (isEnemyAttacking && rng.nextDouble() < 0.2) {
//                curAiState = AiState.DEFENSE;
//            }

            switch(curAiState) {
                case OFFENSE:
                    if (aiUnit.getGridRow() != playerUnit.getGridRow()) {
//                        aiUnit.setGridRow(aiUnit.getGridRow()+(int)((1.0*playerUnit.getGridRow() - aiUnit.getGridRow())));
                        aiUnit.move(rng.nextBoolean()?1:-1, 0);
                        if (aiUnit.getGridRow() < playerUnit.getGridRow()) {
                            aiUnit.move(1, 0);
                        } else if (aiUnit.getGridRow() > playerUnit.getGridRow()) {
                            aiUnit.move(-1, 0);
                        }
//                        System.out.println((int)(1.0*playerUnit.getGridRow() - aiUnit.getGridRow()));
                    } //else {
                    if (aiUnit.getGridCol() == 3 && rng.nextDouble() < .7 && aiUnit.getEnergy() != 0) {
                        aiUnit.action1();
                    }
//                    }
                    aiUnit.setGridCol(aiUnit.getGridCol() - 1);
                    break;
                case DEFENSE:
                    aiUnit.setGridCol(aiUnit.getGridCol() + 1);
                    if (aiUnit.getGridRow() == playerUnit.getGridRow()) {
                        aiUnit.move(rng.nextBoolean()?1:-1, 0);
                        if (aiUnit.getGridRow() < 0) {
                            aiUnit.move(2, 0);
                        } else if (aiUnit.getGridRow() >= 3) {
                            aiUnit.move(-2, 0);
                        }
                    }
                    break;
            }
        } else {
            aiDelay--;
        }
    }
}
