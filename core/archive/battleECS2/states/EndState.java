package com.comp460.screens.battleECS2.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.comp460.assets.FontManager;
import com.comp460.screens.battleECS2.BattleScreen;

/**
 * Created by matth on 4/9/2017.
 */
public class EndState extends BattleState {

    public enum EndCondition {TIME, PLAYER_WINS, AI_WINS}

    private static BitmapFont resultsFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 32, Color.WHITE, Color.BLACK, 2);
    private static BitmapFont continueFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 16, Color.WHITE, Color.BLACK, 1);

    private static GlyphLayout continueLayout = new GlyphLayout(continueFont, "z to continue");

    private EndCondition endTrigger;
    private GlyphLayout endTextLayout;

    private float endDelay;

    private float zToContinuePhase;
    private boolean zToContinueVisible;

    public EndState(BattleScreen parentScreen, EndCondition endTrigger) {
        super(parentScreen);
        this.endTrigger = endTrigger;

        switch (endTrigger) {
            case TIME:
                endTextLayout = new GlyphLayout(resultsFont, "OUT OF TIME");
                break;
            case PLAYER_WINS:
                endTextLayout = new GlyphLayout(resultsFont, parentScreen.playerUnit.name + " WINS!");
                break;
            case AI_WINS:
                endTextLayout = new GlyphLayout(resultsFont, parentScreen.aiUnit.name + " WINS!");
                break;
        }
    }

    @Override
    public void init() {
        this.endDelay = 1.5f;
        this.zToContinuePhase = 0.5f;
        this.zToContinueVisible = true;
    }

    @Override
    public void render(float delta) {

        endDelay -= delta;
        if (endDelay <= 0) {
            if (parentScreen.game.controller.button1JustPressed()) {
                parentScreen.game.setScreen(parentScreen.prevScreen);
            }
        }

        int shift = 50;

        SpriteBatch batch = parentScreen.uiBatch;
        resultsFont.draw(batch, endTextLayout, parentScreen.width / 2 - endTextLayout.width / 2, 100 + shift);
        if (this.endDelay <= 0 && zToContinueVisible) {
            continueFont.draw(batch, continueLayout, parentScreen.width / 2 - continueLayout.width / 2, 50 + shift);
        }
        batch.end();

        if (zToContinuePhase <= 0) {
            zToContinueVisible = !zToContinueVisible;
            zToContinuePhase = 0.5f;
        }
        zToContinuePhase -= delta;
    }

    @Override
    public void dispose() {

    }
}
