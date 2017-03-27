package com.comp460.screens.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.comp460.MainGame;
import com.comp460.assets.FontManager;
import com.comp460.assets.SpriteManager;
import com.comp460.screens.battle.players.BattlePlayer;
import com.comp460.screens.battle.players.HumanPlayer;
import com.comp460.screens.battle.players.ai.GhastAi;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.common.GameScreen;
import com.comp460.common.GameUnit;
import com.sun.javafx.binding.StringFormatter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by matthewhammond on 2/15/17.
 */
public class BattleScreen extends GameScreen {

    public final int numRows = 3;
    public final int numCols = 6;

    private final TextureRegion background = SpriteManager.BATTLE.findRegion("bg/plains");
    private final TextureRegion tileLHS = SpriteManager.BATTLE.findRegion("bg/tile_plains_blue");
    private final TextureRegion tileRHS = SpriteManager.BATTLE.findRegion("bg/tile_plains_red");

    private final TextureRegion tileSideLHS = SpriteManager.BATTLE.findRegion("bg/tile_plains_blue_side");
    private final TextureRegion tileSideRHS = SpriteManager.BATTLE.findRegion("bg/tile_plains_red_side");

    private final TextureRegion hpBar = SpriteManager.BATTLE.findRegion("ui/hp_bar_new");
    private final TextureRegion energyBar = SpriteManager.BATTLE.findRegion("ui/energy");

    public final int tileWidth = tileLHS.getRegionWidth();
    public final int tileHeight = tileLHS.getRegionHeight();
    private final int tileSideHeight = tileSideLHS.getRegionHeight();

    public final float gridOffsetX = 0;
    public final float gridOffsetY = tileHeight;

    private static BitmapFont greenFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.GREEN, Color.BLACK, 2);
    private static BitmapFont yellowFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.ORANGE, Color.BLACK, 2);
    private static BitmapFont redFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.RED, Color.BLACK, 2);

    private static BitmapFont timerFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.RED);
    private static BitmapFont resultsFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 32, Color.WHITE, Color.BLACK, 2);
    private static BitmapFont continueFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 16, Color.WHITE, Color.BLACK, 1);

    private static BitmapFont hpFont = FontManager.getFont(FontManager.KEN_PIXEL, 8, Color.WHITE);
    private static BitmapFont movesFont = FontManager.getFont(FontManager.KEN_PIXEL, 8, Color.WHITE);

    private static GlyphLayout readyLayout = new GlyphLayout(redFont, "READY");
    private static GlyphLayout setLayout = new GlyphLayout(yellowFont, "SET");
    private static GlyphLayout fightLayout = new GlyphLayout(greenFont, "FIGHT!");

    private GlyphLayout drawLayout = new GlyphLayout(resultsFont, "DRAW");
    private GlyphLayout p1WinsLayout = new GlyphLayout(resultsFont, "YOU WIN!");
    private GlyphLayout p2WinsLayout = new GlyphLayout(resultsFont, "YOU LOSE!");
    private GlyphLayout outOfTimeLayout = new GlyphLayout(resultsFont, "OUT OF TIME");

    private enum BattleState {COUNTOFF, RUNNING, END_P1_DIED, END_P2_DIED, END_DRAW, END_TIME}

    private float countOffTimer = 3;
    private float countdownTimer;

    private BattleState curState = BattleState.COUNTOFF;

    private float endDelay = 2.0f;

    public BattleUnit p1Unit;
    public BattleUnit p2Unit;

    public BattlePlayer player1;
    public BattlePlayer player2;

    public GlyphLayout p1MovesLayout;
    public GlyphLayout p2MovesLayout;

    public List<BattleAnimation> playingAnimations = new ArrayList<>();

    public Vector2[][] tileOffsets;
    public Vector2 tileDefaultOffset = new Vector2(0, 0);

    public boolean exitAllowed;

    public BattleScreen(MainGame game, GameScreen prevScreen, GameUnit p1UnitBase, GameUnit p2UnitBase, boolean p1Initiated, boolean exitAllowed, float time) {
        super(game, prevScreen);

        this.countdownTimer = time;
        this.exitAllowed = exitAllowed;
        tileOffsets = new Vector2[numRows][numCols];
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                tileOffsets[r][c] = new Vector2(0, 0);
            }
        }

        this.p1Unit = p1UnitBase.buildBattleUnit(this, 1, 1);
        this.p2Unit = p2UnitBase.buildBattleUnit(this, 1, numCols - 2);

        // give energy advantage
        if (p1Initiated && !exitAllowed)
            p2Unit.removeEnergy(3);
        else if (!p1Initiated && !exitAllowed)
            p1Unit.removeEnergy(3);
        this.player1 = new HumanPlayer(p1Unit);
        this.player2 = new GhastAi(p2Unit, p1Unit, this);

        this.p1MovesLayout = new GlyphLayout(movesFont, "Z: " + p1Unit.ability1.name + "\nX: " + p1Unit.ability2.name);
        this.p2MovesLayout = new GlyphLayout(movesFont, "1: " + p2Unit.ability1.name + "\n2: " + p2Unit.ability2.name);

        drawLayout = new GlyphLayout(resultsFont, "DRAW");
        p1WinsLayout = new GlyphLayout(resultsFont, p1Unit.name + " WINS!");
        p2WinsLayout = new GlyphLayout(resultsFont, p2Unit.name + " WINS!");
        outOfTimeLayout = new GlyphLayout(resultsFont, "OUT OF TIME");
    }

    public void addAnimation(BattleAnimation anim) {
        this.playingAnimations.add(anim);
    }

    public float rowToScreenY(int row, int col) {
        return (tileHeight * row) + gridOffsetY + tileOffsets[row][col].y;
    }

    public float colToScreenX(int row, int col) {
        return ((this.width / 2f) + (col - numCols / 2f) * tileWidth) + gridOffsetX + tileOffsets[row][col].x;
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
        for (Iterator<BattleAnimation> iter = playingAnimations.iterator(); iter.hasNext(); ) {
            BattleAnimation anim = iter.next();
            anim.update(delta);
            if (anim.duration <= 0) {
                iter.remove();
            }
        }
        if (game.controller.endJustPressed() && exitAllowed) {
            previousScreen();
        }
        switch (curState) {
            case COUNTOFF:
                countoff(delta);
                break;
            case RUNNING:
                tickTimer(delta);
                checkEndConditions(delta);
                if (curState != BattleState.RUNNING) {
                    return;
                }
                player1.update(delta);
                player2.update(delta);

                p1Unit.update(delta);
                p2Unit.update(delta);
                break;
            case END_DRAW:
            case END_P1_DIED:
            case END_P2_DIED:
            case END_TIME:
                endDelay -= delta;
                if (endDelay <= 0) {
                    if (game.controller.button1JustPressed()) {
                        previousScreen();
                    }
                }
                break;
        }
    }

    private void tickTimer(float delta) {
        countdownTimer -= delta;
    }

    //region render
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
                break;
            case END_DRAW:
            case END_P1_DIED:
            case END_P2_DIED:
            case END_TIME:
                renderEnd(delta);
                break;
        }
    }

    private void renderScene() {
        batch.begin();
        batch.draw(background, 0, 0);

        for (int r = numRows - 1; r >= 0; r--) {
            for (int c = 0; c < numCols; c++) {
                float x = colToScreenX(r, c);
                float y = rowToScreenY(r, c);
                if (c < numCols / 2) {
                    batch.draw(tileLHS, x, y);
                    batch.draw(tileSideLHS, x, y - tileSideHeight);

                } else {
                    batch.draw(tileRHS, x, y);
                    batch.draw(tileSideRHS, x, y - tileSideHeight);
                }
                tileOffsets[r][c].lerp(tileDefaultOffset, 0.3f);
            }
        }
        batch.end();
    }

    private void renderUnitsAndEffects(float delta) {
        batch.begin();
        p1Unit.render(batch, delta);
        p2Unit.render(batch, delta);
        if (curState.equals(BattleState.RUNNING)) {
            for (BattleAnimation anim : playingAnimations) {
                anim.render(batch, delta);
            }
        }
        batch.end();
    }

    private void renderUI(float delta) {

        renderHealthBar(p1Unit, width / 2 - hpBar.getRegionWidth() - 10, 3);
        renderHealthBar(p2Unit, width / 2 + 10, 3);

        batch.begin();
        movesFont.draw(batch, p1MovesLayout, 10, 220);
        movesFont.draw(batch, p2MovesLayout, 400 - p2MovesLayout.width - 10, 220);
        batch.end();

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

    private void renderHealthBar(BattleUnit unit, float x, float y) {
        batch.begin();

        batch.draw(hpBar, x, y);

        for (int i = unit.curEnergy; i > 0; i--)
            batch.draw(energyBar, 51 + x - (i - 1) * 11, y + 2);

        String hpString = String.format("%03d/%03d", unit.curHP, unit.maxHP);
        GlyphLayout hpLayout = new GlyphLayout(hpFont, hpString);
        hpFont.draw(batch, hpString, x + hpBar.getRegionWidth() - hpLayout.width - 2, y + 22);
        batch.end();

        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        double percentHP = 1.0 * unit.curHP / unit.maxHP;
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

    private void renderEnd(float delta) {
        GlyphLayout layout = drawLayout;
        batch.begin();
        switch (curState) {
            case END_DRAW:
                layout = drawLayout;
                break;
            case END_P1_DIED:
                layout = p2WinsLayout;
                break;
            case END_P2_DIED:
                layout = p1WinsLayout;
                break;
            case END_TIME:
                layout = outOfTimeLayout;
                break;
        }
        resultsFont.draw(batch, layout, width / 2 - layout.width / 2, 100);

        if (this.endDelay <= 0) {
            layout = new GlyphLayout(continueFont, "z to continue");
            continueFont.draw(batch, "z to continue", width / 2 - layout.width / 2, 50);
        }
        batch.end();
    }

    //endregion

    private void checkEndConditions(float delta) {

        boolean p1Dead = p1Unit.curHP <= 0;
        boolean p2Dead = p2Unit.curHP <= 0;

        if (p1Dead && p2Dead) {
            curState = BattleState.END_DRAW;
            return;
        } else if (p1Dead) {
            curState = BattleState.END_P1_DIED;
            return;
        } else if (p2Dead) {
            curState = BattleState.END_P2_DIED;
            return;
        }

        if (countdownTimer <= 0) {
            curState = BattleState.END_TIME;
            return;
        }
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
        }
        batch.end();
    }

    @Override
    public void previousScreen() {

        this.p1Unit.updateBase();
        this.p2Unit.updateBase();

        super.previousScreen();
    }

    @Override
    public void show() {
        super.show();
        game.playMusic("music/battle-in-the-winter.mp3");
    }
}