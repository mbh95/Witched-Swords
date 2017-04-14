package com.comp460.screens.launcher.practice.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.comp460.assets.FontManager;
import com.comp460.assets.SpriteManager;

/**
 * Created by matthewhammond on 2/11/17.
 */
public class BattlePracticeAssets {

    public static final BitmapFont FONT_BATTLE_PORTRAIT = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.WHITE, Color.BLACK, 1);
    public static final BitmapFont FONT_VS = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 48, Color.RED);
    public static final BitmapFont FONT_INFO = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.WHITE);
    public static final BitmapFont FONT_READY = FontManager.getFont(FontManager.KEN_VECTOR_FUTURE, 20, Color.ORANGE, Color.BLACK, 2);

    public static final TextureRegion TEXTURE_SQUARE = SpriteManager.BATTLE_PRACTICE.findRegion("battle-square");
    public static final TextureRegion TEXTURE_SQUARE_BLUE = SpriteManager.BATTLE_PRACTICE.findRegion("battle-square-blue");
    public static final TextureRegion TEXTURE_SQUARE_RED = SpriteManager.BATTLE_PRACTICE.findRegion("battle-square-red");


    public static final TextureRegion TEXTURE_BG = SpriteManager.COMMON.findRegion("bg");;
//    public static final NinePatch NP_INFO_BG = new NinePatch(SpriteManager.BATTLE_PRACTICE.findRegion("info-bg-ns"), 2, 2, 2, 2);
    public static final NinePatch NP_INFO_BG = new NinePatch(SpriteManager.BATTLE_PRACTICE.findRegion("info-bg-ns"), 2, 2, 2, 2);

    public static final TextureRegion TEXTURE_READY_BANNER = SpriteManager.BATTLE_PRACTICE.findRegion("ready-banner");

    public static final TextureRegion TEXTURE_PLAYER_AREA = SpriteManager.BATTLE_PRACTICE.findRegion("player-area");
    public static final TextureRegion TEXTURE_AI_AREA = SpriteManager.BATTLE_PRACTICE.findRegion("ai-area");

    public static final TextureRegion TEXTURE_FIGHT_BUTTON = SpriteManager.BATTLE_PRACTICE.findRegion("fight-button");;

    public static final NinePatch NINEPATCH_CURSOR = new NinePatch(SpriteManager.MAIN_MENU.findRegion("cursor-tiny"), 2, 2, 2, 2);

    public static final TextureRegion TEXTURE_BACK_BUTTON = SpriteManager.COMMON.findRegion("back-button");
}
