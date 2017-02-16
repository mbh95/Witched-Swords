package com.comp460.battle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.FontManager;
import com.comp460.assets.SpriteManager;
import com.comp460.battle.players.BattlePlayer;
import com.comp460.battle.players.HumanPlayer;
import com.comp460.battle.units.BattleUnit;
import com.comp460.common.GameScreen;
import com.comp460.common.GameUnit;

/**
 * Created by matthewhammond on 2/15/17.
 */
public class BattleScreen extends GameScreen {

    public final int numRows = 3;
    public final int numCols = 6;

    public final TextureRegion background = SpriteManager.BATTLE.findRegion("bg/plains");
    public final TextureRegion tileLHS = SpriteManager.BATTLE.findRegion("bg/tile_plains_blue");
    public final TextureRegion tileRHS = SpriteManager.BATTLE.findRegion("bg/tile_plains_red");

    public final TextureRegion tileSideLHS = SpriteManager.BATTLE.findRegion("bg/tile_plains_blue_side");
    public final TextureRegion tileSideRHS = SpriteManager.BATTLE.findRegion("bg/tile_plains_red_side");

    public final TextureRegion hpBar = SpriteManager.BATTLE.findRegion("ui/hp_bar_new");
    public final TextureRegion energyBar = SpriteManager.BATTLE.findRegion("ui/energy");

    public final int tileWidth = tileLHS.getRegionWidth();
    public final int tileHeight = tileLHS.getRegionHeight();
    public final int tileSideHeight = tileSideLHS.getRegionHeight();

    public final float gridOffsetX = 0;
    public final float gridOffsetY = tileHeight;

    private static BitmapFont greenFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.GREEN, Color.BLACK, 2);
    private static BitmapFont yellowFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.ORANGE, Color.BLACK, 2);
    private static BitmapFont redFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.RED, Color.BLACK, 2);

    private static BitmapFont timerFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.RED);
    private static BitmapFont resultsFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 32, Color.WHITE, Color.BLACK, 2);
    private static BitmapFont continueFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 16, Color.WHITE);

    private static BitmapFont hpFont = FontManager.getFont(FontManager.KEN_PIXEL, 8, Color.WHITE);

    private static GlyphLayout readyLayout = new GlyphLayout(redFont, "READY");
    private static GlyphLayout setLayout = new GlyphLayout(yellowFont, "SET");
    private static GlyphLayout fightLayout = new GlyphLayout(greenFont, "FIGHT!");

    private static GlyphLayout tieLayout = new GlyphLayout(resultsFont, "DRAW");
    private static GlyphLayout p1WinsLayout = new GlyphLayout(resultsFont, "YOU WIN!");
    private static GlyphLayout p2WinsLayout = new GlyphLayout(resultsFont, "YOU LOSE!");
    private static GlyphLayout stalemateLayout = new GlyphLayout(resultsFont, "STALEMATE");
    private static GlyphLayout outOfTimeLayout = new GlyphLayout(resultsFont, "OUT OF TIME");

    private enum BattleState {COUNTOFF, RUNNING, END_STALEMATE, END_PLAYER_DIED, END_AI_DIED, END_BOTH_DIED, END_TIME}

    private float countOffTimer = 3;
    private float countdownTimer = 30;

    private BattleState curState = BattleState.COUNTOFF;

    private float endDelay = 2.0f;

    public BattleUnit p1Unit;
    public BattleUnit p2Unit;

    public BattlePlayer player1;
    public BattlePlayer player2;

    public BattleScreen(Game game, GameScreen prevScreen, GameUnit p1UnitBase, GameUnit p2UnitBase) {
        super(game, prevScreen);
        this.p1Unit = new BattleUnit(this, 0, 0, p1UnitBase);
        this.p2Unit = new BattleUnit(this, 0, numCols - 1, p2UnitBase);

        this.player1 = new HumanPlayer(p1Unit);
        this.player2 = new HumanPlayer(p2Unit);
    }

    public float rowToScreenY(int row) {
        return (tileHeight * row) + gridOffsetY;
    }

    public float colToScreenX(int col) {
        return ((this.width / 2f) + (col - numCols / 2f) * tileWidth) + gridOffsetX;
    }

    public boolean isOnGrid(int row, int col) {
        return (row >= 0 && row < numRows) && (col >= 0 && col < numCols);
    }

    public boolean isOnLHS(int row, int col) {
        return isOnGrid(row, col) && (col < numCols / 2);
    }

    public boolean isOnRHS(int row, int col) {
        return isOnGrid(row, col) && (col >= numCols / 2);
    }

    public void update(float delta) {
        switch (curState) {
            case COUNTOFF:
                countoff(delta);
                break;
            case RUNNING:
                tickTimer(delta);
                checkEndConditions(delta);
                break;
            case END_STALEMATE:
            case END_BOTH_DIED:
            case END_PLAYER_DIED:
            case END_AI_DIED:
            case END_TIME:
                renderEnd(delta);
                endDelay -= delta;
                if (endDelay <= 0) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                        previousScreen();
                    }
                }
                break;
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        update(delta);

        renderScene();

        renderUnitsAndEffects(delta);

        renderUI(delta);

        switch (curState) {
            case COUNTOFF:
                countoff(delta);
                break;
            case RUNNING:
                tickTimer(delta);
                checkEndConditions(delta);
                break;
            case END_STALEMATE:
            case END_BOTH_DIED:
            case END_PLAYER_DIED:
            case END_AI_DIED:
            case END_TIME:
                renderEnd(delta);
                endDelay -= delta;
                if (endDelay <= 0) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                        previousScreen();
                    }
                }
                break;
        }
    }



    private void renderScene() {
        batch.begin();
        batch.draw(background, 0, 0);

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (c < numCols / 2) {
                    batch.draw(tileLHS, colToScreenX(c), rowToScreenY(r));
                } else {
                    batch.draw(tileRHS, colToScreenX(c), rowToScreenY(r));
                }
            }
        }
        for (int c = 0; c < numCols; c++) {
            float x = colToScreenX(c);
            float y = rowToScreenY(0) - tileSideHeight;
            if (c < numCols / 2) {
                batch.draw(tileSideLHS, x, y);
            } else {
                batch.draw(tileSideRHS, x, y);
            }
        }
        batch.end();
    }

    private void renderUnitsAndEffects(float delta) {
        batch.begin();
        p1Unit.render(batch, delta);
        p2Unit.render(batch, delta);
        batch.end();
    }

    private void tickTimer(float delta) {
        countdownTimer -= delta;
    }

    private void renderUI(float delta) {

        for (Entity e : engine.getEntitiesFor(playerUnitsFamily)) {
            HealthComponent health = Mappers.healthM.get(e);
            EnergyComponent energy = Mappers.energyM.get(e);
            if (health != null && energy != null) {
                renderHealthBar(health, energy, width / 2 - hpBar.getRegionWidth() - 10, 3);
            }
        }

        for (Entity e : engine.getEntitiesFor(aiUnitsFamily)) {
            HealthComponent health = Mappers.healthM.get(e);
            EnergyComponent energy = Mappers.energyM.get(e);
            if (health != null && energy != null) {
                renderHealthBar(health, energy, width / 2 + 10, 3);
            }
        }

        renderTimer(width / 2, 200);
    }

    private void renderTimer(float x, float y) {
        int seconds = ((int) countdownTimer) % 60;
        String timerString = StringFormatter.format("%02d", seconds).getValue();
        GlyphLayout timerLayout = new GlyphLayout(timerFont, timerString);
        batch.begin();
        timerFont.draw(batch, timerLayout, x - timerLayout.width / 2, y);
        batch.end();
    }

    private void renderHealthBar(HealthComponent health, EnergyComponent energy, float x, float y) {
        batch.begin();

        batch.draw(hpBar, x, y);

        for (int i = energy.curEnergy; i > 0; i--)
            batch.draw(energyBar, 51 + x - (i - 1) * 11, y + 2);

        String hpString = String.format("%03d/%03d", health.curHP, health.maxHP);
        GlyphLayout hpLayout = new GlyphLayout(hpFont, hpString);
        hpFont.draw(batch, hpString, x + hpBar.getRegionWidth() - hpLayout.width - 2, y + 22);
        batch.end();

        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        double percentHP = 1.0 * health.curHP / health.maxHP;
        if (percentHP > .45)
            sr.setColor(Color.GREEN);
        else if (percentHP > .25)
            sr.setColor(Color.GOLDENROD);
        else
            sr.setColor(Color.SCARLET);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        if (percentHP > 0)
            sr.rect(x + 9, y + 8, (int) (52 * percentHP), 4);
        sr.end();
    }

    private void checkEndConditions(float delta) {
        if (engine.getEntitiesFor(movesToKeepAliveFamily).size() > 0) {
            return;
        }

        boolean allPlayersDead = true;
        for (Entity e : engine.getEntitiesFor(playerUnitsFamily)) {
            HealthComponent health = Mappers.healthM.get(e);
            if (health.curHP > 0) {
                allPlayersDead = false;
                break;
            }
        }

        boolean allAiDead = true;
        for (Entity e : engine.getEntitiesFor(aiUnitsFamily)) {
            HealthComponent health = Mappers.healthM.get(e);
            if (health.curHP > 0) {
                allAiDead = false;
                break;
            }
        }

        if (allPlayersDead && allAiDead) {
            curState = BattleState.END_BOTH_DIED;
            return;
        } else if (allPlayersDead) {
            curState = BattleState.END_PLAYER_DIED;
            return;
        } else if (allAiDead) {
            curState = BattleState.END_AI_DIED;
            return;
        }

        if (countdownTimer <= 0) {
            curState = BattleState.END_TIME;
            return;
        }

        boolean allPlayersIdle = true;
        for (Entity e : engine.getEntitiesFor(playerUnitsFamily)) {
            if (canUseAnyAbilities(e)) {
                allPlayersIdle = false;
                break;
            }
        }
        boolean allAiIdle = true;
        for (Entity e : engine.getEntitiesFor(aiUnitsFamily)) {
            if (canUseAnyAbilities(e)) {
                allAiIdle = false;
                break;
            }
        }

        if (allPlayersIdle && allAiIdle) {
            curState = BattleState.END_STALEMATE;
        }

        if (curState != BattleState.RUNNING) {
            engine.getSystem(UnitControlSystem.class).setProcessing(false);
        }
    }

    private void renderEnd(float delta) {
        GlyphLayout layout = tieLayout;
        batch.begin();
        switch (curState) {
            case END_BOTH_DIED:
                layout = tieLayout;
                break;
            case END_PLAYER_DIED:
                layout = p2WinsLayout;
                break;
            case END_AI_DIED:
                layout = p1WinsLayout;
                break;
            case END_TIME:
                layout = outOfTimeLayout;
                break;
            case END_STALEMATE:
                layout = stalemateLayout;
                break;
        }
        resultsFont.draw(batch, layout, width / 2 - layout.width / 2, 100);


        if (this.endDelay <= 0) {
            layout = new GlyphLayout(continueFont, "any key to continue");
            continueFont.draw(batch, "any key to continue", width / 2 - layout.width / 2, 50);
        }
        batch.end();
    }

    public void countoff(float delta) {
        countOffTimer -= delta;
        int seconds = ((int) countOffTimer) % 60;
        batch.begin();
        switch (seconds) {
            case 2:
                redFont.draw(batch, readyLayout, 400 / 2 - readyLayout.width / 2, 240 / 2 - readyLayout.height / 2);
                break;
            case 1:
                yellowFont.draw(batch, setLayout, 400 / 2 - setLayout.width / 2, 240 / 2 - setLayout.height / 2);
                break;
            case 0:
                greenFont.draw(batch, fightLayout, 400 / 2 - fightLayout.width / 2, 240 / 2 - fightLayout.height / 2);
                break;
        }
        if (countOffTimer <= 0) {
            curState = BattleState.RUNNING;
            engine.getSystem(UnitControlSystem.class).setProcessing(true);
        }
        batch.end();
    }
}