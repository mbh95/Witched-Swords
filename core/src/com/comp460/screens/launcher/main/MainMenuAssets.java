package com.comp460.screens.launcher.main;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.FontManager;
import com.comp460.assets.SpriteManager;

/**
 * Created by matthewhammond on 2/11/17.
 */
public class MainMenuAssets {

    public static final TextureRegion TEXTURE_TITLE = SpriteManager.COMMON.findRegion("title");
    public static final TextureRegion TEXTURE_TITLE_BG = SpriteManager.COMMON.findRegion("title-outline");

    public static final TextureRegion TEXTURE_BG = SpriteManager.COMMON.findRegion("bg"); //SpriteManager.COMMON.findRegion("bg");
    public static final TextureRegion TEXTURE_GRID = SpriteManager.COMMON.findRegion("grid"); //SpriteManager.COMMON.findRegion("bg");


    public static final NinePatch NINEPATCH_BUTTON = new NinePatch(SpriteManager.MAIN_MENU.findRegion("mm_button_normal"), 8, 8, 8, 8);

    public static final NinePatch NINEPATCH_CURSOR = new NinePatch(SpriteManager.MAIN_MENU.findRegion("cursor-tiny"), 2, 2, 2, 2);

    public static final BitmapFont FONT_MENU_ITEM = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.WHITE, Color.BLACK, 1);

}
