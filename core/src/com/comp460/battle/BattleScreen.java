package com.comp460.battle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.FontManager;
import com.comp460.assets.SpriteManager;
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


    public BattleScreen(Game game, GameScreen prevScreen, GameUnit p1UnitBase, GameUnit p2UnitBase) {
        super(game, prevScreen);

        this.p1Unit = new BattleUnit(this, 0, 0, p1UnitBase);
        this.p2Unit = new BattleUnit(this, 0, numCols - 1, p2UnitBase);

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


}
