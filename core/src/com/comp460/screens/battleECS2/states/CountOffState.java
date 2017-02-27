package com.comp460.screens.battleECS2.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.comp460.assets.FontManager;
import com.comp460.common.GameState;
import com.comp460.screens.battleECS2.BattleScreen;

/**
 * Created by matthewhammond on 2/25/17.
 */
public class CountOffState implements GameState {

    private float timer = 3f;

    private static BitmapFont readyFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.GREEN, Color.BLACK, 2);
    public static BitmapFont setFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.ORANGE, Color.BLACK, 2);
    public static BitmapFont fightFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.RED, Color.BLACK, 2);

    private static GlyphLayout readyLayout = new GlyphLayout(readyFont, "READY");
    private static GlyphLayout setLayout = new GlyphLayout(setFont, "SET");
    private static GlyphLayout fightLayout = new GlyphLayout(fightFont, "FIGHT!");

    BattleScreen screen;

    public CountOffState(BattleScreen screen) {
        this.screen = screen;
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {
        timer-=delta;
        if (timer <= 0) {
            this.screen.setState(new RunningState(this.screen));
        }
    }

    @Override
    public void render(SpriteBatch batch, SpriteBatch uiBatch) {
        batch.begin();
        switch ((int)timer) {
            case 2:
                readyFont.draw(uiBatch, readyLayout, 400 / 2 - readyLayout.width / 2, 240 / 2 - readyLayout.height / 2);
                break;
            case 1:
                setFont.draw(uiBatch, setLayout, 400 / 2 - setLayout.width / 2, 240 / 2 - setLayout.height / 2);
                break;
            case 0:
                fightFont.draw(uiBatch, fightLayout, 400 / 2 - fightLayout.width / 2, 240 / 2 - fightLayout.height / 2);
                break;
        }
        batch.end();
    }
}
