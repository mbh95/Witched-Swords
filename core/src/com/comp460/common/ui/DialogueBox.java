package com.comp460.common.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.comp460.MainGame;
import com.comp460.Settings;
import com.comp460.screens.launcher.practice.battle.BattlePracticeAssets;

/**
 * Created by Belinda on 4/14/17.
 */
public class DialogueBox {
    private TextureRegion character;
    private GlyphLayout layout;
    private MainGame game;

    private float infoWidth = 350;
    private float infoHeight = 50 - 2;

    private float padding = 4;
    private float w = infoWidth;
    private float h;
    private float x = (Settings.INTERNAL_WIDTH-w)/2;
    private float y = 0;

    public DialogueBox(TextureRegion character, String text, MainGame game) {
        this.character = character;
        layout = new GlyphLayout(BattlePracticeAssets.FONT_INFO, text, Color.WHITE, w - 2 * padding - character.getRegionWidth(), Align.left, true);
        this.game = game;
        h = Math.max(infoHeight, layout.height + 2 * padding);
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        BattlePracticeAssets.NP_INFO_BG.draw(batch, (int) x, (int) y, (int) w, (int) h);
        BattlePracticeAssets.FONT_INFO.draw(batch, layout, x + padding + character.getRegionWidth(), y + h - padding);
        batch.end();
    }
}
