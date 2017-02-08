package com.comp460.battle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.AssetManager;
import com.comp460.FontManager;
import com.comp460.common.GameUnit;
import com.comp460.launcher.BattlePracticeMenu;
import com.sun.javafx.binding.StringFormatter;

import java.util.Random;

/**
 * Created by matthewhammond on 1/29/17.
 */
public class BattleScreen extends ScreenAdapter {

    private static BitmapFont timerFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.RED);
    private static BitmapFont resultsFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 32, Color.WHITE);

    private enum BattleState {RUNNING, END_ENERGY, END_DEATH, END_TIME};

    private BattleUnit playerUnit;

    private BattleUnit aiUnit;

    private BattleGrid grid;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Game game;

    private int width, height;

    private float countdownTimer = 10;

    private BattleState curState;

    private float endDelay = 2.0f;

    public BattleScreen(Game parent, int width, int height, GameUnit basePlayerUnit, GameUnit baseAiUnit) {

        this.batch = new SpriteBatch();
        this.game = parent;
        this.width = width;
        this.height = height;
        this.camera = new OrthographicCamera(width, height);
        this.camera.position.set(width / 2, height / 2, 0);
        int gridWidth = 3;
        int gridHeight = 3;
        grid = new BattleGrid(width, height, gridHeight, gridWidth);

        playerUnit = new BattleUnit(grid, basePlayerUnit, gridHeight/2, 0);
        aiUnit = new BattleUnit(grid, baseAiUnit, gridHeight/2, gridWidth * 2 - 1);

        curState = BattleState.RUNNING;
    }

    @Override
    public void render(float delta) {
        update(delta);
        batch.setProjectionMatrix(camera.combined);
        renderBackground(batch);
        grid.render(batch);
        renderUI(batch);

        if (curState != BattleState.RUNNING) {
            renderEnd();
            if (endDelay <=0 ) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                    game.setScreen(new BattlePracticeMenu(game));
                    dispose();
                }
            }
            endDelay-=delta;
        }
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
//        if (freezeDelay < 0) {
//            layout = new GlyphLayout(font, "any key to continue");
//            font.draw(batch, "any key to continue", width/2 - layout.width/2, 50);
//        }
        batch.end();
    }

    public void update(float delta) {
        checkEndConditions();
        camera.update();
        if (curState == BattleState.RUNNING) {
            takeInput();
            updateAI(delta);
            countdownTimer -= delta;
        }
        grid.update(delta);
    }

    private void checkEndConditions() {
        if (aiUnit.getCurHP() == 0 || playerUnit.getCurHP() == 0) {
            curState = BattleState.END_DEATH;
            return;
        } else if (aiUnit.getEnergy() == 0 && playerUnit.getEnergy() == 0) {
            curState = BattleState.END_ENERGY;
        }
//        } else if (countdownTimer <= 0){
//            curState = BattleState.END_TIME;
//        }
    }

    private void renderBackground(SpriteBatch batch) {
        batch.begin();
        batch.draw(AssetManager.Textures.BATTLE_BG, 0,0);
        batch.end();
    }

    private void renderUI(SpriteBatch batch) {

        Texture hpBarTexture = AssetManager.Textures.HP_BAR;

        renderHealthBar(batch, playerUnit, width/2 - hpBarTexture.getWidth() - 10, 3);
        renderHealthBar(batch, aiUnit, width/2 + 10, 3);

        renderTimer(batch, width/2, 200);
    }

    private void renderHealthBar(SpriteBatch batch, BattleUnit unit, int x, int y) {
        batch.begin();

        batch.draw(AssetManager.Textures.HP_BAR, x, y);

        for (int i = unit.getEnergy(); i > 0 ; i--)
            batch.draw(AssetManager.Textures.ENERGY, 51 + x - (i-1)*11, y+2);

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
//                    if (aiUnit.row != playerUnit.row) {
//                        aiUnit.row += (int)((1.0*playerUnit.row - aiUnit.row) / 2.0);
//                    } else {
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
