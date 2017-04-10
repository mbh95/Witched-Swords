package com.comp460.screens.battleECS2.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.comp460.assets.FontManager;
import com.comp460.screens.battleECS2.BattleScreen;

/**
 * Created by matthewhammond on 2/25/17.
 */
public class CountOffState extends BattleState {

    private static BitmapFont readyFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.GREEN, Color.BLACK, 2);
    private static BitmapFont setFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.ORANGE, Color.BLACK, 2);
    private static BitmapFont fightFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.RED, Color.BLACK, 2);

    private static GlyphLayout readyLayout = new GlyphLayout(readyFont, "READY");
    private static GlyphLayout setLayout = new GlyphLayout(setFont, "SET");
    private static GlyphLayout fightLayout = new GlyphLayout(fightFont, "FIGHT!");

    private float timer;

    public CountOffState(BattleScreen parentScreen, float duration) {
        super(parentScreen);

        this.timer = duration;
    }

    @Override
    public void init() {
    }

    @Override
    public void render(float delta) {

        this.parentScreen.engine.update(delta);

        timer -= delta;
        if (timer <= 0) {
            this.parentScreen.setState(new RunningState(this.parentScreen));
        }

        SpriteBatch batch = this.parentScreen.batch;
        batch.begin();
        switch ((int) timer) {
            case 2:
                readyFont.draw(batch, readyLayout, 400 / 2 - readyLayout.width / 2, 240 / 2 - readyLayout.height / 2);
                break;
            case 1:
                setFont.draw(batch, setLayout, 400 / 2 - setLayout.width / 2, 240 / 2 - setLayout.height / 2);
                break;
            case 0:
                fightFont.draw(batch, fightLayout, 400 / 2 - fightLayout.width / 2, 240 / 2 - fightLayout.height / 2);
                break;
        }
        batch.end();
    }

    @Override
    public void dispose() {

    }
}
