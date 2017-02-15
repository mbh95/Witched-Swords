package com.comp460.launcher.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.common.FontManager;
import com.comp460.common.SpriteManager;

/**
 * Created by matthewhammond on 2/11/17.
 */
public class MainMenuAssets {

    public static final TextureRegion TEXTURE_TITLE = SpriteManager.COMMON.findRegion("title");
    public static final TextureRegion TEXTURE_TITLE_BG = SpriteManager.COMMON.findRegion("title-outline");

    public static final TextureRegion TEXTURE_BG = SpriteManager.COMMON.findRegion("bg");

    public static final TextureRegion TEXTURE_BUTTON_NORMAL = SpriteManager.MAIN_MENU.findRegion("mm_button_normal");
    public static final TextureRegion TEXTURE_BUTTON_PRESSED = SpriteManager.MAIN_MENU.findRegion("mm_button_pressed");
    public static final TextureRegion TEXTURE_CURSOR = SpriteManager.MAIN_MENU.findRegion("cursor");

    public static final BitmapFont FONT_MENU_ITEM = FontManager.getFont(FontManager.KEN_PIXEL, 16, Color.WHITE);
}
