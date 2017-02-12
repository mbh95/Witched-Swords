package com.comp460.launcher.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.FontManager;

/**
 * Created by matthewhammond on 2/11/17.
 */
public class MainMenuAssets {

    private static MainMenuAssets singleton;

    public BitmapFont FONT_MENU_ITEM;

    public TextureRegion TEXTURE_TITLE;
    public TextureRegion TEXTURE_TITLE_BG;

    public TextureRegion TEXTURE_BG;

    public TextureRegion TEXTURE_BUTTON_NORMAL;
    public TextureRegion TEXTURE_BUTTON_PRESSED;
    public TextureRegion TEXTURE_CURSOR;

    public static MainMenuAssets getInstance() {
        if (singleton == null) {
            singleton = new MainMenuAssets();
        }
        return singleton;
    }

    private MainMenuAssets() {
        load();
        singleton = this;
    }

    private void load() {

        TEXTURE_TITLE = new TextureRegion(new Texture(Gdx.files.internal("launcher/common/sprites/title.png")));
        TEXTURE_TITLE_BG = new TextureRegion(new Texture(Gdx.files.internal("launcher/common/sprites/title-outline.png")));

        TEXTURE_BG = new TextureRegion(new Texture(Gdx.files.internal("launcher/common/sprites/bg.png")));

        TEXTURE_BUTTON_NORMAL= new TextureRegion(new Texture(Gdx.files.local("launcher/main_menu/sprites/mm_button_normal.png")));
        TEXTURE_BUTTON_PRESSED= new TextureRegion(new Texture(Gdx.files.local("launcher/main_menu/sprites/mm_button_pressed.png")));
        TEXTURE_CURSOR= new TextureRegion(new Texture(Gdx.files.local("launcher/main_menu/sprites/cursor.png")));

        FONT_MENU_ITEM = FontManager.getFont(FontManager.KEN_PIXEL, 16, Color.WHITE);
    }
}
