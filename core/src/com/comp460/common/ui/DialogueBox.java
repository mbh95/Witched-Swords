package com.comp460.common.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.comp460.MainGame;
import com.comp460.Settings;
import com.comp460.common.GameScreen;
import com.comp460.screens.launcher.practice.battle.BattlePracticeAssets;

/**
 * Created by Belinda on 4/14/17.
 */
public class DialogueBox {
    private TextureRegion character;
    private GlyphLayout layout;
    private GameScreen parentScreen;

    private float infoWidth = 400;
    private float infoHeight = 50 - 2;

    private float padding = 4;
    private float w = infoWidth;
    private float h;
    private float x = (Settings.INTERNAL_WIDTH - w) / 2;
    private float y = 0;
    private DialogueBox next;

    public DialogueBox(TextureRegion character, String text, GameScreen parentScreen) {
        this(character, text, parentScreen, null);
    }

    public DialogueBox(TextureRegion character, String text, GameScreen parentScreen, DialogueBox next) {
        this.character = character;
        layout = new GlyphLayout(BattlePracticeAssets.FONT_INFO, text, Color.WHITE, w - 2 * padding - character.getRegionWidth(), Align.left, true);
        this.parentScreen = parentScreen;
        h = Math.max(infoHeight, layout.height + 2 * padding);
        this.next = next;
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        BattlePracticeAssets.NP_INFO_BG.draw(batch, (int) x, (int) y, (int) w, (int) h);
        BattlePracticeAssets.FONT_INFO.draw(batch, layout, x + padding + character.getRegionWidth(), y + h - padding);
        batch.draw(character, x + padding, y + h - padding - character.getRegionHeight());
        batch.end();
    }

    public DialogueBox update() {
        if (parentScreen.game.controller.button1JustPressedDestructive()) {
            return next;
        } else {
            return this;
        }
    }

    public static DialogueBox buildList(GameScreen parentScreen, DialogueBoxTemplate... boxes) {
        DialogueBox prev = null;
        for (int i = boxes.length - 1; i >= 0; i--) {
            DialogueBox next = new DialogueBox(boxes[i].icon, boxes[i].text, parentScreen, prev);
            prev = next;
        }
        return prev;
    }

    public static class DialogueBoxTemplate {
        public TextureRegion icon;
        public String text;
        public DialogueBoxTemplate(TextureRegion icon, String text) {
            this.icon = icon;
            this.text=  text;
        }
    }
}
