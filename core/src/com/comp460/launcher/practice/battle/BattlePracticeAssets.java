package com.comp460.launcher.practice.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.FontManager;

/**
 * Created by matthewhammond on 2/11/17.
 */
public class BattlePracticeAssets {
    private static BattlePracticeAssets singleton;

    public BitmapFont FONT_BATTLE_PORTRAIT;
    public BitmapFont FONT_FIGHT;
    public BitmapFont FONT_VS;

    public TextureRegion TEXTURE_SQUARE;
    public TextureRegion TEXTURE_SQUARE_HOVERED;

    public TextureRegion TEXTURE_BACK_BUTTON;
    public TextureRegion TEXTURE_BACK_BUTTON_HOVERED;

    public TextureRegion TEXTURE_BG;

    public TextureRegion TEXTURE_PLAYER_AREA;
    public TextureRegion TEXTURE_AI_AREA;

    public TextureRegion TEXTURE_FIGHT_BUTTON;
    public TextureRegion TEXTURE_FIGHT_BUTTON_HOVER;

    public static BattlePracticeAssets getInstance() {
        if (singleton == null) {
            singleton = new BattlePracticeAssets();
        }
        return singleton;
    }

    private BattlePracticeAssets() {
        load();
        singleton = this;
    }

    private void load() {
         FONT_BATTLE_PORTRAIT = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.WHITE);
         FONT_FIGHT = FontManager.getFont(FontManager.KEN_VECTOR_FUTURE, 16, Color.WHITE);
         FONT_VS = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 48, Color.RED);

         TEXTURE_SQUARE = new TextureRegion(new Texture(Gdx.files.local("launcher/battle_practice/sprites/battle-square.png")));
         TEXTURE_SQUARE_HOVERED = new TextureRegion(new Texture(Gdx.files.local("launcher/battle_practice/sprites/battle-square-hovered.png")));

         TEXTURE_BACK_BUTTON = new TextureRegion(new Texture(Gdx.files.local("launcher/battle_practice/sprites/back-button.png")));
        TEXTURE_BACK_BUTTON_HOVERED = new TextureRegion(new Texture(Gdx.files.local("launcher/battle_practice/sprites/back-button-hovered.png")));

        TEXTURE_BG = new TextureRegion(new Texture(Gdx.files.local("launcher/common/sprites/bg.png")));

         TEXTURE_PLAYER_AREA = new TextureRegion(new Texture(Gdx.files.local("launcher/battle_practice/sprites/player-area.png")));
         TEXTURE_AI_AREA = new TextureRegion(new Texture(Gdx.files.local("launcher/battle_practice/sprites/ai-area.png")));

         TEXTURE_FIGHT_BUTTON = new TextureRegion(new Texture(Gdx.files.local("launcher/battle_practice/sprites/fight-button.png")));
         TEXTURE_FIGHT_BUTTON_HOVER = new TextureRegion(new Texture(Gdx.files.local("launcher/battle_practice/sprites/fight-button-hover.png")));
    }
}
