package com.comp460.screens.battleECS2.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.comp460.assets.FontManager;
import com.comp460.screens.battleECS2.BattleScreen;
import com.sun.javafx.binding.StringFormatter;

/**
 * Created by matthewhammond on 2/25/17.
 */
public class RunningState extends BattleState {

    private static BitmapFont timerFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.RED);

    private float timer;

    public RunningState(BattleScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void init() {
        this.timer = parentScreen.duration;
    }

    @Override
    public void render(float delta) {
        timer -= delta;
        if (timer <= 0) {
            this.parentScreen.setState(new RunningState(this.parentScreen));
        }

        renderTimer(parentScreen.uiBatch, parentScreen.width / 2, 210);
    }

    private void renderTimer(SpriteBatch batch, float x, float y) {
        int seconds = ((int) timer) % 60;
        String timerString = StringFormatter.format("%02d", seconds).getValue();
        GlyphLayout timerLayout = new GlyphLayout(timerFont, timerString);
        batch.begin();
        timerFont.draw(batch, timerLayout, x - timerLayout.width / 2, y);
        batch.end();
    }

    @Override
    public void dispose() {

    }
}
